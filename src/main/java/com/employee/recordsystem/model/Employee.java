package com.employee.recordsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Employee ID is required")
    @Pattern(regexp = "^EMP\\d{6}$", message = "Employee ID must be in format EMP followed by 6 digits")
    private String employeeId;

    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Full name must contain only letters, spaces, and basic punctuation")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title cannot exceed 100 characters")
    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @NotNull(message = "Department is required")
    private Department department;

    @NotNull(message = "Hire date is required")
    @Past(message = "Hire date must be in the past")
    private LocalDateTime hireDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Employment status is required")
    private EmploymentStatus employmentStatus;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format. Must start with + and contain 1-14 digits")
    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
