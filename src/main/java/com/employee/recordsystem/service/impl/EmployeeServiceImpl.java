package com.employee.recordsystem.service.impl;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.Employee;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.repository.DepartmentRepository;
import com.employee.recordsystem.repository.EmployeeRepository;
import com.employee.recordsystem.service.AuditService;
import com.employee.recordsystem.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmployeeId(employeeDTO.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }

        Employee employee = mapToEntity(employeeDTO);
        employee = employeeRepository.save(employee);
        auditService.logAction("Employee", employee.getId(), "CREATE", "Created new employee");
        
        return mapToDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        updateEmployeeFields(employee, employeeDTO);
        employee = employeeRepository.save(employee);
        auditService.logAction("Employee", employee.getId(), "UPDATE", "Updated employee details");

        return mapToDTO(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
            
        employeeRepository.delete(employee);
        auditService.logAction("Employee", id, "DELETE", "Deleted employee");
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    @Override
    public List<EmployeeDTO> findEmployees(String name, String employeeId, Long departmentId, 
            String jobTitle, EmploymentStatus status, LocalDate hireDateFrom, LocalDate hireDateTo) {
        // Implementation of search with criteria
        return employeeRepository.findAll().stream()
            .filter(employee -> matchesSearchCriteria(employee, name, employeeId, departmentId, 
                jobTitle, status, hireDateFrom, hireDateTo))
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        
        return employeeRepository.findByDepartment(department).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> searchEmployees(String query, Long departmentId, EmploymentStatus status) {
        List<Employee> employees = employeeRepository.findAll();
        
        return employees.stream()
            .filter(employee -> matchesSearchQuery(employee, query, departmentId, status))
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> searchByName(String name) {
        return employeeRepository.findByFullNameContainingIgnoreCase(name)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> searchById(String employeeId) {
        return employeeRepository.findByEmployeeIdContainingIgnoreCase(employeeId)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> searchByDepartment(String departmentName) {
        return employeeRepository.findByDepartment_NameContainingIgnoreCase(departmentName)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> searchByJobTitle(String jobTitle) {
        return employeeRepository.findByJobTitleContainingIgnoreCase(jobTitle)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private void updateEmployeeFields(Employee employee, EmployeeDTO dto) {
        employee.setFullName(dto.getFullName());
        employee.setJobTitle(dto.getJobTitle());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
        employee.setEmploymentStatus(dto.getEmploymentStatus());
        employee.setHireDate(convertToDateTime(dto.getHireDate()));
        
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
            employee.setDepartment(department);
        }
    }

    private Employee mapToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setFullName(dto.getFullName());
        employee.setJobTitle(dto.getJobTitle());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
        employee.setEmploymentStatus(dto.getEmploymentStatus());
        employee.setHireDate(convertToDateTime(dto.getHireDate()));
        
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
            employee.setDepartment(department);
        }
        
        return employee;
    }

    private EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFullName(employee.getFullName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setAddress(employee.getAddress());
        dto.setEmploymentStatus(employee.getEmploymentStatus());
        dto.setHireDate(convertToDate(employee.getHireDate()));
        
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        
        return dto;
    }

    // Helper methods for date conversion
    private LocalDateTime convertToDateTime(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }

    private LocalDate convertToDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }

    private boolean matchesSearchCriteria(Employee employee, String name, String employeeId,
            Long departmentId, String jobTitle, EmploymentStatus status, 
            LocalDate hireDateFrom, LocalDate hireDateTo) {
        
        LocalDateTime hireDateTime = employee.getHireDate();
        LocalDate hireDate = hireDateTime != null ? hireDateTime.toLocalDate() : null;
        
        return (name == null || employee.getFullName().toLowerCase().contains(name.toLowerCase())) &&
               (employeeId == null || employee.getEmployeeId().toLowerCase().contains(employeeId.toLowerCase())) &&
               (departmentId == null || employee.getDepartment().getId().equals(departmentId)) &&
               (jobTitle == null || employee.getJobTitle().toLowerCase().contains(jobTitle.toLowerCase())) &&
               (status == null || employee.getEmploymentStatus() == status) &&
               (hireDateFrom == null || (hireDate != null && !hireDate.isBefore(hireDateFrom))) &&
               (hireDateTo == null || (hireDate != null && !hireDate.isAfter(hireDateTo)));
    }

    private boolean matchesSearchQuery(Employee employee, String query, Long departmentId, EmploymentStatus status) {
        boolean matchesQuery = query == null || 
            employee.getFullName().toLowerCase().contains(query.toLowerCase()) ||
            employee.getEmployeeId().toLowerCase().contains(query.toLowerCase()) ||
            employee.getJobTitle().toLowerCase().contains(query.toLowerCase());
            
        boolean matchesDepartment = departmentId == null || 
            (employee.getDepartment() != null && employee.getDepartment().getId().equals(departmentId));
            
        boolean matchesStatus = status == null || employee.getEmploymentStatus() == status;
        
        return matchesQuery && matchesDepartment && matchesStatus;
    }
}
