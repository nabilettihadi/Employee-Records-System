package com.employee.recordsystem.service;

import com.employee.recordsystem.dto.DepartmentDTO;
import java.util.List;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);
    void deleteDepartment(Long id);
    DepartmentDTO getDepartmentById(Long id);
    List<DepartmentDTO> getAllDepartments();
    DepartmentDTO assignManager(Long departmentId, String employeeId);
    boolean isDepartmentNameUnique(String name);
}
