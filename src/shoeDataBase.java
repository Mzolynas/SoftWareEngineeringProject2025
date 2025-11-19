import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class shoeDataBase {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/shoe_inventory_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static void initializeDatabase() {
        // Just print message
        System.out.println("Database ready");
    }
    
    public static List<shoes> getAllShoes() {
        List<shoes> shoesList = new ArrayList<>();
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM shoes");
            
            while (rs.next()) {
                shoes shoe = new shoes(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getDouble("size"),
                    rs.getString("color"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getString("date_added")
                );
                shoesList.add(shoe);
            }
            
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shoesList;
    }
}