import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {
    public static List<Shoe> getAllShoes() {
        List<Shoe> shoes = new ArrayList<>();
        String sql = "SELECT id, model, quantity, price FROM shoe ORDER BY id";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                shoes.add(new Shoe(id, model, quantity, price));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shoes;
    }

    // EP2-S3: add new shoe to database
    public static void addItem(Shoe shoe) {
        String sql = "INSERT INTO shoe (model, quantity, price) VALUES (?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, shoe.getModel());
            ps.setInt(2, shoe.getQuantity());
            ps.setDouble(3, shoe.getPrice());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    shoe.setId(id);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // EP2-S2: update selected shoe by index (row in table)
    public static void updateItem(int index, String model, int quantity, double price) {
        List<Shoe> shoes = getAllShoes();
        if (index < 0 || index >= shoes.size()) {
            return;
        }
        Shoe existing = shoes.get(index);
        int id = existing.getId();

        String sql = "UPDATE shoe SET model = ?, quantity = ?, price = ? WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, model);
            ps.setInt(2, quantity);
            ps.setDouble(3, price);
            ps.setInt(4, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
