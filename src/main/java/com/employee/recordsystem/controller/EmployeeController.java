package com.employee.recordsystem.controller;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.service.EmployeeService;
import com.employee.recordsystem.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees", description = "Employee management APIs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SearchService searchService;

    @Operation(summary = "Get all employees", description = "Retrieve all employees with optional filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees found",
                content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(
            @Parameter(description = "Employee name to search for") 
            @RequestParam(required = false) String name,
            @Parameter(description = "Employee ID to search for") 
            @RequestParam(required = false) String employeeId,
            @Parameter(description = "Department ID to filter by") 
            @RequestParam(required = false) Long departmentId,
            @Parameter(description = "Job title to filter by") 
            @RequestParam(required = false) String jobTitle,
            @Parameter(description = "Employment status to filter by") 
            @RequestParam(required = false) EmploymentStatus status,
            @Parameter(description = "Start date for hire date range") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateFrom,
            @Parameter(description = "End date for hire date range") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateTo) {
        return ResponseEntity.ok(employeeService.findEmployees(name, employeeId, departmentId, 
            jobTitle, status, hireDateFrom, hireDateTo));
    }

    @Operation(summary = "Get employee by ID", description = "Retrieve an employee by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee found",
                content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER', 'ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployee(
            @Parameter(description = "ID of the employee to retrieve") 
            @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Operation(summary = "Search employees with pagination",
            description = "Search for employees using various criteria. Results are paginated and filtered based on user's role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Page<EmployeeDTO>> searchEmployees(
            @Parameter(description = "Employee name to search for") 
            @RequestParam(required = false) String name,
            
            @Parameter(description = "Employee ID to search for") 
            @RequestParam(required = false) String employeeId,
            
            @Parameter(description = "Department name to filter by") 
            @RequestParam(required = false) String department,
            
            @Parameter(description = "Job title to filter by") 
            @RequestParam(required = false) String jobTitle,
            
            @Parameter(description = "Employment status to filter by") 
            @RequestParam(required = false) EmploymentStatus status,
            
            @Parameter(description = "Start date for hire date range") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireFromDate,
            
            @Parameter(description = "End date for hire date range") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireToDate,
            
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        
        return ResponseEntity.ok(searchService.searchEmployees(
            name, employeeId, department, jobTitle, status,
            hireFromDate, hireToDate, pageable));
    }

    @Operation(summary = "Create a new employee",
            description = "Creates a new employee record. Only accessible by HR personnel.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee created successfully",
                content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "409", description = "Employee ID already exists")
    })
    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        return new ResponseEntity<>(employeeService.createEmployee(employeeDTO), 
            HttpStatus.CREATED);
    }

    @Operation(summary = "Update employee details",
            description = "Updates an existing employee record. Managers can only update employees in their department.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @Parameter(description = "ID of the employee to update") 
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @Operation(summary = "Delete employee",
            description = "Deletes an employee record. Only accessible by HR personnel.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "ID of the employee to delete") 
            @PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get employees by department", description = "Retrieve all employees in a specific department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees found",
                content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(
            @Parameter(description = "Department ID to retrieve employees from") 
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }
}
