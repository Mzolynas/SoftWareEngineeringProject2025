import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderService {

    // EP3-S2: create new purchase orders for each low-stock item
    // and save them into the database (purchase_order table)
    public static List<PurchaseOrder> createOrdersForLowStock(List<Shoe> lowStockItems) {
        List<PurchaseOrder> newOrders = new ArrayList<>();

        String sql = "INSERT INTO purchase_order (model, quantity, status) VALUES (?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (Shoe shoe : lowStockItems) {
                int quantityToOrder = 10;

                ps.setString(1, shoe.getModel());
                ps.setInt(2, quantityToOrder);
                ps.setString(3, "Pending");
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        PurchaseOrder po =
                                new PurchaseOrder(id, shoe.getModel(), quantityToOrder, "Pending");
                        newOrders.add(po);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newOrders;
    }

    // EP4-S3: load all purchase orders from database
    public static List<PurchaseOrder> getAllOrders() {
        List<PurchaseOrder> orders = new ArrayList<>();
        String sql = "SELECT id, model, quantity, status FROM purchase_order ORDER BY id";

        try (Connection conn = Db.getConnection();
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
            e.printStackTrace();
        }

        return orders;
    }

    // EP4-S3: update status for a specific purchase order
    public static void updateOrderStatus(int id, String newStatus) {
        String sql = "UPDATE purchase_order SET status = ? WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
        