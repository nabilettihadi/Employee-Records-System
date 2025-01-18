package com.employee.recordsystem.ui;

import com.employee.recordsystem.service.AuthService;
import com.employee.recordsystem.service.DepartmentService;
import com.employee.recordsystem.service.EmployeeService;
import com.employee.recordsystem.ui.panels.DepartmentPanel;
import com.employee.recordsystem.ui.panels.EmployeePanel;
import com.employee.recordsystem.ui.panels.LoginPanel;
import com.employee.recordsystem.ui.panels.NavigationPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@DependsOn({"authService", "employeeService", "departmentService"})
public class MainFrame extends JFrame {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private DepartmentService departmentService;
    
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private LoginPanel loginPanel;
    private EmployeePanel employeePanel;
    private DepartmentPanel departmentPanel;
    private NavigationPanel navigationPanel;
    private String currentUser;
    private String userRole;

    public MainFrame() {
        // Default constructor for Spring
        SwingUtilities.invokeLater(this::initializeUI);
    }

    private void initializeUI() {
        setTitle("Employee Records System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // Initialize panels
        loginPanel = new LoginPanel(authService, this);
        employeePanel = new EmployeePanel(employeeService, departmentService);
        departmentPanel = new DepartmentPanel(departmentService);
        navigationPanel = new NavigationPanel(this);

        // Initialize UI for each panel
        loginPanel.initializeUI();
        employeePanel.initializeUI();
        departmentPanel.initializeUI();

        // Add panels to card layout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(employeePanel, "EMPLOYEES");
        mainPanel.add(departmentPanel, "DEPARTMENTS");

        // Create main layout
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(navigationPanel, BorderLayout.WEST);
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        // Add to frame
        add(contentPanel);

        // Set menu bar
        setJMenuBar(createMenuBar());

        // Start with login panel
        showLoginPanel();
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
        loginPanel.clearFields();
        navigationPanel.setVisible(false);
    }

    public void showMainPanel() {
        cardLayout.show(mainPanel, "EMPLOYEES");
        navigationPanel.setVisible(true);
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
        this.currentUser = username;
        this.userRole = role;
        navigationPanel.updateUserInfo(username, role);
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getUserRole() {
        return userRole;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        logoutItem.addActionListener(e -> {
            authService.logout();
            currentUser = null;
            userRole = null;
            showLoginPanel();
        });
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // View menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem employeesItem = new JMenuItem("Employees");
        JMenuItem departmentsItem = new JMenuItem("Departments");
        
        employeesItem.addActionListener(e -> showEmployeePanel());
        departmentsItem.addActionListener(e -> showDepartmentPanel());
        
        viewMenu.add(employeesItem);
        viewMenu.add(departmentsItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Employee Records System\nVersion 1.0\n 2025 Your Company",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
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
