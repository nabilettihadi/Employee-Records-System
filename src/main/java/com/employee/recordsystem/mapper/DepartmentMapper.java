package com.employee.recordsystem.mapper;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentDTO toDTO(Department department) {
        if (department == null) {
            return null;
        }

        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        if (department.getManager() != null) {
            dto.setManagerId(department.getManager().getId());
            dto.setManagerName(department.getManager().getFullName());
        }
        return dto;
    }

    public Department toEntity(DepartmentDTO dto) {
        if (dto == null) {
            return null;
        }

        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        return department;
    }
}
