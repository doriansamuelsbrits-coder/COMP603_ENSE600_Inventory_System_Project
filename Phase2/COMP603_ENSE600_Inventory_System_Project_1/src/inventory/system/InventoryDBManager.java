package inventory.system;

import java.sql.Connection;     //database connection service
import java.sql.DriverManager;  //Existing table removal
import java.sql.Statement;      //Statement Generation for tables
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;   //Exceptions
import java.util.HashMap;
import java.util.Map;           //For inventory handling

//Inventory Database URL: jdbc:derby://localhost:1527/InventoryDB - Old version no-longer being used.
//Database Username: COMP603
//Database Password: COMP603
public class InventoryDBManager {

    public static Connection conn;
    public static String url = "jdbc:derby:InventoryDataBase_Ebd;create=true";
    private static String username = "COMP603";
    private static String password = "COMP603";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private boolean tableExists(String tableName) throws SQLException {
        conn = getConnection();
        var md = conn.getMetaData();
        try (var rs = md.getTables(null, null, tableName.toUpperCase(), null)) {
            return rs.next();
        }
    }

    public boolean usernameExists(String username) {
        try {
            conn = getConnection();
            String query = "SELECT 1 FROM Security WHERE USERNAME = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            var rs = ps.executeQuery();
            return rs.next(); // true if a row exists
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }
    }

    public Map<String, Security> LoadSecurityTable() {
        Map<String, Security> map = new HashMap<>();

        String query = "SELECT USERNAME, NAME, PASSWORD, STATUS FROM SECURITY";

        try (Connection conn = DriverManager.getConnection(url, username, password); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {

            while (rs.next()) {
                String uid = rs.getString("USERNAME");
                String name = rs.getString("NAME");
                String password = rs.getString("PASSWORD");
                String status = rs.getString("STATUS");

                // Create the item and add it to the map
                Security sec = new Security(name, uid, password, status);
                map.put(uid, sec);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, Item> LoadInventoryTable() {
        Map<String, Item> map = new HashMap<>();

        String query = "SELECT CODE, NAME, QTY, MOQ, STK_MIN, PRICE FROM INVENTORY";

        try (Connection conn = DriverManager.getConnection(url, username, password); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {

            while (rs.next()) {
                String code = rs.getString("CODE");
                String name = rs.getString("NAME");
                int qty = rs.getInt("QTY");
                int moq = rs.getInt("MOQ");
                int stkMin = rs.getInt("STK_MIN");
                double price = rs.getDouble("PRICE");

                // Create the item and add it to the map
                Item item = new Item(code, name, qty, moq, stkMin, price);
                map.put(code, item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    /*
    This Database Method establishes an update to a preexisting Database table Called INVENTORY,
    if an item does not exist make a new item
     */
    public void UpdateInventoryTable(Map<String, Item> inventory) {
        String createTable = "CREATE TABLE Inventory (ID INT, "
                + "CODE VARCHAR(50), "
                + "NAME VARCHAR(50), "
                + "QTY INT, MOQ INT, "
                + "STK_MIN INT, "
                + "PRICE DOUBLE)";

        try {
            conn = getConnection();
            Statement statement = conn.createStatement();

            if (!tableExists("Inventory")) {
                System.out.println("Table Inventory does not exist");
                statement.executeUpdate(createTable);
                System.out.println("Table Inventory has been Created");
            }

            String insertIntoSQL = "INSERT INTO Inventory "
                    + "(CODE, NAME, QTY, MOQ, STK_MIN, PRICE)"
                    + " VALUES (?,?,?,?,?,?)";
            String updateSQL = "UPDATE Inventory SET "
                    + "NAME=?, QTY=?, MOQ=?, STK_MIN=?, PRICE=? WHERE CODE=?";

            PreparedStatement insertPS = conn.prepareStatement(insertIntoSQL);
            PreparedStatement updatePS = conn.prepareStatement(updateSQL);

            for (Item item : inventory.values()) {
                updatePS.setString(1, item.itemName);
                updatePS.setInt(2, item.qty);
                updatePS.setInt(3, item.moq);
                updatePS.setInt(4, item.stkMin);
                updatePS.setDouble(5, item.price);
                updatePS.setString(6, item.itemCode); //ITEMCODE must go last because of string requirements
                int updatedRows = updatePS.executeUpdate();

                // If no rows were updated, insert the item
                if (updatedRows == 0) {
                    insertPS.setString(1, item.itemCode);
                    insertPS.setString(2, item.itemName);
                    insertPS.setInt(3, item.qty);
                    insertPS.setInt(4, item.moq);
                    insertPS.setInt(5, item.stkMin);
                    insertPS.setDouble(6, item.price);
                    insertPS.executeUpdate();
                }
            }

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }
    
    
     public boolean deleteItemFromDatabase(String code) throws SQLException {
        String deleteSQL = "DELETE FROM Inventory WHERE CODE=?";
        try (Connection conn = getConnection();
             PreparedStatement deletePS = conn.prepareStatement(deleteSQL)) {
            
            deletePS.setString(1, code);
            int rowsAffected = deletePS.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /*
    This Database Method will display and currently updated orderrequest table which
    will update the database table by removing any item that doesnt meet the requirements for new orders
     */
    public void InsertIntoOrderRequestTable(Map<String, Item> inventory) {
        try {
            conn = getConnection();
            Statement statement = conn.createStatement();
            String table = "OrderRequest";

            if (tableExists(table)) {
                statement.executeUpdate("DROP TABLE " + table);
                System.out.println("Table " + table + " Has Been Dropped");
            } else {
                System.out.println("Table " + table + "does not exist.");
            }

            String createTable = "CREATE TABLE " + table
                    + "(CODE VARCHAR(50), "
                    + "NAME VARCHAR(50), "
                    + "QTY INT, MOQ INT, "
                    + "STK_MIN INT, "
                    + "PRICE FLOAT)";
            statement.executeUpdate(createTable);

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }

    /*
    This method will establish a security table for usernames and passwords for 
    users who want to use the GUI application which is first run through a registration
    it will also check if there a currently registered user in the database.
     */
    public void UpdateSecurityTable(Map<String, Security> security) {
        String createTable = "CREATE TABLE Security (ID INT, "
                + "USERNAME VARCHAR(50),"
                + "NAME VARCHAR(50),"
                + "PASSWORD VARCHAR(50), "
                + "STATUS VARCHAR(50)) ";
        try {
            conn = getConnection();
            Statement statement = conn.createStatement();

            if (!tableExists("Security")) {
                System.out.println("Table Security does not exist");
                statement.executeUpdate(createTable);
                System.out.println("Table Security has been created");
            }

            String insertIntoSQL = "INSERT INTO Security "
                    + "(USERNAME, NAME, PASSWORD, STATUS)"
                    + " VALUES (?,?,?,?)";
            String updateSQL = "UPDATE Security SET "
                    + "NAME=?, PASSWORD=?, STATUS=? WHERE USERNAME=?";

            PreparedStatement iSQL = conn.prepareStatement(insertIntoSQL);
            PreparedStatement uSQL = conn.prepareStatement(updateSQL);
            //sources the Username and passwords stored in the security
            for (Security sec : security.values()) {

                uSQL.setString(4, sec.getUserName());       //gets employees username
                uSQL.setString(1, sec.getEmployeeName());   //gets employees name
                uSQL.setString(2, sec.getPassword());       //gets employees password
                uSQL.setString(3, sec.getPosition());       //gets employee position in the company

                int updatedRows = uSQL.executeUpdate();

                if (updatedRows == 0) {
                    iSQL.setString(1, sec.getUserName());
                    iSQL.setString(2, sec.getEmployeeName());
                    iSQL.setString(3, sec.getPassword());
                    iSQL.setString(4, sec.getPosition());
                    iSQL.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }

    }
}
