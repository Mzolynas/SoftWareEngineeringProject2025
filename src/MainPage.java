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

    // main menu
    public static void createMainPage() {
        mainFrame = new JFrame("Inventory Management System");
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // top title
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Current Stock Levels", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add New Item");
        JButton updateButton = new JButton("Update Selected Item");

        // add new item
        addButton.addActionListener(e -> openAddItemDialog());
        // update
        updateButton.addActionListener(e -> openUpdateItemDialog());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        mainFrame.add(topPanel, BorderLayout.NORTH);

        // table setup
        String[] columnNames = {"Model", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        refreshInventoryTable();

        mainFrame.setVisible(true);
    }
    //refresh table data
    public static void refreshInventoryTable() {
        tableModel.setRowCount(0); 
        List<Shoe> shoes = InventoryService.getAllShoes();
        for (Shoe shoe : shoes) {
            Object[] row = {shoe.getModel(), shoe.getQuantity(), shoe.getPrice()};
            tableModel.addRow(row);
        }
    }

    //add new item
    private static void openAddItemDialog() {
        JDialog addDialog = new JDialog(mainFrame, "Add New Shoe", true);
        addDialog.setSize(350, 200);
        addDialog.setLayout(new GridLayout(4, 2));

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
                        JOptionPane.showMessageDialog(addDialog,
                                "All fields must be valid!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    InventoryService.addItem(new Shoe(model, quantity, price));
                    refreshInventoryTable();
                    JOptionPane.showMessageDialog(addDialog,
                            "Item added successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    addDialog.dispose();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addDialog,
                            "Quantity and price must be valid numbers!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addDialog.add(saveButton);
        addDialog.setVisible(true);
    }

    //update item
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

        //pop up update dialog
        JDialog updateDialog = new JDialog(mainFrame, "Update Shoe Item", true);
        updateDialog.setSize(350, 200);
        updateDialog.setLayout(new GridLayout(4, 2));

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
}