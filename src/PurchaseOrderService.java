import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PurchaseOrderService {

    // Create purchase orders for each low-stock item
    public static List<PurchaseOrder> createOrdersForLowStock(List<shoes> lowStockItems) {
        List<PurchaseOrder> newOrders = new ArrayList<>();
        String sql = "INSERT INTO purchase_order (model, quantity, status) VALUES (?, ?, ?)";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (shoes shoe : lowStockItems) {
                int quantityToOrder = 10; // default amount

                ps.setString(1, shoe.getName());
                ps.setInt(2, quantityToOrder);
                ps.setString(3, "Pending");
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        // Use your constructor
                        PurchaseOrder po = new PurchaseOrder(id, shoe.getName(), quantityToOrder, "Pending");
                        newOrders.add(po);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating purchase orders: " + e.getMessage());
        }

        return newOrders;
    }

    // Get all purchase orders
    public static List<PurchaseOrder> getAllOrders() {
        List<PurchaseOrder> orders = new ArrayList<>();
        String sql = "SELECT po_id, model, quantity, status FROM purchase_order ORDER BY po_id";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("po_id");
                String model = rs.getString("model");
                int qty = rs.getInt("quantity");
                String status = rs.getString("status");

                orders.add(new PurchaseOrder(id, model, qty, status));
            }

        } catch (SQLException e) {
            System.err.println("Error loading purchase orders: " + e.getMessage());
        }

        return orders;
    }

    // Get an order by ID
    public static PurchaseOrder getOrderById(int id) {
        String sql = "SELECT * FROM purchase_order WHERE po_id = ?";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new PurchaseOrder(
                    rs.getInt("po_id"),
                    rs.getString("model"),
                    rs.getInt("quantity"),
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error getting order: " + e.getMessage());
        }

        return null;
    }

    // Update only the status
    public static boolean updateOrderStatus(int id, String newStatus) {
        String sql = "UPDATE purchase_order SET status = ? WHERE po_id = ?";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
        }

        return false;
    }

    // Mark order as completed
    public static boolean markOrderCompleted(int id) {
        return updateOrderStatus(id, "Completed");
    }

    // Delete an order
    public static boolean deleteOrder(int id) {
        String sql = "DELETE FROM purchase_order WHERE po_id = ?";

        try (Connection conn = EnhancedShoeDataBase.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
        }

        return false;
    }

    // Get low stock shoes (unchanged)
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
            System.err.println("Error loading low stock items: " + e.getMessage());
        }

        return lowStockItems;
    }

    // Show popup for low-stock items
    public static void showLowStockAlert(JFrame parent) {
        List<shoes> lowStockItems = getLowStockItems();

        if (!lowStockItems.isEmpty()) {
            StringBuilder msg = new StringBuilder("LOW STOCK ALERT!\n\n");

            for (shoes shoe : lowStockItems) {
                msg.append("• ").append(shoe.getName())
                   .append(" (Qty: ").append(shoe.getQuantity()).append(")\n");
            }

            JOptionPane.showMessageDialog(parent, msg.toString(), "Low Stock", JOptionPane.WARNING_MESSAGE);
        }
    }
}
