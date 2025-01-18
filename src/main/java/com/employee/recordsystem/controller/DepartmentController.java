package com.employee.recordsystem.controller;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Department management APIs")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Get all departments", description = "Retrieve all departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    @Operation(summary = "Get department by ID", description = "Retrieve a department by its ID")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new department", description = "Create a new department")
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.ok(departmentService.createDepartment(departmentDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing department", description = "Update an existing department")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a department", description = "Delete a department")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{departmentId}/manager/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign manager to department", description = "Assign a manager to a department")
    public ResponseEntity<DepartmentDTO> assignManager(
            @PathVariable Long departmentId,
            @PathVariable String employeeId) {
        return ResponseEntity.ok(departmentService.assignManager(departmentId, employeeId));
    }
}
