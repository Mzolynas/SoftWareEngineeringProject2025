import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class EmployeePage {

    private static JTable shoesTable;
    private static DefaultTableModel tableModel;
    private static JFrame frame;
    private static JLabel statsLabel;

    public static void createEmployeePage() {
        frame = new JFrame("Employee – Shoe Inventory System");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Employee Inventory View", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        statsLabel = new JLabel("Loading inventory...", SwingConstants.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setForeground(Color.GRAY);
        titlePanel.add(statsLabel, BorderLayout.SOUTH);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // --- Search & Filters (UNCHANGED) ---
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filters"));
        filterPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Search:"), gbc);

        gbc.gridx = 1;
        JTextField searchField = new JTextField(20);
        filterPanel.add(searchField, gbc);

        gbc.gridx = 2;
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        filterPanel.add(searchButton, gbc);

        gbc.gridx = 3;
        JButton clearSearchButton = new JButton("Clear");
        clearSearchButton.setBackground(new Color(169, 169, 169));
        clearSearchButton.setForeground(Color.WHITE);
        filterPanel.add(clearSearchButton, gbc);

        // Filters
        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(new JLabel("Brand:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> brandFilter = new JComboBox<>();
        brandFilter.addItem("All Brands");
        for (String brand : EnhancedShoeDataBase.getAllBrands()) brandFilter.addItem(brand);
        filterPanel.add(brandFilter, gbc);

        gbc.gridx = 2;
        filterPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All Categories");
        for (String c : EnhancedShoeDataBase.getAllCategories()) categoryFilter.addItem(c);
        filterPanel.add(categoryFilter, gbc);

        gbc.gridx = 4;
        JButton filterButton = new JButton("Apply Filters");
        filterButton.setBackground(new Color(70, 130, 180));
        filterButton.setForeground(Color.WHITE);
        filterPanel.add(filterButton, gbc);

        // Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        filterPanel.add(new JLabel("Price Range:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> priceFilter = new JComboBox<>(new String[]{
                "All Prices", "Under $50", "$50 - $100", "$100 - $150", "$150 - $200", "Over $200"
        });
        filterPanel.add(priceFilter, gbc);

        gbc.gridx = 2;
        filterPanel.add(new JLabel("Sort By:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> sortFilter = new JComboBox<>(new String[]{
                "Default", "Price: Low to High", "Price: High to Low", "Name A-Z", "Brand A-Z"
        });
        filterPanel.add(sortFilter, gbc);

        gbc.gridx = 4;
        JButton resetAllButton = new JButton("Reset All");
        resetAllButton.setBackground(new Color(169, 169, 169));
        resetAllButton.setForeground(Color.WHITE);
        filterPanel.add(resetAllButton, gbc);

        mainPanel.add(filterPanel, BorderLayout.CENTER);

        // --- TABLE (UNCHANGED) ---
        String[] columnNames = {"ID", "Name", "Brand", "Size", "Color", "Price", "Quantity", "Category", "Description", "Date Added"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {  // ID column
                    return Integer.class;
                }
                return Object.class;
            }
        };

        shoesTable = new JTable(tableModel);
        shoesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shoesTable.getTableHeader().setReorderingAllowed(false);
        shoesTable.setAutoCreateRowSorter(true);

        // Highlight low quantity
        shoesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(Color.WHITE);
                if (column == 6) {
                    try {
                        int qty = Integer.parseInt(value.toString());
                        if (qty < 5) c.setBackground(new Color(255, 200, 200));
                        else if (qty < 10) c.setBackground(new Color(255, 255, 200));
                    } catch (Exception ignored) {}
                }
                if (isSelected) c.setBackground(new Color(200, 220, 255));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(shoesTable);
        scrollPane.setPreferredSize(new Dimension(1100, 400));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Shoe Inventory"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ----------- EMPLOYEE BUTTONS (NO ADD/EDIT/DELETE) ----------
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        Color buttonColor = new Color(70, 130, 180);

        JButton refreshBtn = createBtn("Refresh", buttonColor);
        JButton viewDetailsBtn = createBtn("View Details", buttonColor);
        JButton statsBtn = createBtn("Inventory Stats", buttonColor);
        JButton stockMonitorBtn = createBtn("Stock Monitor", buttonColor);
        JButton logoutBtn = createBtn("Logout", buttonColor);

        // Only employee-safe buttons:
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(statsBtn);
        buttonPanel.add(stockMonitorBtn);
        buttonPanel.add(logoutBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        loadShoes();

        // --- LISTENERS ---
        refreshBtn.addActionListener(e -> loadShoes());
        searchButton.addActionListener(e -> searchShoes(searchField.getText().trim()));
        clearSearchButton.addActionListener(e -> { searchField.setText(""); loadShoes(); });
        filterButton.addActionListener(e -> applyFilters(
                brandFilter.getSelectedItem().toString(),
                categoryFilter.getSelectedItem().toString(),
                priceFilter.getSelectedItem().toString()
        ));
        resetAllButton.addActionListener(e -> {
            brandFilter.setSelectedIndex(0);
            categoryFilter.setSelectedIndex(0);
            priceFilter.setSelectedIndex(0);
            sortFilter.setSelectedIndex(0);
            searchField.setText("");
            loadShoes();
        });
        sortFilter.addActionListener(e -> sortTable(sortFilter.getSelectedItem().toString()));
        viewDetailsBtn.addActionListener(e -> showDetails());
        statsBtn.addActionListener(e -> showInventoryStats());
        stockMonitorBtn.addActionListener(e -> openStockMonitorPage());
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            loginPage.createLoginPage();
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JButton createBtn(String txt, Color c) {
        JButton b = new JButton(txt);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    private static void loadShoes() {
        tableModel.setRowCount(0);
        for (shoes s : EnhancedShoeDataBase.getAllShoes())
            tableModel.addRow(s.toTableRow());
        updateStats();
    }

    private static void updateStats() {
        int total = tableModel.getRowCount();
        int qty = 0;
        double value = 0;

        for (int i = 0; i < total; i++) {
            qty += Integer.parseInt(tableModel.getValueAt(i, 6).toString());
            value += Double.parseDouble(tableModel.getValueAt(i, 5).toString().replace("$", "")) *
                    Integer.parseInt(tableModel.getValueAt(i, 6).toString());
        }

        statsLabel.setText(String.format("Models: %d | Quantity: %d | Value: $%.2f", total, qty, value));
    }

    private static void searchShoes(String key) {
        if (key.isEmpty()) return;
        tableModel.setRowCount(0);
        List<shoes> list = EnhancedShoeDataBase.searchShoes(key);
        for (shoes s : list) tableModel.addRow(s.toTableRow());
        updateStats();
    }

    private static void applyFilters(String brand, String category, String priceRange) {
        tableModel.setRowCount(0);
        for (shoes s : EnhancedShoeDataBase.getAllShoes()) {
            boolean ok = true;

            if (!brand.equals("All Brands") && !s.getBrand().equals(brand)) ok = false;
            if (!category.equals("All Categories") && !s.getCategory().equals(category)) ok = false;

            if (!priceRange.equals("All Prices")) {
                double p = s.getPrice();
                switch (priceRange) {
                    case "Under $50": if (p >= 50) ok = false; break;
                    case "$50 - $100": if (p < 50 || p > 100) ok = false; break;
                    case "$100 - $150": if (p < 100 || p > 150) ok = false; break;
                    case "$150 - $200": if (p < 150 || p > 200) ok = false; break;
                    case "Over $200": if (p <= 200) ok = false; break;
                }
            }

            if (ok) tableModel.addRow(s.toTableRow());
        }
        updateStats();
    }

    private static void sortTable(String opt) {
        switch (opt) {
            case "Price: Low to High": shoesTable.getRowSorter().toggleSortOrder(5); break;
            case "Price: High to Low":
                shoesTable.getRowSorter().toggleSortOrder(5);
                shoesTable.getRowSorter().toggleSortOrder(5);
                break;
            case "Name A-Z": shoesTable.getRowSorter().toggleSortOrder(1); break;
            case "Brand A-Z": shoesTable.getRowSorter().toggleSortOrder(2); break;
            default: loadShoes();
        }
    }

    private static void showDetails() {
        int row = shoesTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Select a shoe first");
            return;
        }
        row = shoesTable.convertRowIndexToModel(row);

        StringBuilder info = new StringBuilder("=== Shoe Details ===\n\n");
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            info.append(tableModel.getColumnName(i)).append(": ").append(tableModel.getValueAt(row, i)).append("\n");
        }

        JOptionPane.showMessageDialog(frame, new JScrollPane(new JTextArea(info.toString())));
    }

    private static void showInventoryStats() {
        JOptionPane.showMessageDialog(frame,
                "Employee Inventory Stats available.\n(Admin-only stock editing is disabled.)");
    }

    private static void openStockMonitorPage() {
        // Reuse your existing stock monitor page
        MainPage main = new MainPage();
        // Call your existing method safely:
        try {
            java.lang.reflect.Method m = MainPage.class.getDeclaredMethod("openStockMonitorPage");
            m.setAccessible(true);
            m.invoke(null);
        } catch (Exception ignored) {}
    }
}
