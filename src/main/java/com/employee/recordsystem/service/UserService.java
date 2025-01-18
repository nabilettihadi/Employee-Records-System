package com.employee.recordsystem.service;

import com.employee.recordsystem.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    User save(User user);
    boolean existsByUsername(String username);
}
