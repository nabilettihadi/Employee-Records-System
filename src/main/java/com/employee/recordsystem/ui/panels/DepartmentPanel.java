package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.ui.service.DepartmentService;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class DepartmentPanel extends JPanel {
    
    private final JTable departmentTable;
    private final DefaultTableModel tableModel;
    private final DepartmentService departmentService;

    public DepartmentPanel() {
        setLayout(new MigLayout("fill", "[grow]", "[]10[]10[grow]"));
        this.departmentService = new DepartmentService();
        
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
            try {
                DepartmentDTO newDepartment = new DepartmentDTO();
                newDepartment.setName(nameField.getText());
                newDepartment.setLocation(locationField.getText());
                
                departmentService.createDepartment(newDepartment);
                loadDepartmentData();
                dialog.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error creating department: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.add(saveButton, "span 2, center");
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadDepartmentData() {
        try {
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            updateTableData(departments);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading departments: " + ex.getMessage(),
                "Load Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableData(List<DepartmentDTO> departments) {
        tableModel.setRowCount(0);
        for (DepartmentDTO department : departments) {
            tableModel.addRow(new Object[]{
                department.getId(),
                department.getName(),
                department.getManager() != null ? department.getManager().getName() : "Not Assigned",
                department.getEmployeeCount(),
                department.getLocation(),
                department.getCreatedDate(),
                "Edit/Delete"
            });
        }
    }
}
