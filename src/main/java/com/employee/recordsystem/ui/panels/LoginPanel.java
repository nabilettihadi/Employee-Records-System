package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.dto.auth.JwtAuthenticationResponse;
import com.employee.recordsystem.dto.auth.LoginRequest;
import com.employee.recordsystem.service.AuthService;
import com.employee.recordsystem.ui.MainFrame;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class LoginPanel extends JPanel {
    private final AuthService authService;
    private final MainFrame mainFrame;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        JLabel titleLabel = new JLabel("Employee Records System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 30, 5);
        add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        add(loginButton, gbc);

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 5, 5, 5);
        add(messageLabel, gbc);

        // Add action listener
        loginButton.addActionListener(e -> handleLogin());
        
        // Add key listener for Enter key
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password");
            return;
        }

        try {
            LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

            JwtAuthenticationResponse response = authService.login(loginRequest);
            messageLabel.setText("");
            mainFrame.setCurrentUser(response.getUsername(), response.getRole());
            mainFrame.showMainPanel();
        } catch (Exception e) {
            messageLabel.setText("Invalid username or password");
            passwordField.setText("");
        }
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText(" ");
    }
}
