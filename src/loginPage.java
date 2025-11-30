import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginPage {
    public static void createLoginPage() {
        JFrame frame = new JFrame("Shoe Inventory System - Employee Login");
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
        loginType.addItem("Employee");
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

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 180, 80, 25);
        loginButton.addActionListener(e -> {
            // Retrieve login inputs and validate required fields
            String userType = (String) loginType.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Email and password are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Admin authentication
            if (userType.equals("Admin")) {
                if (username.equals("admin") && password.equals("admin")) {
                    JOptionPane.showMessageDialog(frame, "Admin Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    MainPage.createMainPage();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            // Employee authentication with enhanced validation
            else if (userType.equals("Employee")) {
                if (!AuthService.isValidEmail(username)) {
                    JOptionPane.showMessageDialog(frame, "Invalid email format! Must be in format: name@company.com", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!AuthService.isValidPassword(password)) {
                    JOptionPane.showMessageDialog(frame, "Password must be at least 8 characters and contain 1 special character!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Enhanced employee login success message
                JOptionPane.showMessageDialog(frame, "Employee Login Successful!\nWelcome to the Inventory System", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                EmployeePage.createEmployeePage();
                return;
            }
        });
        panel.add(loginButton);

        JButton resetButton = new JButton("Reset Password");
        resetButton.setBounds(130, 220, 140, 25);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openResetPasswordDialog();
            }
        });
        panel.add(resetButton);

        // Add help text for employees
        JLabel helpLabel = new JLabel("<html>Employee: Use company email & secure password<br>Admin: Use default credentials</html>");
        helpLabel.setBounds(50, 260, 300, 50);
        helpLabel.setForeground(Color.GRAY);
        helpLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        panel.add(helpLabel);

        frame.setVisible(true);
    }

    private static void openResetPasswordDialog() {
        JDialog resetDialog = new JDialog();
        resetDialog.setTitle("Reset Employee Password");
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
        submitButton.setBounds(100, 190, 120, 25);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (!AuthService.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(resetDialog, "Please enter a valid company email address!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(resetDialog, "Please enter a new password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(resetDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!AuthService.isValidPassword(newPassword)) {
                    JOptionPane.showMessageDialog(resetDialog, "Password must be at least 8 characters and contain 1 special character!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean emailSent = simulateEmailSending(email);
                if (!emailSent) {
                    JOptionPane.showMessageDialog(resetDialog, "Failed to send reset email. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(resetDialog, "Password reset successful! Check your email for confirmation.", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetDialog.dispose();
            }
        });
        resetDialog.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(230, 190, 80, 25);
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
        System.out.println("Sending password reset email to: " + email);
        return true;
    }
}