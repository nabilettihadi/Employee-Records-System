package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.ui.MainFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class NavigationPanel extends JPanel {
    
    private final MainFrame mainFrame;
    private final JLabel userLabel;
    private final JLabel roleLabel;

    public NavigationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new MigLayout("fillx", "[]", "[]20[][]20[]"));
        setPreferredSize(new Dimension(200, 0));
        setBackground(new Color(51, 51, 51));
        
        // User info section
        userLabel = new JLabel("Not logged in");
        roleLabel = new JLabel("");
        styleLabel(userLabel);
        styleLabel(roleLabel);
        
        // Navigation buttons
        JButton employeesBtn = createNavButton("Employees");
        JButton departmentsBtn = createNavButton("Departments");
        JButton logoutBtn = createNavButton("Logout");

        // Add components
        add(userLabel, "wrap");
        add(roleLabel, "wrap");
        add(employeesBtn, "growx, wrap");
        add(departmentsBtn, "growx, wrap");
        add(logoutBtn, "growx, push, bottom");

        // Add actions
        employeesBtn.addActionListener(e -> mainFrame.showEmployeePanel());
        departmentsBtn.addActionListener(e -> mainFrame.showDepartmentPanel());
        logoutBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                mainFrame.showLoginPanel();
            }
        });
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(64, 64, 64));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    public void updateUserInfo(String username, String role) {
        userLabel.setText("User: " + username);
        roleLabel.setText("Role: " + role);
    }
}
