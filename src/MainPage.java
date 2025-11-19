import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainPage {
    public static void createMainPage() {
        // Basic frame
        JFrame frame = new JFrame("Shoe Inventory");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Simple text area to show shoes
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        // Load and show shoes
        loadShoes(textArea);
        
        // Single button to refresh
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadShoes(textArea));
        
        // Layout
        frame.setLayout(new BorderLayout());
        frame.add(new JLabel("Shoe Inventory", SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(refreshBtn, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
    
    private static void loadShoes(JTextArea textArea) {
        List<shoes> shoes = shoeDataBase.getAllShoes();
        StringBuilder sb = new StringBuilder();
        
        for (shoes shoe : shoes) {
            sb.append(shoe.getId())
              .append(". ")
              .append(shoe.getName())
              .append(" - ")
              .append(shoe.getBrand())
              .append(" - $")
              .append(shoe.getPrice())
              .append("\n");
        }
        
        textArea.setText(sb.toString());
    }
}