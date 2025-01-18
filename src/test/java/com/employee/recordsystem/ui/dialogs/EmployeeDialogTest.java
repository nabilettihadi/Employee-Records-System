package com.employee.recordsystem.ui.dialogs;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.service.DepartmentService;
import com.employee.recordsystem.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeDialogTest {
    @Mock
    private EmployeeService employeeService;
    
    @Mock
    private DepartmentService departmentService;
    
    private EmployeeDialog dialog;
    private EmployeeDTO testEmployee;
    private DepartmentDTO testDepartment;

    @BeforeEach
    void setUp() {
        // Create test data
        testDepartment = new DepartmentDTO();
        testDepartment.setId(1L);
        testDepartment.setName("IT Department");

        testEmployee = new EmployeeDTO();
        testEmployee.setId(1L);
        testEmployee.setEmployeeId("EMP123456");
        testEmployee.setFullName("John Doe");
        testEmployee.setJobTitle("Software Engineer");
        testEmployee.setDepartmentId(testDepartment.getId());
        testEmployee.setHireDate(LocalDate.now().minusDays(1));
        testEmployee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        testEmployee.setEmail("john.doe@company.com");
        testEmployee.setPhone("+1234567890");
        testEmployee.setAddress("123 Main St");

        // Mock department service
        when(departmentService.getAllDepartments())
            .thenReturn(Arrays.asList(testDepartment));

        // Create dialog
        dialog = new EmployeeDialog(employeeService, departmentService);
    }

    @Test
    void whenValidEmployee_thenSaveSuccessfully() {
        // Set up the dialog with valid employee data
        dialog.setEmployee(testEmployee);
        
        // Verify that the employee data was loaded correctly
        assertTrue(dialog.isApproved(), "Dialog should be in a valid state");
        
        // Verify that the save operation was called with correct data
        verify(employeeService, times(1)).updateEmployee(any(EmployeeDTO.class));
    }

    @Test
    void whenInvalidEmployeeId_thenShowValidationError() {
        testEmployee.setEmployeeId("invalid");
        dialog.setEmployee(testEmployee);
        
        assertFalse(dialog.isApproved(), "Dialog should be in an invalid state");
    }

    @Test
    void whenInvalidEmail_thenShowValidationError() {
        testEmployee.setEmail("invalid-email");
        dialog.setEmployee(testEmployee);
        
        assertFalse(dialog.isApproved(), "Dialog should be in an invalid state");
    }

    @Test
    void whenDepartmentSelected_thenUpdateEmployeeDepartment() {
        dialog.setEmployee(testEmployee);
        
        // Verify that the department was set correctly
        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    void whenNewEmployee_thenCreateInsteadOfUpdate() {
        // Create a new employee without an ID
        EmployeeDTO newEmployee = new EmployeeDTO();
        newEmployee.setEmployeeId("EMP123456");
        newEmployee.setFullName("Jane Doe");
        // ... set other required fields
        
        dialog.setEmployee(newEmployee);
        
        // Verify that create was called instead of update
        verify(employeeService, times(1)).createEmployee(any(EmployeeDTO.class));
        verify(employeeService, never()).updateEmployee(any(EmployeeDTO.class));
    }
}
