import javax.swing.*;

public class loginPage {
    public static void createLoginPage() {
        JFrame frame = new JFrame("Shoe Inventory Login");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);
        
        JLabel typeLabel = new JLabel("Login Type:");
        typeLabel.setBounds(50, 30, 80, 25);
        panel.add(typeLabel);
        
        JComboBox<String> loginType = new JComboBox<>();
        loginType.setBounds(150, 30, 165, 25);
        loginType.addItem("Customer");
        loginType.addItem("Admin");
        panel.add(loginType);
        
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 80, 25);
        panel.add(userLabel);
        
        JTextField userField = new JTextField(20);
        userField.setBounds(150, 80, 165, 25);
        panel.add(userField);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 130, 80, 25);
        panel.add(passwordLabel);
        
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 130, 165, 25);
        panel.add(passwordField);
        
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 180, 80, 25);
        loginButton.addActionListener(e -> {
        	
        	
        	
            // TEAM: AVERY - Get user input and validate empty fields
            /*
            String userType = (String) loginType.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            */
            
        	
        	
            // TEAM: SLUNKA - Implement admin login validation
            /*
            if (userType.equals("Admin")) {
                if (username.equals("admin") && password.equals("admin")) {
                    JOptionPane.showMessageDialog(frame, "Admin Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    MainPage.createMainPage(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            */
            
        	
        	
            // DOM - Implement customer login validation  
            /*
            if (!AuthService.isValidEmail(username)) {
                JOptionPane.showMessageDialog(frame, "Invalid email format! Must be xxxx@xxx.xx", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!AuthService.isValidPassword(password)) {
                JOptionPane.showMessageDialog(frame, "Password must be at least 8 characters and contain 1 special character!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(frame, "Customer Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            MainPage.createMainPage(false);
            */
        	
        	
        });
        panel.add(loginButton);
        
        JButton resetButton = new JButton("Reset Password");
        resetButton.setBounds(150, 220, 120, 25);
        panel.add(resetButton);
        
        frame.setVisible(true);
    }
}