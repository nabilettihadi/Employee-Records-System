package com.employee.recordsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank(message = "Department name is required")
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Column(name = "description")
    private String description;
}
