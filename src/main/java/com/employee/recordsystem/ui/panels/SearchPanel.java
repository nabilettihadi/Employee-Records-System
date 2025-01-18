package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.service.DepartmentService;
import com.employee.recordsystem.service.EmployeeService;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class SearchPanel extends JPanel {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    
    private JTextField nameField;
    private JTextField idField;
    private JComboBox<String> departmentCombo;
    private JTextField jobTitleField;
    private JComboBox<EmploymentStatus> statusCombo;
    private JDateChooser hireDateFrom;
    private JDateChooser hireDateTo;

    public SearchPanel(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("fillx, insets 10", "[][grow][]", "[][]"));
        
        // Name search
        add(new JLabel("Name:"));
        nameField = new JTextField(20);
        add(nameField, "growx");
        
        // Employee ID search
        add(new JLabel("ID:"));
        idField = new JTextField(10);
        add(idField, "growx, wrap");
        
        // Department search
        add(new JLabel("Department:"));
        departmentCombo = new JComboBox<>();
        loadDepartments();
        add(departmentCombo, "growx");
        
        // Job Title search
        add(new JLabel("Job Title:"));
        jobTitleField = new JTextField(20);
        add(jobTitleField, "growx, wrap");
        
        // Employment Status
        add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(EmploymentStatus.values());
        add(statusCombo, "growx");
        
        // Hire Date Range
        JPanel datePanel = new JPanel(new MigLayout("", "[][grow][]", "[]"));
        datePanel.add(new JLabel("Hire Date:"));
        hireDateFrom = new JDateChooser();
        hireDateTo = new JDateChooser();
        datePanel.add(hireDateFrom, "growx");
        datePanel.add(new JLabel("to"));
        datePanel.add(hireDateTo, "growx");
        add(datePanel, "span 2, growx, wrap");
        
        // Search Button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        add(searchButton, "span, align center");
    }

    private void loadDepartments() {
        departmentCombo.removeAllItems();
        departmentCombo.addItem("All Departments");
        departmentService.getAllDepartments().forEach(dept ->
            departmentCombo.addItem(dept.getName())
        );
    }

    private void performSearch() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String department = departmentCombo.getSelectedItem().toString();
        String jobTitle = jobTitleField.getText().trim();
        EmploymentStatus status = (EmploymentStatus) statusCombo.getSelectedItem();
        
        LocalDate fromDate = null;
        LocalDate toDate = null;
        
        if (hireDateFrom.getDate() != null) {
            fromDate = hireDateFrom.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        }
        
        if (hireDateTo.getDate() != null) {
            toDate = hireDateTo.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        }
        
        // Trigger search in EmployeeService
        employeeService.searchEmployees(name, id, department, jobTitle, status, fromDate, toDate);
    }
}
