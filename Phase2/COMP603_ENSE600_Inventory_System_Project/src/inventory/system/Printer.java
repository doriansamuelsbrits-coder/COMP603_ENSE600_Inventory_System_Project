package inventory.system;

import java.io.*;
import java.util.*;
/*
this is the main printer class where the in formation regarding the hashmap lists are
printed to the txt file, however there is also a display of the current inventory that 
the user can see when selecting the current inventory on the CLI.
*/
public class Printer {

    public static void displayInventory(Map<String, Item> inventory) {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("\nCurrent Inventory:");
        System.out.println("Item Code  | Item Name  | Qty | MOQ | Stk Min |  Price ");
        System.out.println("-------------------------------------------------------");
        for (Item item : inventory.values()) {
            System.out.println(item.toString());
        }
    }

    public static void displayInventoryItemsBrief(Map<String, Item> inventory) {
        for (Item item : inventory.values()) {
            System.out.println(item.itemCode + " - " + item.itemName + " | Qty: " + item.qty);
        }
    }

    public static void processOrderRequest(Map<String, Item> inventory, String filePath) {
        File file = new File(filePath);
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            boolean anyOrders = false;

            pw.println("Order Request Report - Generated on: " + new Date());
            pw.println();
            pw.println("Item Code  | Item Name  | Order Qty |  Price  | Total Price ");
            pw.println("-----------------------------------------------------------");

            for (Item item : inventory.values()) {
                if (item.qty < item.stkMin) {
                    int deficit = item.stkMin - item.qty;
                    int orderQty = ((deficit + item.moq - 1) / item.moq) * item.moq;
                    double totalPrice = orderQty * item.price;

                    pw.printf("%-10s | %-10s | %9d | %7.2f | %11.2f%n",
                            item.itemCode, item.itemName, orderQty, item.price, totalPrice);

                    System.out.println("Generated order for: " + item.itemName +
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
            System.out.println("Error writing order request file: " + e.getMessage());
        }
    }
}

