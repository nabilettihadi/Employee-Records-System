package com.employee.recordsystem.controller;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Api(tags = "Employee Management")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @ApiOperation("Create a new employee")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or @securityService.isEmployeeManager(#employeeId)")
    @ApiOperation("Update an existing employee")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable String employeeId,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, employeeDTO));
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @ApiOperation("Delete an employee")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or @securityService.isEmployeeManager(#employeeId)")
    @ApiOperation("Get employee by ID")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @GetMapping
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @ApiOperation("Get all employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @ApiOperation("Search employees")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(@RequestParam String searchTerm) {
        return ResponseEntity.ok(employeeService.searchEmployees(searchTerm));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or @securityService.isManagerOfDepartment(#departmentId)")
    @ApiOperation("Get employees by department")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @ApiOperation("Get employees by status")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByStatus(@PathVariable EmploymentStatus status) {
        return ResponseEntity.ok(employeeService.getEmployeesByStatus(status));
    }

    @GetMapping("/hired-between")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @ApiOperation("Get employees hired between dates")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByHireDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(employeeService.getEmployeesByHireDateRange(startDate, endDate));
    }
}
