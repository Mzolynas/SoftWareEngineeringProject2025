import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainPage {
    private static DefaultTableModel tableModel;
    private static JFrame mainFrame;
    private static JTable table;

    // Main window for stock management
    public static void createMainPage() {
        mainFrame = new JFrame("Inventory Management System");
        mainFrame.setSize(800, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Top title
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Current Stock Levels", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Top buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add New Item");
        JButton updateButton = new JButton("Update Selected Item");
        JButton alertButton = new JButton("Check Low Stock");
        JButton reorderButton = new JButton("Generate Reorder");
        JButton viewPOButton = new JButton("View Purchase Orders");

        addButton.addActionListener(e -> openAddItemDialog());
        updateButton.addActionListener(e -> openUpdateItemDialog());
        alertButton.addActionListener(e -> AlertService.showLowStockAlert(mainFrame));
        reorderButton.addActionListener(e -> generateReorderFromLowStock());
        viewPOButton.addActionListener(e -> openPurchaseOrdersDialog());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(alertButton);
        buttonPanel.add(reorderButton);
        buttonPanel.add(viewPOButton);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        mainFrame.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Model", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        refreshInventoryTable();

        AlertService.showLowStockAlert(mainFrame);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void refreshInventoryTable() {
        tableModel.setRowCount(0);
        List<Shoe> shoes = InventoryService.getAllShoes();
        for (Shoe shoe : shoes) {
            Object[] row = {shoe.getModel(), shoe.getQuantity(), shoe.getPrice()};
            tableModel.addRow(row);
        }
    }

    private static void openAddItemDialog() {
        JDialog addDialog = new JDialog(mainFrame, "Add New Shoe", true);
        addDialog.setSize(350, 200);
        addDialog.setLayout(new GridLayout(4, 2));
        addDialog.setLocationRelativeTo(mainFrame);

        JTextField modelField = new JTextField(15);
        JTextField quantityField = new JTextField(15);
        JTextField priceField = new JTextField(15);

        addDialog.add(new JLabel("  Model:"));
        addDialog.add(modelField);
        addDialog.add(new JLabel("  Quantity:"));
        addDialog.add(quantityField);
        addDialog.add(new JLabel("  Price:"));
        addDialog.add(priceField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String model = modelField.getText().trim();
                    int quantity = Integer.parseInt(quantityField.getText().trim());
                    double price = Double.parseDouble(priceField.getText().trim());

                    if (model.isEmpty() || quantity <= 0 || price <= 0) {
                        JOptionPane.showMessageDialog(
                                addDialog,
                                "All fields must be valid!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    InventoryService.addItem(new Shoe(model, quantity, price));
                    refreshInventoryTable();
                    JOptionPane.showMessageDialog(
                            addDialog,
                            "Item added successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    addDialog.dispose();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            addDialog,
                            "Quantity and price must be valid numbers!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        addDialog.add(saveButton);
        addDialog.setVisible(true);
    }

    private static void openUpdateItemDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Please select one row to update.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Shoe selectedShoe = InventoryService.getAllShoes().get(selectedRow);

        JDialog updateDialog = new JDialog(mainFrame, "Update Shoe Item", true);
        updateDialog.setSize(350, 200);
        updateDialog.setLayout(new GridLayout(4, 2));
        updateDialog.setLocationRelativeTo(mainFrame);

        JTextField modelField = new JTextField(selectedShoe.getModel(), 15);
        JTextField quantityField = new JTextField(String.valueOf(selectedShoe.getQuantity()), 15);
        JTextField priceField = new JTextField(String.valueOf(selectedShoe.getPrice()), 15);

        updateDialog.add(new JLabel("  Model:"));
        updateDialog.add(modelField);
        updateDialog.add(new JLabel("  Quantity:"));
        updateDialog.add(quantityField);
        updateDialog.add(new JLabel("  Price:"));
        updateDialog.add(priceField);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String model = modelField.getText().trim();
                    int quantity = Integer.parseInt(quantityField.getText().trim());
                    double price = Double.parseDouble(priceField.getText().trim());

                    if (model.isEmpty() || quantity <= 0 || price <= 0) {
                        JOptionPane.showMessageDialog(
                                updateDialog,
                                "All fields must be valid!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    InventoryService.updateItem(selectedRow, model, quantity, price);
                    refreshInventoryTable();

                    AlertService.showLowStockAlert(mainFrame);

                    JOptionPane.showMessageDialog(
                            updateDialog,
                            "Item updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    updateDialog.dispose();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            updateDialog,
                            "Quantity and price must be valid numbers!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        updateDialog.add(saveButton);
        updateDialog.setVisible(true);
    }

    // Generate purchase orders from low-stock items
    private static void generateReorderFromLowStock() {
        List<Shoe> allShoes = InventoryService.getAllShoes();
        List<Shoe> lowStockItems = AlertService.getLowStockItems(allShoes);

        if (lowStockItems.isEmpty()) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "No low stock items. No reorder needed.",
                    "Generate Reorder",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // This will insert records into purchase_order table (MySQL)
        List<PurchaseOrder> newOrders = PurchaseOrderService.createOrdersForLowStock(lowStockItems);

        StringBuilder message = new StringBuilder();
        message.append("New purchase orders created:\n\n");
        for (PurchaseOrder po : newOrders) {
            message.append("PO ")
                   .append(po.getId())
                   .append(" - ")
                   .append(po.getModel())
                   .append(" (Qty: ")
                   .append(po.getQuantity())
                   .append(")\n");
        }

        JOptionPane.showMessageDialog(
                mainFrame,
                message.toString(),
                "Generate Reorder",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // EP4-S3: view and update purchase order status (reads/writes purchase_order table)
    private static void openPurchaseOrdersDialog() {
        List<PurchaseOrder> orders = PurchaseOrderService.getAllOrders();

        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "No purchase orders have been created yet.",
                    "Purchase Orders",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JDialog poDialog = new JDialog(mainFrame, "Purchase Orders", true);
        poDialog.setSize(500, 350);
        poDialog.setLayout(new BorderLayout());
        poDialog.setLocationRelativeTo(mainFrame);

        String[] columnNames = {"PO ID", "Model", "Quantity", "Status"};
        DefaultTableModel poTableModel = new DefaultTableModel(columnNames, 0);
        JTable poTable = new JTable(poTableModel);

        for (PurchaseOrder po : orders) {
            Object[] row = {
                    po.getId(),
                    po.getModel(),
                    po.getQuantity(),
                    po.getStatus()
            };
            poTableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(poTable);
        poDialog.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel: select new status and update
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel statusLabel = new JLabel("New Status:");
        String[] statuses = {"Pending", "Ordered", "Received", "Cancelled"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);
        JButton updateStatusButton = new JButton("Update Status");

        updateStatusButton.addActionListener(e -> {
            int selectedRow = poTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        poDialog,
                        "Please select one purchase order to update.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String newStatus = (String) statusBox.getSelectedItem();

            PurchaseOrder selectedOrder = orders.get(selectedRow);
            selectedOrder.setStatus(newStatus);

            PurchaseOrderService.updateOrderStatus(selectedOrder.getId(), newStatus);

            poTableModel.setValueAt(newStatus, selectedRow, 3);

            JOptionPane.showMessageDialog(
                    poDialog,
                    "Purchase order status updated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        bottomPanel.add(statusLabel);
        bottomPanel.add(statusBox);
        bottomPanel.add(updateStatusButton);

        poDialog.add(bottomPanel, BorderLayout.SOUTH);
        poDialog.setVisible(true);
    }
}
