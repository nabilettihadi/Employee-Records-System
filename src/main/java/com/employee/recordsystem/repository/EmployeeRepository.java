package com.employee.recordsystem.repository;

import com.employee.recordsystem.model.Employee;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmployeeId(String employeeId);
    
    List<Employee> findByDepartment(Department department);
    
    List<Employee> findByEmploymentStatus(EmploymentStatus status);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.jobTitle) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);
    
    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);
    
    boolean existsByEmployeeId(String employeeId);
    
    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    List<Employee> findByDepartmentId(@Param("departmentId") Long departmentId);
    
    List<Employee> findByJobTitleContainingIgnoreCase(String jobTitle);
    
    List<Employee> findByFullNameContainingIgnoreCase(String name);
    List<Employee> findByEmployeeIdContainingIgnoreCase(String employeeId);
    List<Employee> findByDepartment_NameContainingIgnoreCase(String departmentName);
}
