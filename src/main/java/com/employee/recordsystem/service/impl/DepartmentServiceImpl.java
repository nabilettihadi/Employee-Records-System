package com.employee.recordsystem.service.impl;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.Employee;
import com.employee.recordsystem.repository.DepartmentRepository;
import com.employee.recordsystem.repository.EmployeeRepository;
import com.employee.recordsystem.service.DepartmentService;
import com.employee.recordsystem.service.AuditService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepository.existsByName(departmentDTO.getName())) {
            throw new IllegalArgumentException("Department name already exists");
        }

        Department department = convertToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        auditService.logAction("DEPARTMENT", savedDepartment.getId(), "CREATE", "Department created");
        
        return convertToDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        
        if (!department.getName().equals(departmentDTO.getName()) && 
            departmentRepository.existsByName(departmentDTO.getName())) {
            throw new IllegalArgumentException("Department name already exists");
        }

        updateDepartmentFields(department, departmentDTO);
        Department updatedDepartment = departmentRepository.save(department);
        auditService.logAction("DEPARTMENT", updatedDepartment.getId(), "UPDATE", "Department updated");
        
        return convertToDTO(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        
        departmentRepository.delete(department);
        auditService.logAction("DEPARTMENT", id, "DELETE", "Department deleted");
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new EntityNotFoundException("Department not found"));
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO assignManager(Long departmentId, String employeeId) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        
        Employee manager = employeeRepository.findByEmployeeId(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        
        department.setManager(manager);
        Department updatedDepartment = departmentRepository.save(department);
        auditService.logAction("DEPARTMENT", departmentId, "UPDATE", 
            "Manager assigned: " + manager.getFullName());
        
        return convertToDTO(updatedDepartment);
    }

    @Override
    public boolean isDepartmentNameUnique(String name) {
        return !departmentRepository.existsByName(name);
    }

    private Department convertToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        return department;
    }

    private void updateDepartmentFields(Department department, DepartmentDTO dto) {
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
    }

    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        
        if (department.getManager() != null) {
            dto.setManagerId(department.getManager().getId());
            dto.setManagerName(department.getManager().getFullName());
        }
        
        if (department.getEmployees() != null) {
            dto.setEmployees(department.getEmployees().stream()
                .map(this::convertEmployeeToDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private EmployeeDTO convertEmployeeToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFullName(employee.getFullName());
        dto.setJobTitle(employee.getJobTitle());
        return dto;
    }
}
