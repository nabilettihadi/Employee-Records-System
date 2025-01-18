package com.employee.recordsystem.service;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
    EmployeeDTO getEmployeeById(Long id);
    
    // Search methods
    List<EmployeeDTO> searchByName(String name);
    List<EmployeeDTO> searchById(String employeeId);
    List<EmployeeDTO> searchByDepartment(String departmentName);
    List<EmployeeDTO> searchByJobTitle(String jobTitle);
    List<EmployeeDTO> getAllEmployees();
    
    // Existing search methods
    List<EmployeeDTO> findEmployees(String name, String employeeId, Long departmentId, 
            String jobTitle, EmploymentStatus status, LocalDate hireDateFrom, LocalDate hireDateTo);
    List<EmployeeDTO> searchEmployees(String query, Long departmentId, EmploymentStatus status);
    List<EmployeeDTO> getEmployeesByDepartment(Long departmentId);
}
