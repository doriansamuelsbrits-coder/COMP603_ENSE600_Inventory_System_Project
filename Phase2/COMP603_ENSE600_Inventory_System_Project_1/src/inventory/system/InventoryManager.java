package inventory.system;

import java.io.*;
import java.util.*;
/*
this class focuses on the management of the hashmap for the inventory, 
and with that saves the information to the inventories txt files.
this class contains:
1. saveInventory method - to store the information in the hasmap to the main invetnory txt file
2. loadInventory method - to obtain the inventory list from the txt files.
3. loadDemo method - this is a default method which contains default items if the txt file is 
missing or is deleted
*/
public class InventoryManager {

    public static void saveInventory(Map<String, Item> inventory, String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("Item Code  | Item Name  | Qty | MOQ | Stk Min |  Price ");
            pw.println("-------------------------------------------------------");

            for (Item item : inventory.values()) {
                pw.println(item.toString());
            }
            System.out.println("Inventory saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public static void loadInventory(Map<String, Item> inventory, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Inventory file not found. Creating with demo items...");
            loadDemoInventory(inventory);
            saveInventory(inventory, filePath);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (!headerSkipped) { headerSkipped = true; continue; }
                if (line.startsWith("-")) continue;

                String[] parts = line.split("\\|");
                if (parts.length != 6) continue;

                String code = parts[0].trim();
                String name = parts[1].trim();
                int qty = Integer.parseInt(parts[2].trim());
                int moq = Integer.parseInt(parts[3].trim());
                int stkMin = Integer.parseInt(parts[4].trim());
                double price = Double.parseDouble(parts[5].trim());

                inventory.put(code, new Item(code, name, qty, moq, stkMin, price));
            }
            System.out.println("Inventory loaded from " + filePath);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading inventory file: " + e.getMessage());
        }
    }

    public static void loadDemoInventory(Map<String, Item> inventory) {
        inventory.put("AB0001", new Item("AB0001", "ItemA", 50, 10, 20, 15.99));
        inventory.put("AB0002", new Item("AB0002", "ItemB", 30, 5, 10, 9.99));
    }
}
        

