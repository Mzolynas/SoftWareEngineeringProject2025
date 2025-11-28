import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginPage {
    public static void createLoginPage() {
        JFrame frame = new JFrame("Shoe Inventory Login");
        frame.setSize(400, 400);
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

        JLabel userLabel = new JLabel("Email:");
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

        // NEW: hint label for format requirements
        JLabel hintLabel = new JLabel("Email: name@example.com  Password: letters & numbers only");
        hintLabel.setBounds(20, 160, 360, 25);
        panel.add(hintLabel);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 200, 80, 25);
        loginButton.addActionListener(e -> {
            // retrieves the login inputs and shows an error message if the username or password is left empty.
            String userType = (String) loginType.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Username and password are required!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Admin login: only admin/admin
            if (userType.equals("Admin")) {
                if (username.equals("admin") && password.equals("admin")) {
                    JOptionPane.showMessageDialog(frame,
                            "Admin Login Successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    MainPage.createMainPage();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid admin credentials!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                return;
            }

            // Customer login:
            // 1) email must be normal format
            if (!AuthService.isValidEmail(username)) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid email format! Example: name@example.com",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2) password only letters and digits
            if (!AuthService.isValidPassword(password)) {
                JOptionPane.showMessageDialog(frame,
                        "Password must contain only letters and numbers.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(frame,
                    "Customer Login Successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            MainPage.createMainPage();
        });
        panel.add(loginButton);

        JButton resetButton = new JButton("Reset Password");
        resetButton.setBounds(130, 240, 140, 25);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openResetPasswordDialog();
            }
        });
        panel.add(resetButton);

        frame.setVisible(true);
    }

    private static void openResetPasswordDialog() {
        // Verify "Forgot password" button is pressed
        JDialog resetDialog = new JDialog();
        resetDialog.setTitle("Reset Password");
        resetDialog.setSize(400, 350);
        resetDialog.setLayout(null);
        resetDialog.setModal(true);
        resetDialog.setLocationRelativeTo(null);

        JLabel emailLabel = new JLabel("Enter your email:");
        emailLabel.setBounds(50, 30, 120, 25);
        resetDialog.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(180, 30, 150, 25);
        resetDialog.add(emailField);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setBounds(50, 80, 120, 25);
        resetDialog.add(newPasswordLabel);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setBounds(180, 80, 150, 25);
        resetDialog.add(newPasswordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(50, 130, 120, 25);
        resetDialog.add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(180, 130, 150, 25);
        resetDialog.add(confirmPasswordField);

        JButton submitButton = new JButton("Reset Password");
        submitButton.setBounds(130, 220, 140, 25);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Verify valid email is entered
                if (!AuthService.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(resetDialog,
                            "Please enter a valid email address!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verify new password is entered
                if (newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(resetDialog,
                            "Please enter a new password!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verify passwords match
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(resetDialog,
                            "Passwords do not match!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verify new password: letters and digits only
                if (!AuthService.isValidPassword(newPassword)) {
                    JOptionPane.showMessageDialog(resetDialog,
                            "Password must contain only letters and numbers.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Simulate email sending
                boolean emailSent = simulateEmailSending(email);
                if (!emailSent) {
                    JOptionPane.showMessageDialog(resetDialog,
                            "Failed to send reset email. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(resetDialog,
                        "Password reset successful! Check your email for confirmation.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                resetDialog.dispose();
            }
        });
        resetDialog.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(150, 220, 120, 25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetDialog.dispose();
            }
        });
        resetDialog.add(cancelButton);

        resetDialog.setVisible(true);
    }

    private static boolean simulateEmailSending(String email) {
        // Simulate email sending - in real implementation, this would connect to an email service
        // For now, just return true to simulate successful email sending
        return true;
    }
}
