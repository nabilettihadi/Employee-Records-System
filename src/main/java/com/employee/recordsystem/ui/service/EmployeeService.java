package com.employee.recordsystem.ui.service;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.ui.service.api.EmployeeApi;
import retrofit2.Response;
import java.io.IOException;
import java.util.List;

public class EmployeeService {
    private final EmployeeApi employeeApi;

    public EmployeeService() {
        this.employeeApi = RetrofitConfig.getClient().create(EmployeeApi.class);
    }

    public List<EmployeeDTO> getAllEmployees() throws IOException {
        Response<List<EmployeeDTO>> response = employeeApi.getAllEmployees().execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public EmployeeDTO getEmployee(String id) throws IOException {
        Response<EmployeeDTO> response = employeeApi.getEmployee(id).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public EmployeeDTO createEmployee(EmployeeDTO employee) throws IOException {
        Response<EmployeeDTO> response = employeeApi.createEmployee(employee).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public EmployeeDTO updateEmployee(String id, EmployeeDTO employee) throws IOException {
        Response<EmployeeDTO> response = employeeApi.updateEmployee(id, employee).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public void deleteEmployee(String id) throws IOException {
        Response<Void> response = employeeApi.deleteEmployee(id).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
    }

    public List<EmployeeDTO> searchEmployees(String query) throws IOException {
        Response<List<EmployeeDTO>> response = employeeApi.searchEmployees(query).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) throws IOException {
        Response<List<EmployeeDTO>> response = employeeApi.getEmployeesByDepartment(departmentId).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public List<EmployeeDTO> getEmployeesByStatus(String status) throws IOException {
        Response<List<EmployeeDTO>> response = employeeApi.getEmployeesByStatus(status).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }
}
