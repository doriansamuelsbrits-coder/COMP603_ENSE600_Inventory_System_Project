package inventory.system;

import java.io.*;
import java.util.*;
/*
this is the main printer class where the in formation regarding the hashmap lists are
printed to the txt file, however there is also a display of the current inventory that 
the user can see when selecting the current inventory on the CLI.
*/
public class Printer {

    public static void generateInventoryStatusDocument(Map<String, Item> inventory, String filePath) {
        File file = new File("./resources/hashmap.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            
            int totalItems = inventory.size();
            int totalQuantity = 0;
            int belowMinimumStock = 0;
            int outOfStock = 0;
            
            pw.println("Inventory Status Report - Generated on: " + new Date());
            pw.println("-------------------------------------------------------");
            
            // Calculate and write summary statistics
            for (Item item : inventory.values()) {
                totalQuantity += item.qty;
                if (item.qty > 0 && item.qty < item.stkMin) {
                    belowMinimumStock++;
                }
                if (item.qty == 0) {
                    outOfStock++;
                }
            }
            
            pw.println("TOTAL UNIQUE ITEMS: " + totalItems);
            pw.println("TOTAL QUANTITY:     " + totalQuantity);
            pw.println("BELOW MINIMUM:      " + belowMinimumStock);
            pw.println("OUT OF STOCK:       " + outOfStock);
            pw.println("-------------------------------------------------------");
            
            // Write detailed item list header
            pw.printf("%-10s | %-12s | %-5s | %-5s | %-8s | %-8s%n","Item Code", "Item Name", "Qty", "MOQ", "Stk Min", "Price");
            pw.println("-----------------------------------------------------------------------------");
            
            // Write individual item details
            for (Item item : inventory.values()) {
                pw.printf("%-10s | %-12s | %-5d | %-5d | %-8d | %-8.2f%n", 
                        item.itemCode, item.itemName, item.qty, item.moq, item.stkMin, item.price);
            }
            
            System.out.println("Inventory Status document created at: " + filePath);
            
        } catch (IOException e) {
            System.out.println("Error writing Inventory Status document: " + e.getMessage());
        }
    }
    
    public static void generateOrderRequestDocument(Map<String, Item> inventory, String filePath) {
        File file = new File("./resources/order_request.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            boolean anyOrders = false;

            pw.println("Order Request Report - Generated on: " + new Date());
            pw.println();
            pw.println("Item Code  | Item Name  | Order Qty |  Price  | Total Price ");
            pw.println("-----------------------------------------------------------");

            for (Item item : inventory.values()) {
                if (item.qty < item.stkMin) {
                    // Calculate deficit and order quantity (multiple of MOQ)
                    int deficit = item.stkMin - item.qty;
                    int orderQty = ((deficit + item.moq - 1) / item.moq) * item.moq;
                    double totalPrice = orderQty * item.price;

                    // Write the formatted order line to the file
                    pw.printf("%-10s | %-10s | %9d | %7.2f | %11.2f%n",
                            item.itemCode, item.itemName, orderQty, item.price, totalPrice);

                    System.out.println("Generated order line for: " + item.itemName +
                            " | Order Qty: " + orderQty +
                            " | Total Price: " + String.format("%.2f", totalPrice));

                    anyOrders = true;
                }
            }

            if (!anyOrders) {
                pw.println("No items below stock minimum. No order request generated.");
                System.out.println("No items below stock minimum. No order request generated.");
            } else {
                System.out.println("Order request file created at: " + filePath);
            }

        } catch (IOException e) {
            System.out.println("Error writing order request document: " + e.getMessage());
        }
    }
}

