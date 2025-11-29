import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EnhancedShoeDataBase {
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
    
    // ADD SHOE - Complete implementation
    public static boolean addShoe(shoes shoe) {
        String sql = "INSERT INTO shoes (name, brand, size, color, price, quantity, category, description, date_added) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURDATE())";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, shoe.getName());
            pstmt.setString(2, shoe.getBrand());
            pstmt.setDouble(3, shoe.getSize());
            pstmt.setString(4, shoe.getColor());
            pstmt.setDouble(5, shoe.getPrice());
            pstmt.setInt(6, shoe.getQuantity());
            pstmt.setString(7, shoe.getCategory());
            pstmt.setString(8, shoe.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding shoe: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error adding shoe to database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // UPDATE SHOE - Complete implementation
    public static boolean updateShoe(shoes shoe) {
        String sql = "UPDATE shoes SET name=?, brand=?, size=?, color=?, price=?, quantity=?, category=?, description=? WHERE id=?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, shoe.getName());
            pstmt.setString(2, shoe.getBrand());
            pstmt.setDouble(3, shoe.getSize());
            pstmt.setString(4, shoe.getColor());
            pstmt.setDouble(5, shoe.getPrice());
            pstmt.setInt(6, shoe.getQuantity());
            pstmt.setString(7, shoe.getCategory());
            pstmt.setString(8, shoe.getDescription());
            pstmt.setInt(9, shoe.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating shoe: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error updating shoe in database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // DELETE SHOE - Complete implementation
    public static boolean deleteShoe(int shoeId) {
        String sql = "DELETE FROM shoes WHERE id = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, shoeId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting shoe: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error deleting shoe from database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // GET SHOE BY ID - For editing
    public static shoes getShoeById(int shoeId) {
        String sql = "SELECT * FROM shoes WHERE id = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, shoeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new shoes(
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
            }
        } catch (SQLException e) {
            System.err.println("Error fetching shoe by ID: " + e.getMessage());
        }
        return null;
    }
    
    // EXISTING METHODS (keep these from your current version)
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

	public static void initializeDatabase() {
		// TODO Auto-generated method stub
		
	}
}