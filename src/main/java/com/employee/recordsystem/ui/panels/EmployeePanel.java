package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.service.DepartmentService;
import com.employee.recordsystem.service.EmployeeService;
import com.employee.recordsystem.ui.dialogs.DeleteConfirmationDialog;
import com.employee.recordsystem.ui.dialogs.EmployeeDialog;
import lombok.RequiredArgsConstructor;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class EmployeePanel extends JPanel {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private List<EmployeeDTO> employees;

    public EmployeePanel(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        initializeUI();
    }

    public void initializeUI() {
        // Use MigLayout for better component organization
        setLayout(new MigLayout(
            "fill, insets 10",
            "[grow]",
            "[][][grow][]"
        ));

        // Create search panel
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, "wrap, growx");

        // Create the toolbar with better styling
        JToolBar toolBar = createToolBar();
        add(toolBar, "wrap, growx");

        // Create and configure the table with better styling
        createTable();
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 0, 5, 0),
            BorderFactory.createLineBorder(Color.GRAY)
        ));
        add(scrollPane, "grow, wrap");

        // Add status bar
        JPanel statusBar = createStatusBar();
        add(statusBar, "growx");

        // Initial load of data
        refreshEmployeeList();
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx, insets 0", "[][][grow][]"));
        
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JComboBox<String> searchType = new JComboBox<>(new String[]{"Name", "ID", "Department", "Job Title"});
        JButton searchButton = new JButton("Search");

        panel.add(searchLabel);
        panel.add(searchType);
        panel.add(searchField, "growx");
        panel.add(searchButton);

        searchButton.addActionListener(e -> performSearch(searchField.getText(), (String)searchType.getSelectedItem()));
        searchField.addActionListener(e -> performSearch(searchField.getText(), (String)searchType.getSelectedItem()));

        return panel;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setOpaque(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create buttons with icons and tooltips
        JButton addButton = new JButton("Add Employee");
        addButton.setToolTipText("Add a new employee");
        
        JButton editButton = new JButton("Edit");
        editButton.setToolTipText("Edit selected employee");
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.setToolTipText("Delete selected employee");
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setToolTipText("Refresh employee list");

        // Add buttons with spacing
        toolBar.add(addButton);
        toolBar.addSeparator(new Dimension(5, 0));
        toolBar.add(editButton);
        toolBar.addSeparator(new Dimension(5, 0));
        toolBar.add(deleteButton);
        toolBar.addSeparator(new Dimension(5, 0));
        toolBar.add(refreshButton);

        // Add action listeners
        addButton.addActionListener(e -> showAddEmployeeDialog());
        editButton.addActionListener(e -> showEditEmployeeDialog());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        refreshButton.addActionListener(e -> refreshEmployeeList());

        return toolBar;
    }

    private void createTable() {
        String[] columnNames = {"ID", "Name", "Department", "Job Title", "Status", "Email", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setRowHeight(25);
        employeeTable.setIntercellSpacing(new Dimension(10, 0));
        employeeTable.setShowGrid(true);
        employeeTable.setGridColor(Color.LIGHT_GRAY);

        // Add better column sizing
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Department
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Job Title
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Status
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Email
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Phone
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new MigLayout("fillx, insets 2", "[grow][]"));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel statusLabel = new JLabel("Ready");
        JLabel recordCountLabel = new JLabel("0 records");
        
        statusBar.add(statusLabel, "grow");
        statusBar.add(recordCountLabel);
        
        return statusBar;
    }

    private void performSearch(String searchText, String searchType) {
        if (searchText == null || searchText.trim().isEmpty()) {
            refreshEmployeeList();
            return;
        }

        try {
            List<EmployeeDTO> searchResults = switch (searchType) {
                case "Name" -> employeeService.searchByName(searchText);
                case "ID" -> employeeService.searchById(searchText);
                case "Department" -> employeeService.searchByDepartment(searchText);
                case "Job Title" -> employeeService.searchByJobTitle(searchText);
                default -> employeeService.getAllEmployees();
            };
            updateTableData(searchResults);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error performing search: " + e.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddEmployeeDialog() {
        EmployeeDialog dialog = new EmployeeDialog(employeeService, departmentService);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (dialog.isApproved()) {
            refreshEmployeeList();
        }
    }

    private void showEditEmployeeDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an employee to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        EmployeeDTO employee = employees.get(selectedRow);
        EmployeeDialog dialog = new EmployeeDialog(employeeService, departmentService);
        dialog.setEmployee(employee);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (dialog.isApproved()) {
            refreshEmployeeList();
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an employee to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        EmployeeDTO employee = employees.get(selectedRow);
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            employee.getFullName());

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                employeeService.deleteEmployee(employee.getId());
                refreshEmployeeList();
                JOptionPane.showMessageDialog(this,
                    "Employee deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting employee: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshEmployeeList() {
        try {
            employees = employeeService.findEmployees(null, null, null, null, null, null, null);
            updateTableData(employees);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading employees: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableData(List<EmployeeDTO> employees) {
        tableModel.setRowCount(0);
        for (EmployeeDTO employee : employees) {
            tableModel.addRow(new Object[]{
                employee.getId(),
                employee.getFullName(),
                employee.getDepartmentName(),
                employee.getJobTitle(),
                employee.getEmploymentStatus(),
                employee.getEmail(),
                employee.getPhone()
            });
        }
    }
}
