package com.employee.recordsystem.mapper;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Employee;
import com.employee.recordsystem.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final DepartmentService departmentService;

    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFullName(employee.getFullName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setDepartmentId(employee.getDepartment().getId());
        dto.setDepartmentName(employee.getDepartment().getName());
        dto.setHireDate(employee.getHireDate().toLocalDate());
        dto.setEmploymentStatus(employee.getEmploymentStatus());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setAddress(employee.getAddress());
        return dto;
    }

    public Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        updateEntity(employee, dto);
        return employee;
    }

    public void updateEntity(Employee employee, EmployeeDTO dto) {
        if (dto.getId() != null) {
            employee.setId(dto.getId());
        }
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setFullName(dto.getFullName());
        employee.setJobTitle(dto.getJobTitle());
        employee.setDepartment(departmentService.getDepartmentById(dto.getDepartmentId()));
        employee.setHireDate(dto.getHireDate().atStartOfDay());
        employee.setEmploymentStatus(dto.getEmploymentStatus());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
    }
}
