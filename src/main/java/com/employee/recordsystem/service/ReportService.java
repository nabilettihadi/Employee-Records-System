package com.employee.recordsystem.service;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Department;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public ReportService(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    public byte[] generateEmployeeReport(String format) throws JRException {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "HR Department");
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            getClass().getResourceAsStream("/reports/employees.jasper"),
            parameters,
            dataSource
        );
        
        return switch (format.toLowerCase()) {
            case "pdf" -> JasperExportManager.exportReportToPdf(jasperPrint);
            case "html" -> JasperExportManager.exportReportToHtml(jasperPrint).getBytes();
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }

    public byte[] generateDepartmentReport(Long departmentId, String format) throws JRException {
        Department department = departmentService.getDepartmentById(departmentId);
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(departmentId);
        
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("departmentName", department.getName());
        parameters.put("reportDate", java.time.LocalDate.now().toString());
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            getClass().getResourceAsStream("/reports/department.jasper"),
            parameters,
            dataSource
        );
        
        return switch (format.toLowerCase()) {
            case "pdf" -> JasperExportManager.exportReportToPdf(jasperPrint);
            case "html" -> JasperExportManager.exportReportToHtml(jasperPrint).getBytes();
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }
}
