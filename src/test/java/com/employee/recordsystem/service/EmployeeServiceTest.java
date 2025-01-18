package com.employee.recordsystem.service;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.Employee;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.repository.EmployeeRepository;
import com.employee.recordsystem.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee testEmployee;
    private EmployeeDTO testEmployeeDTO;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("IT");

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setEmployeeId("EMP123456");
        testEmployee.setFullName("John Doe");
        testEmployee.setJobTitle("Software Engineer");
        testEmployee.setDepartment(testDepartment);
        testEmployee.setHireDate(LocalDateTime.now().minusDays(1));
        testEmployee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        testEmployee.setEmail("john.doe@company.com");
        testEmployee.setPhone("+1234567890");
        testEmployee.setAddress("123 Main St");

        testEmployeeDTO = new EmployeeDTO();
        testEmployeeDTO.setEmployeeId("EMP123456");
        testEmployeeDTO.setFullName("John Doe");
        testEmployeeDTO.setJobTitle("Software Engineer");
        testEmployeeDTO.setDepartmentId(1L);
        testEmployeeDTO.setHireDate(LocalDate.now().minusDays(1));
        testEmployeeDTO.setEmploymentStatus(EmploymentStatus.ACTIVE);
        testEmployeeDTO.setEmail("john.doe@company.com");
        testEmployeeDTO.setPhone("+1234567890");
        testEmployeeDTO.setAddress("123 Main St");
    }

    @Test
    void createEmployee_ValidData_Success() {
        when(departmentService.getDepartmentById(1L)).thenReturn(testDepartment);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        EmployeeDTO result = employeeService.createEmployee(testEmployeeDTO);

        assertNotNull(result);
        assertEquals(testEmployeeDTO.getEmployeeId(), result.getEmployeeId());
        assertEquals(testEmployeeDTO.getFullName(), result.getFullName());
        verify(auditService).logAction(eq("Employee"), any(), eq("CREATE"), any());
    }

    @Test
    void updateEmployee_ValidData_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(departmentService.getDepartmentById(1L)).thenReturn(testDepartment);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        testEmployeeDTO.setId(1L);
        EmployeeDTO result = employeeService.updateEmployee(1L, testEmployeeDTO);

        assertNotNull(result);
        assertEquals(testEmployeeDTO.getFullName(), result.getFullName());
        verify(auditService).logAction(eq("Employee"), eq(1L), eq("UPDATE"), any());
    }

    @Test
    void deleteEmployee_ExistingEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(testEmployee);
        verify(auditService).logAction(eq("Employee"), eq(1L), eq("DELETE"), any());
    }

    @Test
    void getEmployeeById_ExistingEmployee_ReturnsEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(testEmployee.getEmployeeId(), result.getEmployeeId());
    }

    @Test
    void searchEmployees_ValidCriteria_ReturnsFilteredList() {
        // Test implementation for search functionality
        String searchName = "John";
        String departmentName = "IT";
        EmploymentStatus status = EmploymentStatus.ACTIVE;

        when(employeeRepository.findByFullNameContainingAndDepartment_NameAndEmploymentStatus(
            searchName, departmentName, status
        )).thenReturn(java.util.Collections.singletonList(testEmployee));

        List<EmployeeDTO> results = employeeService.searchEmployees(searchName, null, departmentName, null, status, null, null);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(testEmployee.getFullName(), results.get(0).getFullName());
    }
}
