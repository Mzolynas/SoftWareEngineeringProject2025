import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class shoeDataBase {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/shoe_inventory_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public static Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    public static void initializeDatabase() {
        try (Connection conn = connect()) {
            System.out.println("Database connection established successfully!");
            
            // Check total shoes count
            String countSql = "SELECT COUNT(*) as total FROM shoes";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(countSql)) {
                if (rs.next()) {
                    int totalShoes = rs.getInt("total");
                    System.out.println("Total shoes in inventory: " + totalShoes);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Cannot connect to database. Please ensure MySQL is running on port 3307.", 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static List<shoes> getAllShoes() {
        List<shoes> shoesList = new ArrayList<>();
        String sql = "SELECT * FROM shoes ORDER BY brand, name";
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
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
        } catch (SQLException e) {
            System.err.println("Error fetching shoes: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error loading shoes from database", 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        return shoesList;
    }
    
    public static List<shoes> searchShoes(String keyword) {
        List<shoes> shoesList = new ArrayList<>();
        String sql = "SELECT * FROM shoes WHERE name LIKE ? OR brand LIKE ? OR category LIKE ? OR color LIKE ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
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
        } catch (SQLException e) {
            System.err.println("Error searching shoes: " + e.getMessage());
        }
        return shoesList;
    }
    
    // New method to get shoes by brand
    public static List<shoes> getShoesByBrand(String brand) {
        List<shoes> shoesList = new ArrayList<>();
        String sql = "SELECT * FROM shoes WHERE brand = ? ORDER BY name";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, brand);
            ResultSet rs = pstmt.executeQuery();
            
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
        } catch (SQLException e) {
            System.err.println("Error fetching shoes by brand: " + e.getMessage());
        }
        return shoesList;
    }
    
    // New method to get shoes by category
    public static List<shoes> getShoesByCategory(String category) {
        List<shoes> shoesList = new ArrayList<>();
        String sql = "SELECT * FROM shoes WHERE category = ? ORDER BY brand, name";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
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
        } catch (SQLException e) {
            System.err.println("Error fetching shoes by category: " + e.getMessage());
        }
        return shoesList;
    }
    
    // New method to get all unique brands
    public static List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT DISTINCT brand FROM shoes ORDER BY brand";
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                brands.add(rs.getString("brand"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching brands: " + e.getMessage());
        }
        return brands;
    }
    
    // New method to get all unique categories
    public static List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM shoes WHERE category IS NOT NULL ORDER BY category";
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
        }
        return categories;
    }
    
    
    
    // New method to get inventory statistics
    public static void showInventoryStats() {
        String sql = "SELECT " +
                     "COUNT(*) as total_shoes, " +
                     "SUM(quantity) as total_quantity, " +
                     "AVG(price) as avg_price, " +
                     "MAX(price) as max_price, " +
                     "MIN(price) as min_price " +
                     "FROM shoes";
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                System.out.println("=== INVENTORY STATISTICS ===");
                System.out.println("Total shoe models: " + rs.getInt("total_shoes"));
                System.out.println("Total quantity: " + rs.getInt("total_quantity"));
                System.out.println("Average price: $" + String.format("%.2f", rs.getDouble("avg_price")));
                System.out.println("Most expensive: $" + String.format("%.2f", rs.getDouble("max_price")));
                System.out.println("Least expensive: $" + String.format("%.2f", rs.getDouble("min_price")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory stats: " + e.getMessage());
        }
    }
}