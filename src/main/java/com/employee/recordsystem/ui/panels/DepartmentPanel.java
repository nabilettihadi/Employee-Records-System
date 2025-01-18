package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.service.DepartmentService;
import com.employee.recordsystem.ui.dialogs.DeleteConfirmationDialog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DepartmentPanel extends JPanel {
    private final DepartmentService departmentService;
    private JTable departmentTable;
    private DefaultTableModel tableModel;
    private List<DepartmentDTO> departments;

    public void initializeUI() {
        setLayout(new BorderLayout());

        // Create the table model
        String[] columnNames = {"ID", "Name", "Description", "Location", "Manager", "Employee Count"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and configure the table
        departmentTable = new JTable(tableModel);
        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        departmentTable.getTableHeader().setReorderingAllowed(false);

        // Create the toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // Add buttons
        JButton addButton = new JButton("Add Department");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(refreshButton);

        // Add action listeners
        addButton.addActionListener(e -> showAddDepartmentDialog());
        editButton.addActionListener(e -> showEditDepartmentDialog());
        deleteButton.addActionListener(e -> deleteSelectedDepartment());
        refreshButton.addActionListener(e -> refreshDepartmentList());

        // Add components to panel
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(departmentTable), BorderLayout.CENTER);

        // Initial load of data
        refreshDepartmentList();
    }

    private void showAddDepartmentDialog() {
        DepartmentDTO newDepartment = new DepartmentDTO();
        if (showDepartmentDialog(newDepartment, true)) {
            try {
                departmentService.createDepartment(newDepartment);
                refreshDepartmentList();
                JOptionPane.showMessageDialog(this,
                    "Department created successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error creating department: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDepartmentDialog() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a department to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        DepartmentDTO department = departments.get(selectedRow);
        if (showDepartmentDialog(department, false)) {
            try {
                departmentService.updateDepartment(department.getId(), department);
                refreshDepartmentList();
                JOptionPane.showMessageDialog(this,
                    "Department updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error updating department: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a department to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        DepartmentDTO department = departments.get(selectedRow);
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            department.getName());

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                departmentService.deleteDepartment(department.getId());
                refreshDepartmentList();
                JOptionPane.showMessageDialog(this,
                    "Department deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting department: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean showDepartmentDialog(DepartmentDTO department, boolean isNew) {
        // Create dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            isNew ? "Add Department" : "Edit Department",
            true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField nameField = new JTextField(department.getName());
        JTextField descriptionField = new JTextField(department.getDescription());
        JTextField locationField = new JTextField(department.getLocation());

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add button actions
        final boolean[] result = {false};

        saveButton.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Department name is required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            department.setName(nameField.getText().trim());
            department.setDescription(descriptionField.getText().trim());
            department.setLocation(locationField.getText().trim());
            result[0] = true;
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        // Show dialog
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return result[0];
    }

    private void refreshDepartmentList() {
        try {
            departments = departmentService.getAllDepartments();
            updateTableModel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading departments: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableModel() {
        tableModel.setRowCount(0);
        for (DepartmentDTO department : departments) {
            tableModel.addRow(new Object[]{
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getLocation(),
                department.getManagerName(),
                department.getEmployeeCount()
            });
        }
    }
}
