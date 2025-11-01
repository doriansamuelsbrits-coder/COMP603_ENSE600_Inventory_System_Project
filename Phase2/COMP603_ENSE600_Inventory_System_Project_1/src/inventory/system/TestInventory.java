package inventory.system;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TestInventory {

    // Generate the next available key based on existing inventory
    public String TestKeys(Map<String, Item> inventory) {
        int maxNumber = 0;
        String prefix = InventorySystem.getPrefix();

        for (String key : inventory.keySet()) {
            if (key.startsWith(prefix) && key.length() > prefix.length()) {
                try {
                    int n = Integer.parseInt(key.substring(prefix.length()));
                    if (n > maxNumber) {
                        maxNumber = n;
                    }
                } catch (NumberFormatException ignore) {
                }
            }
        }
        return String.format("%s%04d", prefix, maxNumber + 1);
    }

    // Modify dummy item (example: increase quantity)
    public boolean Modifydummy(Map<String, Item> inventory, String key) {
        if (inventory.containsKey(key)) {
            inventory.get(key).qty += 10; // example modification
            return true;
        }
        return false;
    }

    // Check if a file exists
    public boolean TestFile(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public boolean testInventoryFile() {
        boolean exists = TestFile(InventorySystem.getInventoryFile());
        System.out.println("Inventory file exists: " + (exists ? "PASS" : "FAIL"));
        return exists;
    }

    public boolean testOrderRequestFile() {
        boolean exists = TestFile(InventorySystem.getOrderRequestFile());
        System.out.println("Order request file exists: " + (exists ? "PASS" : "FAIL"));
        return exists;
    }

    public boolean testKeyGeneration() {
        Map<String, Item> mockInventory = new HashMap<>();
        mockInventory.put("AB0001", new Item("AB0001", "ItemA", 10, 5, 2, 9.99));
        mockInventory.put("AB0002", new Item("AB0002", "ItemB", 20, 10, 5, 19.99));

        String nextKey = TestKeys(mockInventory);
        boolean success = "AB0003".equals(nextKey);
        System.out.println("Key generation correct: " + (success ? "PASS" : "FAIL"));
        return success;
    }

    public boolean testKeyGenerationEmpty() {
        Map<String, Item> emptyInventory = new HashMap<>();
        String nextKey = TestKeys(emptyInventory);
        boolean success = "AB0001".equals(nextKey);
        System.out.println("Key generation on empty inventory correct: " + (success ? "PASS" : "FAIL"));
        return success;
    }

    public boolean testDummyInsertionAndModification() {
        Map<String, Item> mockInventory = new HashMap<>();
        String dummyKey = TestKeys(mockInventory);
        Item dummyItem = new Item(dummyKey, "Dummy", 0, 0, 1, 1.00);

        // Insert dummy
        mockInventory.put(dummyKey, dummyItem);

        // Modify dummy
        boolean modified = Modifydummy(mockInventory, dummyKey);
        boolean qtyCorrect = mockInventory.get(dummyKey).qty == 10;

        // Cleanup
        mockInventory.remove(dummyKey);
        boolean cleaned = !mockInventory.containsKey(dummyKey);

        boolean success = modified && qtyCorrect && cleaned;
        System.out.println("Dummy insertion, modification, and cleanup: " + (success ? "PASS" : "FAIL"));
        return success;
    }

    public boolean testGUISecurityHashExists() {
        maingui m = new maingui(); // Create your GUI (assuming its constructor sets up maps)
        Map<String, Security> secMap = m.getSecurityMap(); // Retrieve the map

        if (secMap == null) {
            System.out.println("Security map: FAIL (map is null)");
            return false;
        }

        if (secMap.isEmpty()) {
            System.out.println("Security map: WARN (map exists but is empty)");
            return true; // not necessarily a fail, but you might choose to fail it
        }

        System.out.println("Security map: PASS (map exists and has entries)");
        return true;
    }

    public boolean testGUIInventoryHashExists() {
        maingui m = new maingui(); // Create your GUI (assuming its constructor sets up maps)
        Map<String, Item> itemMap = m.getItemMap(); // Retrieve the map

        if (itemMap == null) {
            System.out.println("Security map: FAIL (map is null)");
            return false;
        }

        if (itemMap.isEmpty()) {
            System.out.println("Security map: WARN (map exists but is empty)");
            return true; // not necessarily a fail, but you might choose to fail it
        }

        System.out.println("Security map: PASS (map exists and has entries)");
        return true;
    }

    public static void main(String[] args) {
        TestInventory ti = new TestInventory();

        System.out.println("=== Running Inventory System Unit Tests ===");

        boolean allPassed = true;

        allPassed &= ti.testInventoryFile();
        allPassed &= ti.testOrderRequestFile();
        allPassed &= ti.testKeyGeneration();
        allPassed &= ti.testKeyGenerationEmpty();
        allPassed &= ti.testDummyInsertionAndModification();
        allPassed &= ti.testGUISecurityHashExists();
        allPassed &= ti.testGUIInventoryHashExists();

        if (allPassed) {
            System.out.println("==================");
            System.out.println(" ALL TESTS PASSED");
            System.out.println("==================");
        } else {
            System.out.println("===================");
            System.out.println(" SOME TESTS FAILED");
            System.out.println("===================");
        }
    }
}
