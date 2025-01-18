package com.employee.recordsystem.service.impl;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.mapper.EmployeeMapper;
import com.employee.recordsystem.model.Employee;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.repository.EmployeeRepository;
import com.employee.recordsystem.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {
    
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Page<EmployeeDTO> searchEmployees(
            String name, String id, String department,
            String jobTitle, EmploymentStatus status,
            LocalDate fromDate, LocalDate toDate,
            Pageable pageable) {
        
        Specification<Employee> spec = buildSearchSpecification(
            name, id, department, jobTitle, status, fromDate, toDate);
        
        return employeeRepository.findAll(spec, pageable)
            .map(employeeMapper::toDTO);
    }

    @Override
    public List<EmployeeDTO> searchEmployees(
            String name, String id, String department,
            String jobTitle, EmploymentStatus status,
            LocalDate fromDate, LocalDate toDate) {
        
        Specification<Employee> spec = buildSearchSpecification(
            name, id, department, jobTitle, status, fromDate, toDate);
        
        return employeeRepository.findAll(spec)
            .stream()
            .map(employeeMapper::toDTO)
            .toList();
    }

    private Specification<Employee> buildSearchSpecification(
            String name, String id, String department,
            String jobTitle, EmploymentStatus status,
            LocalDate fromDate, LocalDate toDate) {
        
        Specification<Employee> spec = Specification.where(null);
        
        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("fullName")), 
                    "%" + name.toLowerCase() + "%"));
        }
        
        if (id != null && !id.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(root.get("employeeId"), 
                    "%" + id + "%"));
        }
        
        if (department != null && !department.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("department").get("name"), 
                    department));
        }
        
        if (jobTitle != null && !jobTitle.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("jobTitle")), 
                    "%" + jobTitle.toLowerCase() + "%"));
        }
        
        if (status != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("employmentStatus"), status));
        }
        
        if (fromDate != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("hireDate"), 
                    fromDate.atStartOfDay()));
        }
        
        if (toDate != null) {
            spec = spec.and((root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("hireDate"), 
                    toDate.plusDays(1).atStartOfDay()));
        }
        
        return spec;
    }
}
