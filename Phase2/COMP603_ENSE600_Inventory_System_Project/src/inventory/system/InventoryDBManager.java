package inventory.system;
import java.sql.Connection;     //database connection service
import java.sql.DriverManager;  //Existing table removal
import java.sql.Statement;      //Statement Generation for tables
import java.sql.PreparedStatement;
import java.sql.SQLException;   //Exceptions
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.util.Map;           //For inventory handling



//Inventory Database URL: jdbc:derby://localhost:1527/InventoryDB
//Database Username: COMP603
//Database Password: COMP603
public class InventoryDBManager {

    public static Connection conn;
    public static String url = "jdbc:derby://localhost:1527/InventoryDB";
    private static String username = "COMP603";
    private static String password = "COMP603";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    public void InsertIntoInventoryTable(Map<String, Item> inventory) {
        String createTable = "CREATE TABLE Inventory (ID INT, "
                    + "CODE VARCHAR(50), "
                    + "NAME VARCHAR(50), "
                    + "QTY INT, MOQ INT, "
                    + "STK_MIN INT, "
                    + "PRICE DOUBLE)";
        
        try{ conn = getConnection();
            Statement statement = conn.createStatement();
           
            statement.executeUpdate("DROP TABLE IF EXISTS Inventory");
            System.out.println("Table Inventory Has Been Dropped");
            
            statement.executeUpdate(createTable);
            
            String insertIntoSQL = "INSERT INTO Inventory "
                    + "(CODE, NAME, QTY, MOQ, STK_MIN, PRICE)"
                    + " VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(insertIntoSQL);
            for(Item item: inventory.values())
            {
                ps.setString(1, item.itemCode);
                ps.setString(2, item.itemName);
                ps.setInt(3, item.qty);
                ps.setInt(4, item.moq);
                ps.setInt(5, item.stkMin);
                ps.setDouble(6, item.price);
                ps.executeUpdate();
            }
            
        }
        catch(SQLException ex){
            System.err.println("SQLException: " + ex.getMessage());
        } 
    }

    public void InsertIntoOrderRequestTable() {
        try{conn = getConnection();
            Statement statement = conn.createStatement();
            String table = "OrderRequest";
            
            statement.executeUpdate("DROP TABLE IF EXISTS"+table);
            System.out.println("Tabel"+ table+ "Has Been Dropped");
            
            String createTable = "CREATE TABLE "+table+"(ID INT, "
                    + "CODE VARCHAR(50), "
                    + "NAME VARCHAR(50), "
                    + "QTY INT, MOQ INT, "
                    + "STK_MIN INT, "
                    + "PRICE FLOAT)";
            statement.executeUpdate(createTable);
        }
        catch(SQLException ex){
            System.err.println("SQLException: " + ex.getMessage());
        } 
    }

    public void InsertIntoSecurityTable(Map<String, Security> security) {
        String createTable = "CREATE TABLE Security (ID INT, "
                    + "NAME VARCHAR(50), "
                    + "STATUS VARCHAR(50), "
                    + "USERNAME VARCHAR(50), "
                    + "PASSWORD VARCHAR(50))";
        try{
            conn = getConnection();
            Statement statement = conn.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS Security");
            System.out.println("Table Security has been dropped");
            statement.executeUpdate(createTable);
            String insertIntoSQL = "INSERT INTO Security "
                    + "(NAME, STATUS, USERNAME, PASSWORD)"
                    + " VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(insertIntoSQL);
            //sources the Username and passwords stored in the security
            for(Security sec: security.values())
            {
                ps.setString(1, sec.getEmployeeName());
                ps.setString(2, sec.getPosition());
                ps.setString(3, sec.getUserName());
                ps.setString(4, sec.getPassword());
                ps.executeUpdate();
            }
        }
        catch(SQLException ex){
            System.err.println("SQLException: " + ex.getMessage());
        } 
        
    }
}
