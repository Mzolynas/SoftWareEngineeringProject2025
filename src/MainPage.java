// ... (MainPage.java 开头)
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // 引入 ActionListener

public class MainPage {
    // 将表格模型声明为静态变量，以便其他方法可以访问和刷新它
    private static DefaultTableModel tableModel; 
    private static JFrame mainFrame;
    
    public static void createMainPage() {
        mainFrame = new JFrame("Shoe Inventory System - Admin View");
        mainFrame.setSize(600, 400); 
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout()); 
        mainFrame.add(mainPanel);
        
        // --- 替换标题标签为顶部控制面板 ---
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Current Shoe Inventory", SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // 添加按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add New Item");
        
        // 给按钮添加事件监听器
        addButton.addActionListener(e -> openAddItemDialog());
        
        buttonPanel.add(addButton);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH); // 添加到主面板的顶部

        // --- 表格代码修改：使用静态 tableModel ---
        String[] columnNames = {"Model", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0); // 使用静态变量
        JTable table = new JTable(tableModel);
        
        // 首次加载数据
        refreshInventoryTable(); 
        
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainFrame.setVisible(true);
    }
    
    // ----------------------------------------------------
    // 新增方法 1: 刷新表格数据
    // ----------------------------------------------------
    private static void refreshInventoryTable() {
        if (tableModel != null) {
            tableModel.setRowCount(0); // 清空现有行
            for (Shoe shoe : InventoryService.getAllShoes()) {
                tableModel.addRow(new Object[]{shoe.getModel(), shoe.getQuantity(), shoe.getPrice()});
            }
        }
    }
    
    // ----------------------------------------------------
    // 新增方法 2: 添加库存弹窗逻辑 (对应 EP2-S3)
    // ----------------------------------------------------
    private static void openAddItemDialog() {
        JDialog addDialog = new JDialog(mainFrame, "Add New Shoe Item", true);
        addDialog.setSize(350, 200);
        addDialog.setLayout(new GridLayout(4, 2)); // 简单的网格布局

        JTextField modelField = new JTextField(15);
        JTextField quantityField = new JTextField(15);
        JTextField priceField = new JTextField(15);
        
        addDialog.add(new JLabel("  Model:"));
        addDialog.add(modelField);
        addDialog.add(new JLabel("  Quantity:"));
        addDialog.add(quantityField);
        addDialog.add(new JLabel("  Price:"));
        addDialog.add(priceField);
        
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String model = modelField.getText().trim();
                    int quantity = Integer.parseInt(quantityField.getText().trim());
                    double price = Double.parseDouble(priceField.getText().trim());

                    if (model.isEmpty() || quantity <= 0 || price <= 0) {
                        JOptionPane.showMessageDialog(addDialog, "All fields must be valid!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // 1. 调用服务层添加数据
                    InventoryService.addItem(new Shoe(model, quantity, price));
                    
                    // 2. 刷新主界面表格
                    refreshInventoryTable();
                    
                    JOptionPane.showMessageDialog(addDialog, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    addDialog.dispose();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addDialog, "Quantity and Price must be valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        addDialog.add(submitButton);
        addDialog.setVisible(true);
    }
}