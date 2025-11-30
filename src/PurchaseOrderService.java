import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PurchaseOrderService {

    // Create new purchase orders for each low-stock item
    public static List<PurchaseOrder> createOrdersForLowStock(List<shoes> lowStockItems) {
        List<PurchaseOrder> newOrders = new ArrayList<>();

        String sql = "INSERT INTO purchase_order (model, quantity, status) VALUES (?, ?, ?)";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (shoes shoe : lowStockItems) {
                int quantityToOrder = 10; // Default reorder quantity

                ps.setString(1, shoe.getName());
                ps.setInt(2, quantityToOrder);
                ps.setString(3, "Pending");
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        PurchaseOrder po = new PurchaseOrder(id, shoe.getName(), quantityToOrder, "Pending");
                        newOrders.add(po);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating purchase orders: " + e.getMessage());
            e.printStackTrace();
        }

        return newOrders;
    }

    // Load all purchase orders from database
    public static List<PurchaseOrder> getAllOrders() {
        List<PurchaseOrder> orders = new ArrayList<>();
        String sql = "SELECT id, model, quantity, status FROM purchase_order ORDER BY id";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");

                orders.add(new PurchaseOrder(id, model, quantity, status));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching purchase orders: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    // Update status for a specific purchase order
    public static boolean updateOrderStatus(int id, String newStatus) {
        String sql = "UPDATE purchase_order SET status = ? WHERE id = ?";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating purchase order status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get low stock items (quantity < 5)
    public static List<shoes> getLowStockItems() {
        List<shoes> lowStockItems = new ArrayList<>();
        String sql = "SELECT * FROM shoes WHERE quantity < 5";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
                lowStockItems.add(shoe);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching low stock items: " + e.getMessage());
            e.printStackTrace();
        }

        return lowStockItems;
    }

    // Check for low stock and show alert
    public static void showLowStockAlert(JFrame parent) {
        List<shoes> lowStockItems = getLowStockItems();
        
        if (!lowStockItems.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("LOW STOCK ALERT!\n\n");
            message.append("The following items are running low:\n");
            
            for (shoes shoe : lowStockItems) {
                message.append("• ").append(shoe.getName())
                       .append(" - ").append(shoe.getBrand())
                       .append(" (Qty: ").append(shoe.getQuantity()).append(")\n");
            }
            
            message.append("\nConsider generating purchase orders for these items.");
            
            JOptionPane.showMessageDialog(parent, 
                message.toString(), 
                "Low Stock Alert", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}