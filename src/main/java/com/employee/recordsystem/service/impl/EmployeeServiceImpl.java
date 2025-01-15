package com.employee.recordsystem.service.impl;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Employee;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.repository.EmployeeRepository;
import com.employee.recordsystem.repository.DepartmentRepository;
import com.employee.recordsystem.service.EmployeeService;
import com.employee.recordsystem.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmployeeId(employeeDTO.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }

        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        auditService.logAction("EMPLOYEE", savedEmployee.getId(), "CREATE", "Employee created");
        
        return convertToDTO(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(String employeeId, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        
        updateEmployeeFields(employee, employeeDTO);
        Employee updatedEmployee = employeeRepository.save(employee);
        auditService.logAction("EMPLOYEE", updatedEmployee.getId(), "UPDATE", "Employee updated");
        
        return convertToDTO(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        
        employeeRepository.delete(employee);
        auditService.logAction("EMPLOYEE", employee.getId(), "DELETE", "Employee deleted");
    }

    @Override
    public EmployeeDTO getEmployeeById(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
            .map(this::convertToDTO)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> searchEmployees(String searchTerm) {
        return employeeRepository.searchEmployees(searchTerm).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByStatus(EmploymentStatus status) {
        return employeeRepository.findByEmploymentStatus(status).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByHireDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isEmployeeIdUnique(String employeeId) {
        return !employeeRepository.existsByEmployeeId(employeeId);
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        updateEmployeeFields(employee, dto);
        return employee;
    }

    private void updateEmployeeFields(Employee employee, EmployeeDTO dto) {
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setFullName(dto.getFullName());
        employee.setJobTitle(dto.getJobTitle());
        employee.setHireDate(dto.getHireDate());
        employee.setEmploymentStatus(dto.getEmploymentStatus());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
            employee.setDepartment(department);
        }
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFullName(employee.getFullName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setHireDate(employee.getHireDate());
        dto.setEmploymentStatus(employee.getEmploymentStatus());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setAddress(employee.getAddress());
        
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        
        return dto;
    }
}
