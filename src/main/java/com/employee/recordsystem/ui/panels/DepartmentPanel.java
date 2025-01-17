package com.employee.recordsystem.ui.panels;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DepartmentPanel extends JPanel {
    
    private final JTable departmentTable;
    private final DefaultTableModel tableModel;

    public DepartmentPanel() {
        setLayout(new MigLayout("fill", "[grow]", "[]10[]10[grow]"));
        
        // Create toolbar panel
        JPanel toolbarPanel = new JPanel(new MigLayout("fillx", "[]push[]", "[]"));
        
        // Add button
        JButton addButton = new JButton("Add Department");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        
        toolbarPanel.add(addButton);
        
        // Create table
        String[] columns = {
            "ID", "Name", "Manager", "Employee Count", 
            "Location", "Created Date", "Actions"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == columns.length - 1; // Only actions column is editable
            }
        };
        departmentTable = new JTable(tableModel);
        departmentTable.setFillsViewportHeight(true);
        
        // Style table
        departmentTable.setRowHeight(30);
        departmentTable.getTableHeader().setBackground(new Color(240, 240, 240));
        departmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add components to main panel
        add(createTitlePanel(), "growx, wrap");
        add(toolbarPanel, "growx, wrap");
        add(new JScrollPane(departmentTable), "grow");
        
        // Add listeners
        addButton.addActionListener(e -> showAddDepartmentDialog());
        
        // Load initial data
        loadDepartmentData();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new MigLayout("fillx", "[]push[]", "[]"));
        JLabel titleLabel = new JLabel("Department Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private void showAddDepartmentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Department", true);
        dialog.setLayout(new MigLayout("fillx", "[][]", "[]10[]10[]"));
        
        JTextField nameField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        
        dialog.add(new JLabel("Name:"), "right");
        dialog.add(nameField, "growx, wrap");
        dialog.add(new JLabel("Location:"), "right");
        dialog.add(locationField, "growx, wrap");
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // TODO: Implement save functionality
            dialog.dispose();
        });
        
        dialog.add(saveButton, "span 2, center");
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadDepartmentData() {
        // TODO: Load actual data from service
        // For now, add some sample data
        Object[][] sampleData = {
            {"DEPT001", "Information Technology", "John Doe", "25", "Building A", "2024-01-01", "Edit/Delete"},
            {"DEPT002", "Human Resources", "Jane Smith", "10", "Building B", "2024-01-01", "Edit/Delete"}
        };
        
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
}
