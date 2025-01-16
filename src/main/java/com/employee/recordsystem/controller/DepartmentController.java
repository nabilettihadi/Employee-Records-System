package com.employee.recordsystem.controller;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Api(tags = "Department Management")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Create a new department")
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.ok(departmentService.createDepartment(departmentDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Update an existing department")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Delete a department")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    @ApiOperation("Get department by ID")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    @ApiOperation("Get all departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/{departmentId}/manager/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Assign manager to department")
    public ResponseEntity<DepartmentDTO> assignManager(
            @PathVariable Long departmentId,
            @PathVariable String employeeId) {
        return ResponseEntity.ok(departmentService.assignManager(departmentId, employeeId));
    }
}
