import javax.swing.*;

public class MainPage {
    public static void createMainPage() {
        JFrame mainFrame = new JFrame("Shoe Inventory System");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainFrame.add(mainPanel);
        mainPanel.setLayout(null);
        
        JLabel testLabel = new JLabel("test");
        testLabel.setBounds(180, 120, 100, 50);
        mainPanel.add(testLabel);
        
        mainFrame.setVisible(true);
    }
}