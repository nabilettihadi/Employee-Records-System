package com.employee.recordsystem.security;

import com.employee.recordsystem.model.Employee;
import com.employee.recordsystem.repository.EmployeeRepository;
import com.employee.recordsystem.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public boolean isEmployeeManager(String employeeId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return employeeRepository.findByEmployeeId(employeeId)
            .map(employee -> employee.getDepartment().getManager() != null &&
                 employee.getDepartment().getManager().getEmployeeId().equals(currentUsername))
            .orElse(false);
    }

    public boolean isManagerOfDepartment(Long departmentId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return departmentRepository.findById(departmentId)
            .map(department -> department.getManager() != null &&
                 department.getManager().getEmployeeId().equals(currentUsername))
            .orElse(false);
    }
}
