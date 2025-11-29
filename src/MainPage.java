import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainPage {
    private static JTable shoesTable;
    private static DefaultTableModel tableModel;
    private static JFrame frame;
    private static JLabel statsLabel;

    public static void createMainPage() {
        frame = new JFrame("Shoe Inventory System");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Shoe Inventory Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        // Stats label
        statsLabel = new JLabel("Loading inventory...", SwingConstants.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setForeground(Color.GRAY);
        titlePanel.add(statsLabel, BorderLayout.SOUTH);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Search and Filter Panel
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filters"));
        filterPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search row
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Search:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField searchField = new JTextField(20);
        filterPanel.add(searchField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0.0;
        JButton searchButton = createStyledButton("Search", new Color(0, 51, 102));
        filterPanel.add(searchButton, gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        JButton clearSearchButton = createStyledButton("Clear", new Color(0, 51, 102));
        filterPanel.add(clearSearchButton, gbc);

        // Filter row
        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Brand:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        JComboBox<String> brandFilter = new JComboBox<>();
        brandFilter.addItem("All Brands");
        List<String> brands = EnhancedShoeDataBase.getAllBrands();
        for (String brand : brands) {
            brandFilter.addItem(brand);
        }
        filterPanel.add(brandFilter, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        filterPanel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        JComboBox<String> categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All Categories");
        List<String> categories = EnhancedShoeDataBase.getAllCategories();
        for (String category : categories) {
            categoryFilter.addItem(category);
        }
        filterPanel.add(categoryFilter, gbc);

        gbc.gridx = 4; gbc.gridy = 1;
        JButton filterButton = createStyledButton("Apply Filters", new Color(0, 51, 102));
        filterPanel.add(filterButton, gbc);

        // Price filter row
        gbc.gridx = 0; gbc.gridy = 2;
        filterPanel.add(new JLabel("Price Range:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        JComboBox<String> priceFilter = new JComboBox<>(new String[]{
            "All Prices", "Under $50", "$50 - $100", "$100 - $150", "$150 - $200", "Over $200"
        });
        filterPanel.add(priceFilter, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        filterPanel.add(new JLabel("Sort By:"), gbc);
        
        gbc.gridx = 3; gbc.gridy = 2;
        JComboBox<String> sortFilter = new JComboBox<>(new String[]{
            "Default", "Price: Low to High", "Price: High to Low", "Name A-Z", "Brand A-Z"
        });
        filterPanel.add(sortFilter, gbc);

        gbc.gridx = 4; gbc.gridy = 2;
        JButton resetAllButton = createStyledButton("Reset All", new Color(0, 51, 102));
        filterPanel.add(resetAllButton, gbc);

        mainPanel.add(filterPanel, BorderLayout.CENTER);

        // Table setup
        String[] columnNames = {"ID", "Name", "Brand", "Size", "Color", "Price", "Quantity", "Category", "Description", "Date Added"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        shoesTable = new JTable(tableModel);
        shoesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shoesTable.getTableHeader().setReorderingAllowed(false);
        shoesTable.setAutoCreateRowSorter(true);
        shoesTable.setRowHeight(25);
        
        // Custom renderer for better appearance
        shoesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Reset background first
                c.setBackground(Color.WHITE);
                
                // Color coding for low quantity
                if (column == 6) { // Quantity column
                    try {
                        if (value != null) {
                            int quantity = Integer.parseInt(value.toString());
                            if (quantity < 5) {
                                c.setBackground(new Color(255, 200, 200)); // Light red for low stock
                            } else if (quantity < 10) {
                                c.setBackground(new Color(255, 255, 200)); // Light yellow for medium stock
                            }
                        }
                    } catch (NumberFormatException e) {
                        // If it's not a number, keep white background
                    }
                }
                
                // Highlight selected row
                if (isSelected) {
                    c.setBackground(new Color(200, 220, 255));
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(shoesTable);
        scrollPane.setPreferredSize(new Dimension(1100, 400));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Shoe Inventory"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel - ALL BUTTONS DARK BLUE
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton refreshBtn = createStyledButton("Refresh", new Color(0, 51, 102));
        JButton addBtn = createStyledButton("Add Shoe", new Color(0, 51, 102));
        JButton editBtn = createStyledButton("Edit Shoe", new Color(0, 51, 102));
        JButton deleteBtn = createStyledButton("Delete Shoe", new Color(0, 51, 102));
        JButton viewDetailsBtn = createStyledButton("View Details", new Color(0, 51, 102));
        JButton statsBtn = createStyledButton("Inventory Stats", new Color(0, 51, 102));
        JButton exportBtn = createStyledButton("Export Data", new Color(0, 51, 102));
        JButton logoutBtn = createStyledButton("Logout", new Color(0, 51, 102));
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(statsBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(logoutBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        loadShoesIntoTable();

        // Event listeners
        refreshBtn.addActionListener(e -> loadShoesIntoTable());
        
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                searchShoes(keyword);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a search term");
            }
        });
        
        clearSearchButton.addActionListener(e -> {
            searchField.setText("");
            loadShoesIntoTable();
        });
        
        filterButton.addActionListener(e -> {
            String selectedBrand = brandFilter.getSelectedItem().toString();
            String selectedCategory = categoryFilter.getSelectedItem().toString();
            String selectedPrice = priceFilter.getSelectedItem().toString();
            applyFilters(selectedBrand, selectedCategory, selectedPrice);
        });
        
        resetAllButton.addActionListener(e -> {
            brandFilter.setSelectedIndex(0);
            categoryFilter.setSelectedIndex(0);
            priceFilter.setSelectedIndex(0);
            sortFilter.setSelectedIndex(0);
            searchField.setText("");
            loadShoesIntoTable();
        });
        
        sortFilter.addActionListener(e -> {
            String sortOption = sortFilter.getSelectedItem().toString();
            sortTable(sortOption);
        });
        
        addBtn.addActionListener(e -> showAddShoeDialog());
        editBtn.addActionListener(e -> showEditShoeDialog());
        deleteBtn.addActionListener(e -> deleteSelectedShoe());
        viewDetailsBtn.addActionListener(e -> showShoeDetails());
        statsBtn.addActionListener(e -> showInventoryStatistics());
        exportBtn.addActionListener(e -> exportData());
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                loginPage.createLoginPage();
            }
        });

        // Double-click to view details
        shoesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showShoeDetails();
                }
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 51, 102)); // Dark blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 31, 63)), // Darker blue border
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 71, 143)); // Lighter blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 51, 102)); // Back to dark blue
            }
        });
        
        return button;
    }
    
    private static void loadShoesIntoTable() {
        tableModel.setRowCount(0);
        List<shoes> shoesList = EnhancedShoeDataBase.getAllShoes();
        
        for (shoes shoe : shoesList) {
            tableModel.addRow(shoe.toTableRow());
        }
        
        updateStatsLabel();
        autoResizeColumns();
    }
    
    private static void updateStatsLabel() {
        int totalShoes = tableModel.getRowCount();
        int totalQuantity = 0;
        double totalValue = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                Object quantityObj = tableModel.getValueAt(i, 6);
                Object priceObj = tableModel.getValueAt(i, 5);
                
                if (quantityObj != null && priceObj != null) {
                    int quantity = Integer.parseInt(quantityObj.toString());
                    String priceStr = priceObj.toString().replace("$", "").replace(",", "");
                    double price = Double.parseDouble(priceStr);
                    
                    totalQuantity += quantity;
                    totalValue += price * quantity;
                }
            } catch (NumberFormatException e) {
                // Skip invalid entries
            }
        }
        
        statsLabel.setText(String.format(
            "Total Models: %d | Total Quantity: %d | Total Inventory Value: $%.2f",
            totalShoes, totalQuantity, totalValue
        ));
    }
    
    private static void autoResizeColumns() {
        for (int i = 0; i < shoesTable.getColumnCount(); i++) {
            shoesTable.getColumnModel().getColumn(i).setPreferredWidth(120);
        }
        shoesTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        shoesTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        shoesTable.getColumnModel().getColumn(8).setPreferredWidth(200);
        shoesTable.getColumnModel().getColumn(9).setPreferredWidth(100);
    }
    
    private static void searchShoes(String keyword) {
        tableModel.setRowCount(0);
        List<shoes> shoesList = EnhancedShoeDataBase.searchShoes(keyword);
        
        for (shoes shoe : shoesList) {
            tableModel.addRow(shoe.toTableRow());
        }
        
        updateStatsLabel();
        
        if (shoesList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "No shoes found matching: '" + keyword + "'", 
                "Search Results", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Found " + shoesList.size() + " shoes matching: '" + keyword + "'", 
                "Search Results", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private static void applyFilters(String brand, String category, String priceRange) {
        tableModel.setRowCount(0);
        List<shoes> allShoes = EnhancedShoeDataBase.getAllShoes();
        
        for (shoes shoe : allShoes) {
            boolean matches = true;
            
            if (!brand.equals("All Brands") && !shoe.getBrand().equals(brand)) {
                matches = false;
            }
            
            if (!category.equals("All Categories") && !shoe.getCategory().equals(category)) {
                matches = false;
            }
            
            if (!priceRange.equals("All Prices")) {
                double price = shoe.getPrice();
                switch (priceRange) {
                    case "Under $50":
                        if (price >= 50) matches = false;
                        break;
                    case "$50 - $100":
                        if (price < 50 || price > 100) matches = false;
                        break;
                    case "$100 - $150":
                        if (price < 100 || price > 150) matches = false;
                        break;
                    case "$150 - $200":
                        if (price < 150 || price > 200) matches = false;
                        break;
                    case "Over $200":
                        if (price <= 200) matches = false;
                        break;
                }
            }
            
            if (matches) {
                tableModel.addRow(shoe.toTableRow());
            }
        }
        
        updateStatsLabel();
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(frame, "No shoes found with the selected filters");
        }
    }
    
    private static void sortTable(String sortOption) {
        switch (sortOption) {
            case "Price: Low to High":
                shoesTable.getRowSorter().toggleSortOrder(5);
                break;
            case "Price: High to Low":
                shoesTable.getRowSorter().toggleSortOrder(5);
                shoesTable.getRowSorter().toggleSortOrder(5);
                break;
            case "Name A-Z":
                shoesTable.getRowSorter().toggleSortOrder(1);
                break;
            case "Brand A-Z":
                shoesTable.getRowSorter().toggleSortOrder(2);
                break;
            default:
                loadShoesIntoTable();
                break;
        }
    }

    // FIXED ADD SHOE DIALOG
    private static void showAddShoeDialog() {
        JDialog addDialog = new JDialog(frame, "Add New Shoe", true);
        addDialog.setSize(500, 600);
        addDialog.setLocationRelativeTo(frame);
        addDialog.setLayout(new GridLayout(0, 2, 10, 10));
        addDialog.getContentPane().setBackground(new Color(240, 240, 240));
        
        // Form fields
        addDialog.add(new JLabel("Name:*"));
        JTextField nameField = new JTextField();
        addDialog.add(nameField);
        
        addDialog.add(new JLabel("Brand:*"));
        JComboBox<String> brandCombo = new JComboBox<>();
        brandCombo.addItem("Nike");
        brandCombo.addItem("Adidas");
        brandCombo.addItem("New Balance");
        brandCombo.addItem("Puma");
        brandCombo.addItem("Reebok");
        brandCombo.addItem("Converse");
        brandCombo.addItem("Vans");
        brandCombo.addItem("ASICS");
        brandCombo.setEditable(true);
        addDialog.add(brandCombo);
        
        addDialog.add(new JLabel("Size:*"));
        JComboBox<Double> sizeCombo = new JComboBox<>();
        for (double size = 6.0; size <= 13.0; size += 0.5) {
            sizeCombo.addItem(size);
        }
        addDialog.add(sizeCombo);
        
        addDialog.add(new JLabel("Color:*"));
        JTextField colorField = new JTextField();
        addDialog.add(colorField);
        
        addDialog.add(new JLabel("Price:*"));
        JTextField priceField = new JTextField();
        addDialog.add(priceField);
        
        addDialog.add(new JLabel("Quantity:*"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
        addDialog.add(quantitySpinner);
        
        addDialog.add(new JLabel("Category:*"));
        JComboBox<String> categoryCombo = new JComboBox<>();
        categoryCombo.addItem("Running");
        categoryCombo.addItem("Lifestyle");
        categoryCombo.addItem("Basketball");
        categoryCombo.addItem("Skateboarding");
        categoryCombo.addItem("Training");
        categoryCombo.addItem("Hiking");
        categoryCombo.addItem("Luxury");
        categoryCombo.addItem("Sandals");
        categoryCombo.setEditable(true);
        addDialog.add(categoryCombo);
        
        addDialog.add(new JLabel("Description:"));
        JTextArea descriptionArea = new JTextArea(3, 20);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        addDialog.add(descriptionScroll);
        
        // Buttons - DARK BLUE
        JButton saveButton = createStyledButton("Save Shoe", new Color(0, 51, 102));
        JButton cancelButton = createStyledButton("Cancel", new Color(0, 51, 102));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        addDialog.add(new JLabel());
        addDialog.add(buttonPanel);
        
        saveButton.addActionListener(e -> {
            // Validation
            if (nameField.getText().trim().isEmpty() ||
                colorField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(addDialog, 
                    "Please fill in all required fields (*)", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Create new shoe object
                shoes newShoe = new shoes(
                    nameField.getText().trim(),
                    brandCombo.getSelectedItem().toString(),
                    (Double) sizeCombo.getSelectedItem(),
                    colorField.getText().trim(),
                    Double.parseDouble(priceField.getText().trim()),
                    (Integer) quantitySpinner.getValue(),
                    categoryCombo.getSelectedItem().toString(),
                    descriptionArea.getText().trim()
                );
                
                // Save to database
                boolean success = EnhancedShoeDataBase.addShoe(newShoe);
                
                if (success) {
                    JOptionPane.showMessageDialog(addDialog, 
                        "Shoe added successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    addDialog.dispose();
                    loadShoesIntoTable();
                } else {
                    JOptionPane.showMessageDialog(addDialog, 
                        "Failed to add shoe. Please try again.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addDialog, 
                    "Please enter a valid price!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> addDialog.dispose());
        
        addDialog.setVisible(true);
    }

    // FIXED EDIT SHOE DIALOG - No more "Illegal value: -1"
    private static void showEditShoeDialog() {
        int selectedRow = shoesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, 
                "Please select a shoe to edit", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // FIX: Safe conversion and validation
        int modelRow;
        try {
            modelRow = shoesTable.convertRowIndexToModel(selectedRow);
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(frame, 
                "Invalid selection. Please select a valid shoe.", 
                "Selection Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // FIX: Validate model row
        if (modelRow < 0 || modelRow >= tableModel.getRowCount()) {
            JOptionPane.showMessageDialog(frame, 
                "Invalid selection. Please select a valid shoe.", 
                "Selection Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // FIX: Safe data retrieval
        Object idValue = tableModel.getValueAt(modelRow, 0);
        if (idValue == null) {
            JOptionPane.showMessageDialog(frame, 
                "Error: Could not get shoe ID", 
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int shoeId;
        try {
            shoeId = Integer.parseInt(idValue.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, 
                "Error: Invalid shoe ID format", 
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Fetch current shoe data
        shoes currentShoe = EnhancedShoeDataBase.getShoeById(shoeId);
        if (currentShoe == null) {
            JOptionPane.showMessageDialog(frame, 
                "Error loading shoe data!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog editDialog = new JDialog(frame, "Edit Shoe - " + currentShoe.getName(), true);
        editDialog.setSize(500, 600);
        editDialog.setLocationRelativeTo(frame);
        editDialog.setLayout(new GridLayout(0, 2, 10, 10));
        editDialog.getContentPane().setBackground(new Color(240, 240, 240));
        
        // Form fields with current values
        editDialog.add(new JLabel("Name:*"));
        JTextField nameField = new JTextField(currentShoe.getName());
        editDialog.add(nameField);
        
        editDialog.add(new JLabel("Brand:*"));
        JComboBox<String> brandCombo = new JComboBox<>();
        brandCombo.addItem("Nike");
        brandCombo.addItem("Adidas");
        brandCombo.addItem("New Balance");
        brandCombo.addItem("Puma");
        brandCombo.addItem("Reebok");
        brandCombo.addItem("Converse");
        brandCombo.addItem("Vans");
        brandCombo.addItem("ASICS");
        brandCombo.setEditable(true);
        brandCombo.setSelectedItem(currentShoe.getBrand());
        editDialog.add(brandCombo);
        
        editDialog.add(new JLabel("Size:*"));
        JComboBox<Double> sizeCombo = new JComboBox<>();
        for (double size = 6.0; size <= 13.0; size += 0.5) {
            sizeCombo.addItem(size);
        }
        sizeCombo.setSelectedItem(currentShoe.getSize());
        editDialog.add(sizeCombo);
        
        editDialog.add(new JLabel("Color:*"));
        JTextField colorField = new JTextField(currentShoe.getColor());
        editDialog.add(colorField);
        
        editDialog.add(new JLabel("Price:*"));
        JTextField priceField = new JTextField(String.valueOf(currentShoe.getPrice()));
        editDialog.add(priceField);
        
        editDialog.add(new JLabel("Quantity:*"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(currentShoe.getQuantity(), 0, 1000, 1));
        editDialog.add(quantitySpinner);
        
        editDialog.add(new JLabel("Category:*"));
        JComboBox<String> categoryCombo = new JComboBox<>();
        categoryCombo.addItem("Running");
        categoryCombo.addItem("Lifestyle");
        categoryCombo.addItem("Basketball");
        categoryCombo.addItem("Skateboarding");
        categoryCombo.addItem("Training");
        categoryCombo.addItem("Hiking");
        categoryCombo.addItem("Luxury");
        categoryCombo.addItem("Sandals");
        categoryCombo.setEditable(true);
        categoryCombo.setSelectedItem(currentShoe.getCategory());
        editDialog.add(categoryCombo);
        
        editDialog.add(new JLabel("Description:"));
        JTextArea descriptionArea = new JTextArea(currentShoe.getDescription(), 3, 20);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        editDialog.add(descriptionScroll);
        
        // Buttons - DARK BLUE
        JButton saveButton = createStyledButton("Update Shoe", new Color(0, 51, 102));
        JButton cancelButton = createStyledButton("Cancel", new Color(0, 51, 102));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        editDialog.add(new JLabel());
        editDialog.add(buttonPanel);
        
        saveButton.addActionListener(e -> {
            // Validation
            if (nameField.getText().trim().isEmpty() ||
                colorField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(editDialog, 
                    "Please fill in all required fields (*)", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Create updated shoe object
                shoes updatedShoe = new shoes(
                    currentShoe.getId(),
                    nameField.getText().trim(),
                    brandCombo.getSelectedItem().toString(),
                    (Double) sizeCombo.getSelectedItem(),
                    colorField.getText().trim(),
                    Double.parseDouble(priceField.getText().trim()),
                    (Integer) quantitySpinner.getValue(),
                    categoryCombo.getSelectedItem().toString(),
                    descriptionArea.getText().trim(),
                    currentShoe.getDateAdded()
                );
                
                // Update in database
                boolean success = EnhancedShoeDataBase.updateShoe(updatedShoe);
                
                if (success) {
                    JOptionPane.showMessageDialog(editDialog, 
                        "Shoe updated successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    editDialog.dispose();
                    loadShoesIntoTable();
                } else {
                    JOptionPane.showMessageDialog(editDialog, 
                        "Failed to update shoe. Please try again.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editDialog, 
                    "Please enter a valid price!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> editDialog.dispose());
        
        editDialog.setVisible(true);
    }

    // FIXED DELETE SHOE METHOD - No more "Illegal value: -1"
    private static void deleteSelectedShoe() {
        int selectedRow = shoesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, 
                "Please select a shoe to delete", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // FIX: Safe conversion and validation
        int modelRow;
        try {
            modelRow = shoesTable.convertRowIndexToModel(selectedRow);
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(frame, 
                "Invalid selection. Please select a valid shoe.", 
                "Selection Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // FIX: Validate model row
        if (modelRow < 0 || modelRow >= tableModel.getRowCount()) {
            JOptionPane.showMessageDialog(frame, 
                "Invalid selection. Please select a valid shoe.", 
                "Selection Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // FIX: Safe data retrieval
        Object idValue = tableModel.getValueAt(modelRow, 0);
        Object nameValue = tableModel.getValueAt(modelRow, 1);
        Object brandValue = tableModel.getValueAt(modelRow, 2);
        
        if (idValue == null || nameValue == null || brandValue == null) {
            JOptionPane.showMessageDialog(frame, 
                "Error: Could not get shoe data", 
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int shoeId;
        try {
            shoeId = Integer.parseInt(idValue.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, 
                "Error: Invalid shoe ID format", 
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String shoeName = nameValue.toString();
        String shoeBrand = brandValue.toString();
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Are you sure you want to delete:\n" +
            shoeName + " - " + shoeBrand + "?\n\n" +
            "This action cannot be undone.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = EnhancedShoeDataBase.deleteShoe(shoeId);
            if (success) {
                JOptionPane.showMessageDialog(frame, 
                    "Shoe deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadShoesIntoTable(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Failed to delete shoe. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // FIXED SHOW SHOE DETAILS - No more "Illegal value: -1"
    private static void showShoeDetails() {
        int selectedRow = shoesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, 
                "Please select a shoe to view details", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // FIX: Safe conversion and validation
        int modelRow;
        try {
            modelRow = shoesTable.convertRowIndexToModel(selectedRow);
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(frame, 
                "Invalid selection. Please select a valid shoe.", 
                "Selection Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // FIX: Validate model row
        if (modelRow < 0 || modelRow >= tableModel.getRowCount()) {
            JOptionPane.showMessageDialog(frame, 
                "Invalid selection. Please select a valid shoe.", 
                "Selection Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder details = new StringBuilder();
        details.append("=== SHOE DETAILS ===\n\n");
        
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            Object value = tableModel.getValueAt(modelRow, i);
            details.append(tableModel.getColumnName(i))
                   .append(": ")
                   .append(value != null ? value.toString() : "N/A")
                   .append("\n");
        }
        
        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(frame, scrollPane, 
            "Shoe Details", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void showInventoryStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== INVENTORY STATISTICS ===\n\n");
        
        int totalModels = tableModel.getRowCount();
        int totalQuantity = 0;
        double totalValue = 0;
        double minPrice = Double.MAX_VALUE;
        double maxPrice = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                Object quantityObj = tableModel.getValueAt(i, 6);
                Object priceObj = tableModel.getValueAt(i, 5);
                
                if (quantityObj != null && priceObj != null) {
                    int quantity = Integer.parseInt(quantityObj.toString());
                    String priceStr = priceObj.toString().replace("$", "").replace(",", "");
                    double price = Double.parseDouble(priceStr);
                    
                    totalQuantity += quantity;
                    totalValue += price * quantity;
                    minPrice = Math.min(minPrice, price);
                    maxPrice = Math.max(maxPrice, price);
                }
            } catch (NumberFormatException e) {
                // Skip invalid entries
            }
        }
        
        double avgPrice = totalQuantity > 0 ? totalValue / totalQuantity : 0;
        
        stats.append(String.format("Total Shoe Models: %,d\n", totalModels));
        stats.append(String.format("Total Quantity: %,d\n", totalQuantity));
        stats.append(String.format("Total Inventory Value: $%,.2f\n", totalValue));
        stats.append(String.format("Average Price: $%,.2f\n", avgPrice));
        stats.append(String.format("Price Range: $%,.2f - $%,.2f\n", 
            minPrice != Double.MAX_VALUE ? minPrice : 0, maxPrice));
        stats.append(String.format("Average Stock per Model: %,.1f\n", 
            totalModels > 0 ? (double) totalQuantity / totalModels : 0));
        
        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        
        JOptionPane.showMessageDialog(frame, scrollPane, 
            "Inventory Statistics", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Inventory Data");
        fileChooser.setSelectedFile(new java.io.File("shoe_inventory_export.csv"));
        
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(frame, 
                "Data would be exported to: " + file.getAbsolutePath() + "\n\n" +
                "Export would include:\n" +
                "- All shoe data in CSV format\n" +
                "- Current filters and sorting\n" +
                "- Inventory statistics summary",
                "Export Simulation",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}