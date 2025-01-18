package com.employee.recordsystem.controller;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Get all employees", description = "Retrieve all employees with optional filtering")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) EmploymentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateTo) {
        return ResponseEntity.ok(employeeService.findEmployees(name, employeeId, departmentId, 
            jobTitle, status, hireDateFrom, hireDateTo));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Get employee by ID", description = "Retrieve an employee by their ID")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('HR')")
    @Operation(summary = "Create employee", description = "Create a new employee record")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    @Operation(summary = "Update employee", description = "Update an existing employee record")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    @Operation(summary = "Delete employee", description = "Delete an employee record")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Search employees", description = "Search employees by various criteria")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) EmploymentStatus status) {
        return ResponseEntity.ok(employeeService.searchEmployees(query, departmentId, status));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Get employees by department", description = "Retrieve all employees in a specific department")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }
}
