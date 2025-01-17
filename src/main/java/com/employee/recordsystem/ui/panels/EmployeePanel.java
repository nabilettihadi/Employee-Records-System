package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.ui.service.EmployeeService;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class EmployeePanel extends JPanel {
    
    private final JTable employeeTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;
    private final JComboBox<String> departmentFilter;
    private final JComboBox<EmploymentStatus> statusFilter;
    private final EmployeeService employeeService;

    public EmployeePanel() {
        setLayout(new MigLayout("fill", "[grow]", "[]10[]10[grow]"));
        this.employeeService = new EmployeeService();
        
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
        // TODO: Implement add employee dialog with service integration
        JOptionPane.showMessageDialog(this, "Add Employee Dialog - To be implemented");
    }

    private void performSearch() {
        String searchTerm = searchField.getText();
        try {
            List<EmployeeDTO> employees = employeeService.searchEmployees(searchTerm);
            updateTableData(employees);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error searching employees: " + ex.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        String department = (String) departmentFilter.getSelectedItem();
        EmploymentStatus status = (EmploymentStatus) statusFilter.getSelectedItem();
        
        try {
            List<EmployeeDTO> employees;
            if (!"All Departments".equals(department)) {
                employees = employeeService.getEmployeesByDepartment(1L); // TODO: Get actual department ID
            } else if (status != null) {
                employees = employeeService.getEmployeesByStatus(status.name());
            } else {
                employees = employeeService.getAllEmployees();
            }
            updateTableData(employees);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error applying filters: " + ex.getMessage(),
                "Filter Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployeeData() {
        try {
            List<EmployeeDTO> employees = employeeService.getAllEmployees();
            updateTableData(employees);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading employees: " + ex.getMessage(),
                "Load Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableData(List<EmployeeDTO> employees) {
        tableModel.setRowCount(0);
        for (EmployeeDTO employee : employees) {
            tableModel.addRow(new Object[]{
                employee.getEmployeeId(),
                employee.getName(),
                employee.getDepartment().getName(),
                employee.getJobTitle(),
                employee.getStatus(),
                employee.getHireDate(),
                employee.getEmail(),
                "Edit/Delete"
            });
        }
    }
}
