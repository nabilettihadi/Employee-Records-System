package com.employee.recordsystem.service;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(String employeeId, EmployeeDTO employeeDTO);
    void deleteEmployee(String employeeId);
    EmployeeDTO getEmployeeById(String employeeId);
    List<EmployeeDTO> getAllEmployees();
    List<EmployeeDTO> searchEmployees(String searchTerm);
    List<EmployeeDTO> getEmployeesByDepartment(Long departmentId);
    List<EmployeeDTO> getEmployeesByStatus(EmploymentStatus status);
    List<EmployeeDTO> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate);
    boolean isEmployeeIdUnique(String employeeId);
}
