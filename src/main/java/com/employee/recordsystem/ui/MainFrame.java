package com.employee.recordsystem.ui;

import com.employee.recordsystem.ui.panels.*;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final LoginPanel loginPanel;
    private final EmployeePanel employeePanel;
    private final DepartmentPanel departmentPanel;
    private final NavigationPanel navigationPanel;
    private final String currentUser;
    private final String userRole;

    public MainFrame() {
        setTitle("Employee Records Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Initialize components
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        loginPanel = new LoginPanel(this);
        employeePanel = new EmployeePanel();
        departmentPanel = new DepartmentPanel();
        navigationPanel = new NavigationPanel(this);
        currentUser = null;
        userRole = null;

        // Setup layout
        setLayout(new MigLayout("fill"));
        
        // Add panels to card layout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(employeePanel, "EMPLOYEES");
        mainPanel.add(departmentPanel, "DEPARTMENTS");

        // Add components to frame
        add(navigationPanel, "dock west");
        add(mainPanel, "grow");

        // Start with login panel
        showLoginPanel();
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
        navigationPanel.setVisible(false);
    }

    public void showEmployeePanel() {
        cardLayout.show(mainPanel, "EMPLOYEES");
        navigationPanel.setVisible(true);
    }

    public void showDepartmentPanel() {
        cardLayout.show(mainPanel, "DEPARTMENTS");
        navigationPanel.setVisible(true);
    }

    public void setCurrentUser(String username, String role) {
        navigationPanel.updateUserInfo(username, role);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
