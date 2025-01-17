package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.model.EmploymentStatus;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeePanel extends JPanel {
    
    private final JTable employeeTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;
    private final JComboBox<String> departmentFilter;
    private final JComboBox<EmploymentStatus> statusFilter;

    public EmployeePanel() {
        setLayout(new MigLayout("fill", "[grow]", "[]10[]10[grow]"));
        
        // Create toolbar panel
        JPanel toolbarPanel = new JPanel(new MigLayout("fillx", "[]10[]push[]10[]10[]", "[]"));
        
        // Search components
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        
        // Filter components
        departmentFilter = new JComboBox<>(new String[]{"All Departments", "HR", "IT", "Finance"});
        statusFilter = new JComboBox<>(EmploymentStatus.values());
        
        // Add button
        JButton addButton = new JButton("Add Employee");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        
        // Add components to toolbar
        toolbarPanel.add(searchField, "growx");
        toolbarPanel.add(searchButton);
        toolbarPanel.add(departmentFilter);
        toolbarPanel.add(statusFilter);
        toolbarPanel.add(addButton);
        
        // Create table
        String[] columns = {
            "ID", "Name", "Department", "Job Title", 
            "Status", "Hire Date", "Email", "Actions"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == columns.length - 1; // Only actions column is editable
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setFillsViewportHeight(true);
        
        // Style table
        employeeTable.setRowHeight(30);
        employeeTable.getTableHeader().setBackground(new Color(240, 240, 240));
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add components to main panel
        add(createTitlePanel(), "growx, wrap");
        add(toolbarPanel, "growx, wrap");
        add(new JScrollPane(employeeTable), "grow");
        
        // Add listeners
        addButton.addActionListener(e -> showAddEmployeeDialog());
        searchButton.addActionListener(e -> performSearch());
        departmentFilter.addActionListener(e -> applyFilters());
        statusFilter.addActionListener(e -> applyFilters());
        
        // Load initial data
        loadEmployeeData();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new MigLayout("fillx", "[]push[]", "[]"));
        JLabel titleLabel = new JLabel("Employee Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private void showAddEmployeeDialog() {
        // TODO: Implement add employee dialog
        JOptionPane.showMessageDialog(this, "Add Employee Dialog - To be implemented");
    }

    private void performSearch() {
        String searchTerm = searchField.getText();
        // TODO: Implement search functionality
        System.out.println("Searching for: " + searchTerm);
    }

    private void applyFilters() {
        String department = (String) departmentFilter.getSelectedItem();
        EmploymentStatus status = (EmploymentStatus) statusFilter.getSelectedItem();
        // TODO: Implement filter functionality
        System.out.println("Filtering - Department: " + department + ", Status: " + status);
    }

    private void loadEmployeeData() {
        // TODO: Load actual data from service
        // For now, add some sample data
        Object[][] sampleData = {
            {"EMP001", "John Doe", "IT", "Developer", "ACTIVE", "2024-01-01", "john@example.com", "Edit/Delete"},
            {"EMP002", "Jane Smith", "HR", "Manager", "ACTIVE", "2024-01-01", "jane@example.com", "Edit/Delete"}
        };
        
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
}
