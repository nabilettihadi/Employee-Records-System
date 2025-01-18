package com.employee.recordsystem.service;

import com.employee.recordsystem.mapper.DepartmentMapper;
import com.employee.recordsystem.mapper.EmployeeMapper;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.Employee;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;

    public byte[] generateEmployeeReport(Long employeeId) throws JRException {
        Employee employee = employeeMapper.toEntity(employeeService.getEmployeeById(employeeId));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("employeeId", employee.getEmployeeId());
        parameters.put("fullName", employee.getFullName());
        parameters.put("department", employee.getDepartment().getName());

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                getClass().getResourceAsStream("/reports/employee_report.jasper"),
                parameters,
                new JREmptyDataSource()
        );

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] generateDepartmentReport(Long departmentId) throws JRException {
        Department department = departmentMapper.toEntity(departmentService.getDepartmentById(departmentId));
        List<Employee> employees = employeeService.getEmployeesByDepartment(departmentId)
            .stream()
            .map(employeeMapper::toEntity)
            .collect(Collectors.toList());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("departmentName", department.getName());
        parameters.put("employeeCount", employees.size());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                getClass().getResourceAsStream("/reports/department_report.jasper"),
                parameters,
                dataSource
        );

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
