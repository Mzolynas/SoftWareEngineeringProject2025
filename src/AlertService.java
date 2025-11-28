import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AlertService {

    // threshold for low stock
    private static final int LOW_STOCK_THRESHOLD = 5;

    // find items with quantity below threshold
    public static List<Shoe> getLowStockItems(List<Shoe> allShoes) {
        List<Shoe> lowStock = new ArrayList<>();
        for (Shoe shoe : allShoes) {
            if (shoe.getQuantity() < LOW_STOCK_THRESHOLD) {
                lowStock.add(shoe);
            }
        }
        return lowStock;
    }

    // show alert dialog for low stock items
    public static void showLowStockAlert(JFrame parentFrame) {
        List<Shoe> allShoes = InventoryService.getAllShoes();
        List<Shoe> lowStockItems = getLowStockItems(allShoes);

        if (lowStockItems.isEmpty()) {
            // if you don't want this message every time, you can remove this block
            // JOptionPane.showMessageDialog(
            //         parentFrame,
            //         "No low stock items. All good!",
            //         "Low Stock Alert",
            //         JOptionPane.INFORMATION_MESSAGE
            // );
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("Low stock items (quantity < ")
               .append(LOW_STOCK_THRESHOLD)
               .append("):\n\n");

        for (Shoe shoe : lowStockItems) {
            message.append("- ")
                   .append(shoe.getModel())
                   .append(" (Qty: ")
                   .append(shoe.getQuantity())
                   .append(")\n");
        }

        JOptionPane.showMessageDialog(
                parentFrame,
                message.toString(),
                "Low Stock Alert",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
