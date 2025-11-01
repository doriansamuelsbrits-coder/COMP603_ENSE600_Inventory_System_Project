package inventory.system;
import java.util.*;

//for git upload to 

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
    private static Map<String, Security> security = new HashMap<>();

    public static String getInventoryFile(){return INVENTORY_FILE;}
    
    public static String getOrderRequestFile(){return GENERATED_ORDER_FILE;}
    
    public static Map<String,Item> getHashMap(){return inventory;}
    
    public static Map<String,Security> getSecurityMap(){return security;}
    
    public static String getPrefix(){return ITEM_CODE_PREFIX;}
}
