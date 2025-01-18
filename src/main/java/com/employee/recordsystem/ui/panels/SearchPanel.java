package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.service.DepartmentService;
import com.employee.recordsystem.service.EmployeeService;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class SearchPanel extends JPanel {
    
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    
    private JTextField searchField;
    private JComboBox<Department> departmentComboBox;
    private JComboBox<EmploymentStatus> statusComboBox;
    private JTable resultsTable;

    public SearchPanel(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("fillx, wrap 2", "[right][grow,fill]", "[]10[]"));
        
        // Search field
        add(new JLabel("Search:"));
        searchField = new JTextField(20);
        add(searchField);
        
        // Department dropdown
        add(new JLabel("Department:"));
        departmentComboBox = new JComboBox<>();
        add(departmentComboBox);
        
        // Status dropdown
        add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(EmploymentStatus.values());
        add(statusComboBox);
        
        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        add(searchButton, "span, align center");
        
        // Results table
        String[] columns = {"ID", "Name", "Job Title", "Department", "Status", "Hire Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        resultsTable = new JTable(model);
        add(new JScrollPane(resultsTable), "span, grow");
        
        loadDepartments();
    }

    private void loadDepartments() {
        departmentComboBox.removeAllItems();
        departmentComboBox.addItem(new Department(null, "All Departments"));
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        for (DepartmentDTO dept : departments) {
            departmentComboBox.addItem(new Department(dept.getId(), dept.getName()));
        }
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        Department selectedDepartment = (Department) departmentComboBox.getSelectedItem();
        Long departmentId = selectedDepartment != null ? selectedDepartment.getId() : null;
        EmploymentStatus status = (EmploymentStatus) statusComboBox.getSelectedItem();
        
        try {
            List<EmployeeDTO> results = employeeService.searchEmployees(searchTerm, departmentId, status);
            updateResultsTable(results);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error performing search: " + e.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateResultsTable(List<EmployeeDTO> employees) {
        DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
        model.setRowCount(0);
        
        for (EmployeeDTO employee : employees) {
            model.addRow(new Object[]{
                employee.getEmployeeId(),
                employee.getFullName(),
                employee.getJobTitle(),
                employee.getDepartmentName(),
                employee.getEmploymentStatus(),
                employee.getHireDate()
            });
        }
    }
}
