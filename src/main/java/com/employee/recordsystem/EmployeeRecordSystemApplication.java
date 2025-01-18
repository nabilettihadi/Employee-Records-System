package com.employee.recordsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.employee.recordsystem.ui.MainFrame;

import javax.swing.*;

@SpringBootApplication
public class EmployeeRecordSystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EmployeeRecordSystemApplication.class, args);
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                MainFrame mainFrame = context.getBean(MainFrame.class);
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
