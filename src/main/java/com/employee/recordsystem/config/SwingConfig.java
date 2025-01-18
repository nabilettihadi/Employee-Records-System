package com.employee.recordsystem.config;

import com.employee.recordsystem.ui.MainFrame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class SwingConfig {

    @Bean
    @Lazy // This ensures the MainFrame is created on the EDT
    public MainFrame mainFrame() {
        return new MainFrame();
    }
}
