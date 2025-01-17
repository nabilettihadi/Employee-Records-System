package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.ui.MainFrame;
import com.employee.recordsystem.ui.service.AuthService;
import com.employee.recordsystem.dto.auth.JwtAuthenticationResponse;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginPanel extends JPanel {
    
    private final MainFrame mainFrame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final AuthService authService;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authService = new AuthService();
        setLayout(new MigLayout("fill", "[][]", "[]20[][]20[]"));
        
        // Create components
        JLabel titleLabel = new JLabel("Employee Records Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        // Add components
        add(titleLabel, "span 2, center, wrap");
        add(usernameLabel, "right");
        add(usernameField, "growx, wrap");
        add(passwordLabel, "right");
        add(passwordField, "growx, wrap");
        add(loginButton, "span 2, center");

        // Add action
        loginButton.addActionListener(e -> handleLogin());
        
        // Add key listener for Enter key
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            JwtAuthenticationResponse response = authService.login(username, password);
            mainFrame.setCurrentUser(response.getUsername(), response.getRole());
            mainFrame.showEmployeePanel();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Login failed: " + ex.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
            usernameField.setText("");
            passwordField.setText("");
        }
    }
}
