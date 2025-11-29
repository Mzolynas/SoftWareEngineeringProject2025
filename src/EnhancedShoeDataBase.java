import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EnhancedShoeDataBase {
    private static final String BASE_URL = "jdbc:mysql://localhost:3307/";
    private static final String DB_NAME = "shoe_inventory_db";
    private static final String DB_URL = BASE_URL + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // First try to connect to the specific database
            System.out.println("🔗 Attempting to connect to: " + DB_URL);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            // If database doesn't exist, create it
            if (e.getMessage().contains("Unknown database")) {
                System.out.println("📦 Database doesn't exist. Creating it now...");
                createDatabaseAndTables();
                // Try connecting again
                return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            throw e;
        }
    }

    private static void createDatabaseAndTables() {
        try {
            // Connect without specifying database
            Connection conn = DriverManager.getConnection(BASE_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
            // Create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("✅ Database '" + DB_NAME + "' created successfully!");
            
            // Switch to the new database
            stmt.executeUpdate("USE " + DB_NAME);
            
            // Create table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS shoes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "brand VARCHAR(100) NOT NULL, " +
                    "size DECIMAL(3,1) NOT NULL, " +
                    "color VARCHAR(50) NOT NULL, " +
                    "price DECIMAL(10,2) NOT NULL, " +
                    "quantity INT NOT NULL, " +
                    "category VARCHAR(100), " +
                    "description TEXT, " +
                    "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            stmt.executeUpdate(createTableSQL);
            System.out.println("✅ Table 'shoes' created successfully!");
            
            // Insert sample data
            insertSampleData(stmt);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to create database: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Failed to create database!\n\nError: " + e.getMessage(),
                "Database Creation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void insertSampleData(Statement stmt) throws SQLException {
        // Check if data already exists
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM shoes");
        if (rs.next() && rs.getInt("count") == 0) {
            System.out.println("📥 Inserting 20 sample shoes...");
            
            String insertSQL = "INSERT INTO shoes (name, brand, size, color, price, quantity, category, description) VALUES " +
                "('Air Max 270', 'Nike', 9.0, 'Black/White', 150.00, 15, 'Lifestyle', 'Comfortable lifestyle shoes with Max Air cushioning')," +
                "('Ultraboost 22', 'Adidas', 10.0, 'Core Black', 180.00, 12, 'Running', 'High-performance running shoes with Boost technology')," +
                "('Fresh Foam 1080v12', 'New Balance', 9.5, 'Grey/Blue', 150.00, 8, 'Running', 'Premium running shoes with Fresh Foam cushioning')," +
                "('RS-X Reinvention', 'Puma', 10.5, 'White/Red', 120.00, 20, 'Lifestyle', 'Retro-inspired lifestyle sneakers')," +
                "('Classic Leather', 'Reebok', 9.0, 'White/Navy', 85.00, 25, 'Lifestyle', 'Timeless classic leather sneakers')," +
                "('Chuck Taylor All Star', 'Converse', 8.5, 'Black', 55.00, 30, 'Lifestyle', 'Iconic canvas sneakers')," +
                "('Old Skool', 'Vans', 9.0, 'Black/White', 60.00, 18, 'Skateboarding', 'Classic skate shoes with durable construction')," +
                "('Gel-Kayano 29', 'ASICS', 10.0, 'Blue/Silver', 160.00, 10, 'Running', 'Stability running shoes with Gel cushioning')," +
                "('Air Force 1', 'Nike', 9.5, 'White', 100.00, 22, 'Lifestyle', 'Classic basketball-inspired lifestyle shoes')," +
                "('NMD_R1', 'Adidas', 10.5, 'Core Black', 140.00, 14, 'Lifestyle', 'Modern lifestyle shoes with Boost cushioning')," +
                "('990v6', 'New Balance', 9.0, 'Grey', 195.00, 6, 'Lifestyle', 'Premium made in USA lifestyle sneakers')," +
                "('Cali Star', 'Puma', 8.5, 'Pink/White', 110.00, 16, 'Lifestyle', 'Women''s fashion sneakers with platform sole')," +
                "('Club C 85', 'Reebok', 9.5, 'White/Green', 90.00, 12, 'Lifestyle', 'Vintage tennis-inspired sneakers')," +
                "('Run Star Hike', 'Converse', 8.0, 'Black/White', 130.00, 9, 'Lifestyle', 'Platform sneakers with rugged outsole')," +
                "('Sk8-Hi', 'Vans', 10.0, 'Checkerboard', 75.00, 15, 'Skateboarding', 'High-top skate shoes with iconic pattern')," +
                "('Gel-Nimbus 25', 'ASICS', 10.5, 'Purple/Orange', 160.00, 11, 'Running', 'Neutral running shoes with maximum cushioning')," +
                "('Pegasus 39', 'Nike', 9.5, 'Blue/Orange', 120.00, 17, 'Running', 'Versatile daily training running shoes')," +
                "('Superstar', 'Adidas', 9.0, 'White/Black', 100.00, 20, 'Lifestyle', 'Iconic shell-toe sneakers')," +
                "('574 Core', 'New Balance', 8.5, 'Navy/Red', 85.00, 24, 'Lifestyle', 'Classic heritage running shoes')," +
                "('Basket Classic', 'Puma', 9.0, 'Black/Gold', 95.00, 13, 'Lifestyle', 'Retro basketball-inspired sneakers')";
            
            stmt.executeUpdate(insertSQL);
            System.out.println("✅ 20 sample shoes inserted successfully!");
        } else {
            System.out.println("📊 Database already contains data.");
        }
    }

    public static void initializeDatabase() {
        System.out.println("🔄 Initializing shoe_inventory_db...");
        try {
            Connection conn = connect();
            System.out.println("🎉 SUCCESS: Connected to shoe_inventory_db!");
            
            // Verify data
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM shoes");
            if (rs.next()) {
                System.out.println("📊 Total shoes in database: " + rs.getInt("total"));
            }
            
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Failed to initialize database: " + e.getMessage());
        }
    }

    // GET ALL SHOES
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

            System.out.println("📥 Fetched " + shoesList.size() + " shoes from shoe_inventory_db");

        } catch (SQLException e) {
            System.err.println("Error fetching shoes: " + e.getMessage());
        }
        return shoesList;
    }

    // GET ALL BRANDS
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

    // GET ALL CATEGORIES
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

    // ADD SHOE
    public static boolean addShoe(shoes shoe) {
        String sql = "INSERT INTO shoes (name, brand, size, color, price, quantity, category, description, date_added) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

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

    // UPDATE SHOE
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

    // DELETE SHOE
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

    // GET SHOE BY ID
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

    // SEARCH SHOES
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
}