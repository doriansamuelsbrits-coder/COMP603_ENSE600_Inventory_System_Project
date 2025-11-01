package inventory.system;

import java.util.*;

/*
this class focuses on the adding of new item's, there quantities as well as the 
removal of items from the hashmap. 
*/

public class updatelist {

    // ... existing methods (addNewItem, updateExistingItemQty, removeItem) ...
    
    // NOTE: For the context of this GUI application, we'll assume the InventoryDBManager 
    // is the preferred method for saving changes to the database.

    /**
     * Updates the quantity of an item in the inventory map and saves the changes 
     * to the database.
     * * @param inventory The Map of inventory items (Item Code -> Item object).
     * @param code The String code to identify the item.
     * @param qty The integer quantity to add or remove.
     * @param isAdd True to increment the quantity, False to decrease.
     * @return True if the quantity was successfully updated and saved, False otherwise.
     */
    public static boolean updateItemQuantity(Map<String, Item> inventory, String code, int qty, boolean isAdd) {
        
        if (code == null || code.trim().isEmpty() || qty <= 0) {
            System.err.println("Invalid item code or quantity.");
            return false;
        }

        Item item = inventory.get(code);
        if (item == null) {
            System.err.println("Item code " + code + " not found in inventory.");
            return false;
        }

        int changeQty = isAdd ? qty : -qty;
        int newQty = item.qty + changeQty;

        if (!isAdd && newQty < 0) {
            System.err.println("Cannot remove " + qty + ". Not enough stock for item " + code + " (Current: " + item.qty + ").");
            return false;
        }

        item.qty = newQty;
        
        // Update the inventory in the persistent storage (Database)
        try {
            InventoryDBManager dbManager = new InventoryDBManager();
            dbManager.UpdateInventoryTable(inventory); 
            System.out.println("Updated quantity of " + item.itemName + " to: " + item.qty);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving inventory changes to database: " + e.getMessage());
            // Optionally, you might want to revert the in-memory change here if the save fails
            item.qty -= changeQty; 
            return false;
        }
    }
    
    
    public static boolean removeItemFromInventory(Map<String, Item> inventory, String code) {
        if (code == null || code.trim().isEmpty()) {
            System.err.println("Invalid item code provided for removal.");
            return false;
        }

        // 1. Check if item exists in map
        if (!inventory.containsKey(code)) {
            System.err.println("Item code " + code + " not found in local inventory map.");
            return false;
        }
        
        // 2. Remove from persistent storage (Database)
        try {
            InventoryDBManager dbManager = new InventoryDBManager();
            boolean dbSuccess = dbManager.deleteItemFromDatabase(code);
            
            // 3. Remove from in-memory map only after database action succeeds
            if (dbSuccess) {
                inventory.remove(code); // Remove locally
                System.out.println("Item " + code + " successfully removed from inventory and database.");
                return true;
            } else {
                System.err.println("Item " + code + " found in map but database deletion failed or record did not exist.");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Error deleting item from database: " + e.getMessage());
            return false;
        }
    }
    
    
}
   