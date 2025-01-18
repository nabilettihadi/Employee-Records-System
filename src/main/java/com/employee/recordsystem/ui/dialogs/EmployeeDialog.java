package com.employee.recordsystem.ui.dialogs;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.ui.service.DepartmentService;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class EmployeeDialog extends JDialog {
    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField jobTitleField;
    private final JComboBox<DepartmentDTO> departmentCombo;
    private final JComboBox<EmploymentStatus> statusCombo;
    private final JTextField hireDateField;
    private EmployeeDTO employee;
    private boolean approved = false;

    public EmployeeDialog(Frame owner, String title, EmployeeDTO employee) {
        super(owner, title, true);
        this.employee = employee;

        setLayout(new MigLayout("fillx", "[][]", "[]10[]10[]10[]10[]10[]10[]"));

        // Initialize components
        idField = new JTextField(20);
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        jobTitleField = new JTextField(20);
        departmentCombo = new JComboBox<>();
        statusCombo = new JComboBox<>(EmploymentStatus.values());
        hireDateField = new JTextField(20);

        // Load departments
        loadDepartments();

        // Add components
        add(new JLabel("Employee ID:"), "right");
        add(idField, "growx, wrap");
        add(new JLabel("Name:"), "right");
        add(nameField, "growx, wrap");
        add(new JLabel("Email:"), "right");
        add(emailField, "growx, wrap");
        add(new JLabel("Job Title:"), "right");
        add(jobTitleField, "growx, wrap");
        add(new JLabel("Department:"), "right");
        add(departmentCombo, "growx, wrap");
        add(new JLabel("Status:"), "right");
        add(statusCombo, "growx, wrap");
        add(new JLabel("Hire Date:"), "right");
        add(hireDateField, "growx, wrap");

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, "span 2, center");

        // Add actions
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                updateEmployeeFromFields();
                approved = true;
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());

        // Set initial values if editing
        if (employee != null) {
            populateFields();
        }

        // Final setup
        pack();
        setLocationRelativeTo(owner);
    }

    private void loadDepartments() {
        try {
            DepartmentService departmentService = new DepartmentService();
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            for (DepartmentDTO dept : departments) {
                departmentCombo.addItem(dept);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading departments: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFields() {
        idField.setText(employee.getEmployeeId());
        nameField.setText(employee.getName());
        emailField.setText(employee.getEmail());
        jobTitleField.setText(employee.getJobTitle());
        statusCombo.setSelectedItem(employee.getStatus());
        hireDateField.setText(employee.getHireDate().toString());
        
        // Find and select the matching department
        for (int i = 0; i < departmentCombo.getItemCount(); i++) {
            DepartmentDTO dept = departmentCombo.getItemAt(i);
            if (dept.getId().equals(employee.getDepartment().getId())) {
                departmentCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Name is required");
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showError("Email is required");
            return false;
        }
        if (jobTitleField.getText().trim().isEmpty()) {
            showError("Job Title is required");
            return false;
        }
        if (departmentCombo.getSelectedItem() == null) {
            showError("Department is required");
            return false;
        }
        try {
            LocalDate.parse(hireDateField.getText());
        } catch (Exception e) {
            showError("Invalid hire date format (use YYYY-MM-DD)");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void updateEmployeeFromFields() {
        if (employee == null) {
            employee = new EmployeeDTO();
        }
        employee.setEmployeeId(idField.getText());
        employee.setName(nameField.getText());
        employee.setEmail(emailField.getText());
        employee.setJobTitle(jobTitleField.getText());
        employee.setDepartment((DepartmentDTO) departmentCombo.getSelectedItem());
        employee.setStatus((EmploymentStatus) statusCombo.getSelectedItem());
        employee.setHireDate(LocalDate.parse(hireDateField.getText()));
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public boolean isApproved() {
        return approved;
    }
}
