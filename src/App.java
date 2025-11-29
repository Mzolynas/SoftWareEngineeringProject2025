import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize database
        SwingUtilities.invokeLater(() -> {
            EnhancedShoeDataBase.initializeDatabase();
            
            // Start the login page
            loginPage.createLoginPage();
        });
    }
}