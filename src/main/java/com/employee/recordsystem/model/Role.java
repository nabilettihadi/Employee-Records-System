package com.employee.recordsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleType name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public enum RoleType {
        ROLE_ADMIN,
        ROLE_HR,
        ROLE_MANAGER,
        ROLE_EMPLOYEE
    }
}
