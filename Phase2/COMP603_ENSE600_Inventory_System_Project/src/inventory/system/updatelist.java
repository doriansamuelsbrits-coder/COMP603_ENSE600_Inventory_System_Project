package inventory.system;

import java.util.*;

/*
this class focuses on the adding of new item's, there quantities as well as the 
removal of items from the hashmap. 
*/

public class updatelist {

    public static void addNewItem(Scanner scanner, Map<String, Item> inventory, String prefix, String filePath) {
        System.out.println("Adding new item: Please enter details.");
        System.out.print("Item Code (start with " + prefix + "): ");
        String code = scanner.nextLine().trim();

        if (!code.startsWith(prefix)) {
            System.out.println("Invalid Item Code. Must start with: " + prefix);
            return;
        }
        if (inventory.containsKey(code)) {
            System.out.println("Item Code already exists. Use update for quantities.");
            return;
        }

        System.out.print("Item Name: ");
        String name = scanner.nextLine().trim();
        int qty = Filter.readIntegerInput(scanner, "Quantity: ");
        int moq = Filter.readIntegerInput(scanner, "MOQ (Minimum Order Quantity): ");
        int stkMin = Filter.readIntegerInput(scanner, "Stock Minimum: ");
        double price = Filter.readDoubleInput(scanner, "Price: ");

        inventory.put(code, new Item(code, name, qty, moq, stkMin, price));
        InventoryManager.saveInventory(inventory, filePath);
        System.out.println("Item added successfully.");
    }

    public static void updateExistingItemQty(Scanner scanner, Map<String, Item> inventory, boolean isAdd, String filePath) {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        Printer.displayInventoryItemsBrief(inventory);
        System.out.print("Enter Item Code: ");
        String code = scanner.nextLine().trim();

        Item item = inventory.get(code);
        if (item == null) {
            System.out.println("Item code not found.");
            return;
        }

        int changeQty = Filter.readIntegerInput(scanner, "Enter quantity to " + (isAdd ? "add:" : "remove: "));
        if (!isAdd && item.qty < changeQty) {
            System.out.println("Not enough stock to remove.");
            return;
        }
        item.qty += isAdd ? changeQty : -changeQty;
        InventoryManager.saveInventory(inventory, filePath);
        System.out.println("Updated quantity of " + item.itemName + ": " + item.qty);
    }

    public static void removeItem(Scanner scanner, Map<String, Item> inventory, String filePath) {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        Printer.displayInventoryItemsBrief(inventory);
        System.out.print("Enter Item Code to remove: ");
        String code = scanner.nextLine().trim();

        if (inventory.remove(code) != null) {
            InventoryManager.saveInventory(inventory, filePath);
            System.out.println("Item " + code + " removed from inventory.");
        } else {
            System.out.println("Item code not found.");
        }
    }
}
