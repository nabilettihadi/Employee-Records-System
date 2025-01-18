package com.employee.recordsystem.service;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SearchService {
    
    /**
     * Search for employees with pagination
     *
     * @param name Employee name to search for
     * @param id Employee ID to search for
     * @param department Department name to filter by
     * @param jobTitle Job title to filter by
     * @param status Employment status to filter by
     * @param fromDate Start date for hire date range
     * @param toDate End date for hire date range
     * @param pageable Pagination information
     * @return Page of employee DTOs matching the search criteria
     */
    Page<EmployeeDTO> searchEmployees(
        String name,
        String id,
        String department,
        String jobTitle,
        EmploymentStatus status,
        LocalDate fromDate,
        LocalDate toDate,
        Pageable pageable
    );

    /**
     * Search for employees without pagination (for export/report purposes)
     */
    List<EmployeeDTO> searchEmployees(
        String name,
        String id,
        String department,
        String jobTitle,
        EmploymentStatus status,
        LocalDate fromDate,
        LocalDate toDate
    );
}
