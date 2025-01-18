package com.employee.recordsystem.ui.dialogs;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.service.DepartmentService;
import com.employee.recordsystem.service.EmployeeService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeDialog extends JDialog {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private boolean approved = false;
    private EmployeeDTO employee;

    // Form components
    private JTextField employeeIdField;
    private JTextField fullNameField;
    private JTextField jobTitleField;
    private JComboBox<DepartmentDTO> departmentCombo;
    private JFormattedTextField hireDateField;
    private JComboBox<EmploymentStatus> statusCombo;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;

    // Validation labels
    private JLabel employeeIdValidation;
    private JLabel fullNameValidation;
    private JLabel emailValidation;
    private JLabel phoneValidation;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private static final Pattern EMPLOYEE_ID_PATTERN = Pattern.compile("^EMP\\d{6}$");

    public EmployeeDialog(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        
        setModal(true);
        setTitle("Employee Details");
        initializeUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        JPanel contentPanel = new JPanel(new MigLayout("fillx, insets 20", "[][grow][]", "[]10[]"));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize components
        employeeIdField = new JTextField(20);
        fullNameField = new JTextField(20);
        jobTitleField = new JTextField(20);
        departmentCombo = new JComboBox<>();
        hireDateField = new JFormattedTextField(DateTimeFormatter.ISO_LOCAL_DATE);
        statusCombo = new JComboBox<>(EmploymentStatus.values());
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addressArea = new JTextArea(3, 20);

        // Initialize validation labels
        employeeIdValidation = new JLabel();
        fullNameValidation = new JLabel();
        emailValidation = new JLabel();
        phoneValidation = new JLabel();

        // Style validation labels
        styleValidationLabel(employeeIdValidation);
        styleValidationLabel(fullNameValidation);
        styleValidationLabel(emailValidation);
        styleValidationLabel(phoneValidation);

        // Add form components with validation
        contentPanel.add(new JLabel("Employee ID:*"), "align label");
        contentPanel.add(employeeIdField, "growx");
        contentPanel.add(employeeIdValidation, "wrap");

        contentPanel.add(new JLabel("Full Name:*"), "align label");
        contentPanel.add(fullNameField, "growx");
        contentPanel.add(fullNameValidation, "wrap");

        contentPanel.add(new JLabel("Job Title:*"), "align label");
        contentPanel.add(jobTitleField, "growx, wrap");

        contentPanel.add(new JLabel("Department:*"), "align label");
        contentPanel.add(departmentCombo, "growx, wrap");

        contentPanel.add(new JLabel("Hire Date:*"), "align label");
        contentPanel.add(hireDateField, "growx, wrap");

        contentPanel.add(new JLabel("Status:*"), "align label");
        contentPanel.add(statusCombo, "growx, wrap");

        contentPanel.add(new JLabel("Email:*"), "align label");
        contentPanel.add(emailField, "growx");
        contentPanel.add(emailValidation, "wrap");

        contentPanel.add(new JLabel("Phone:*"), "align label");
        contentPanel.add(phoneField, "growx");
        contentPanel.add(phoneValidation, "wrap");

        contentPanel.add(new JLabel("Address:*"), "align label");
        contentPanel.add(new JScrollPane(addressArea), "growx, h 60!, wrap");

        // Add buttons
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[grow][][]", "[]"));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(new JLabel("* Required fields"), "align left");
        buttonPanel.add(saveButton, "align right");
        buttonPanel.add(cancelButton, "align right");

        contentPanel.add(buttonPanel, "span, growx, wrap");

        // Add listeners
        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> dispose());

        // Add real-time validation
        employeeIdField.getDocument().addDocumentListener(new ValidationDocumentListener(() -> 
            validateEmployeeId(employeeIdField.getText())));
        fullNameField.getDocument().addDocumentListener(new ValidationDocumentListener(() -> 
            validateFullName(fullNameField.getText())));
        emailField.getDocument().addDocumentListener(new ValidationDocumentListener(() -> 
            validateEmail(emailField.getText())));
        phoneField.getDocument().addDocumentListener(new ValidationDocumentListener(() -> 
            validatePhone(phoneField.getText())));

        // Load departments
        loadDepartments();

        // Add to dialog
        setContentPane(contentPanel);
    }

    private void styleValidationLabel(JLabel label) {
        label.setForeground(Color.RED);
        label.setFont(label.getFont().deriveFont(11f));
    }

    private boolean validateEmployeeId(String id) {
        if (!EMPLOYEE_ID_PATTERN.matcher(id).matches()) {
            employeeIdValidation.setText("Invalid format (EMP######)");
            return false;
        }
        employeeIdValidation.setText("");
        return true;
    }

    private boolean validateFullName(String name) {
        if (name.length() < 2 || name.length() > 100) {
            fullNameValidation.setText("2-100 characters required");
            return false;
        }
        fullNameValidation.setText("");
        return true;
    }

    private boolean validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            emailValidation.setText("Invalid email format");
            return false;
        }
        emailValidation.setText("");
        return true;
    }

    private boolean validatePhone(String phone) {
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            phoneValidation.setText("Invalid phone format");
            return false;
        }
        phoneValidation.setText("");
        return true;
    }

    private void loadDepartments() {
        try {
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            departmentCombo.setModel(new DefaultComboBoxModel<>(departments.toArray(new DepartmentDTO[0])));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading departments: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveEmployee() {
        if (!validateAll()) {
            JOptionPane.showMessageDialog(this,
                "Please correct the validation errors before saving.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setEmployeeId(employeeIdField.getText());
            dto.setFullName(fullNameField.getText());
            dto.setJobTitle(jobTitleField.getText());
            dto.setDepartmentId(((DepartmentDTO) departmentCombo.getSelectedItem()).getId());
            dto.setHireDate(LocalDate.parse(hireDateField.getText()));
            dto.setEmploymentStatus((EmploymentStatus) statusCombo.getSelectedItem());
            dto.setEmail(emailField.getText());
            dto.setPhone(phoneField.getText());
            dto.setAddress(addressArea.getText());

            if (employee != null) {
                dto.setId(employee.getId());
                employeeService.updateEmployee(employee.getId(), dto);
            } else {
                employeeService.createEmployee(dto);
            }

            approved = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error saving employee: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateAll() {
        return validateEmployeeId(employeeIdField.getText()) &&
               validateFullName(fullNameField.getText()) &&
               validateEmail(emailField.getText()) &&
               validatePhone(phoneField.getText()) &&
               !jobTitleField.getText().trim().isEmpty() &&
               departmentCombo.getSelectedItem() != null &&
               !hireDateField.getText().trim().isEmpty() &&
               statusCombo.getSelectedItem() != null &&
               !addressArea.getText().trim().isEmpty();
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
        employeeIdField.setText(employee.getEmployeeId());
        fullNameField.setText(employee.getFullName());
        jobTitleField.setText(employee.getJobTitle());
        hireDateField.setText(employee.getHireDate().toString());
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhone());
        addressArea.setText(employee.getAddress());
        statusCombo.setSelectedItem(employee.getEmploymentStatus());

        // Set department
        for (int i = 0; i < departmentCombo.getItemCount(); i++) {
            DepartmentDTO dept = departmentCombo.getItemAt(i);
            if (dept.getId().equals(employee.getDepartmentId())) {
                departmentCombo.setSelectedIndex(i);
                break;
            }
        }

        setTitle("Edit Employee - " + employee.getFullName());
    }

    public boolean isApproved() {
        return approved;
    }
}

class ValidationDocumentListener implements javax.swing.event.DocumentListener {
    private final Runnable validationTask;

    public ValidationDocumentListener(Runnable validationTask) {
        this.validationTask = validationTask;
    }

    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        validationTask.run();
    }

    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        validationTask.run();
    }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        validationTask.run();
    }
}
