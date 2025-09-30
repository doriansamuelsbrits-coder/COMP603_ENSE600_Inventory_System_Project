package inventory.system;

import java.util.*;

/*
This class focuses on the main elements for the inventory system at startup, 
this is where the user will be interacting with the CLI and for future work 'the GUI'
this is where the contents fo the hashmap is being stored before being uploaded to the txt file
*/
public class InventorySystem {
    private static final String INVENTORY_FILE = "./resources/hashmap.txt";
    private static final String GENERATED_ORDER_FILE = "./resources/order_request.txt";
    private static final String ITEM_CODE_PREFIX = "AB";
    private static Map<String, Item> inventory = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        if (!Filter.login(scanner)) {
            scanner.close();
            return;
        }
        
        InventoryManager.loadInventory(inventory, INVENTORY_FILE);

        while (true) {
            System.out.println("\nPlease select Operation: \n (1) Stock Entry \n (2) Stock Exit \n (3) Current Inventory Status \n (4) Order Request \n (5) Remove Item \n (x) TO END THE PROGRAM");
            String option = scanner.nextLine().trim();

            try {
                if (option.equalsIgnoreCase("x")) break;
                int selection = Integer.parseInt(option);

                switch (selection) {
                    case 1:
                        System.out.println("Please select Operation: \n (1) Add new item \n (2) Add quantity to existing item");
                        int add = Integer.parseInt(scanner.nextLine().trim());
                        if (add == 1) updatelist.addNewItem(scanner, inventory, ITEM_CODE_PREFIX, INVENTORY_FILE);
                        else if (add == 2) updatelist.updateExistingItemQty(scanner, inventory, true, INVENTORY_FILE);
                        else System.out.println("Incorrect input");
                        break;
                    case 2: updatelist.updateExistingItemQty(scanner, inventory, false, INVENTORY_FILE); break;
                    case 3: Printer.displayInventory(inventory); break;
                    case 4: Printer.processOrderRequest(inventory, GENERATED_ORDER_FILE); break;
                    case 5: updatelist.removeItem(scanner, inventory, INVENTORY_FILE); break;
                    default: System.out.println("Incorrect input");
                }
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input");
            }
        }

        scanner.close();
        InventoryManager.saveInventory(inventory, INVENTORY_FILE);
        System.out.println("Program terminated.");
    }
}
