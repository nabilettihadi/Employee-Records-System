package com.employee.recordsystem.ui.service;

import com.employee.recordsystem.dto.DepartmentDTO;
import com.employee.recordsystem.ui.service.api.DepartmentApi;
import retrofit2.Response;
import java.io.IOException;
import java.util.List;

public class DepartmentService {
    private final DepartmentApi departmentApi;

    public DepartmentService() {
        this.departmentApi = RetrofitConfig.getClient().create(DepartmentApi.class);
    }

    public List<DepartmentDTO> getAllDepartments() throws IOException {
        Response<List<DepartmentDTO>> response = departmentApi.getAllDepartments().execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public DepartmentDTO getDepartment(Long id) throws IOException {
        Response<DepartmentDTO> response = departmentApi.getDepartment(id).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public DepartmentDTO createDepartment(DepartmentDTO department) throws IOException {
        Response<DepartmentDTO> response = departmentApi.createDepartment(department).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO department) throws IOException {
        Response<DepartmentDTO> response = departmentApi.updateDepartment(id, department).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }

    public void deleteDepartment(Long id) throws IOException {
        Response<Void> response = departmentApi.deleteDepartment(id).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
    }

    public DepartmentDTO assignManager(Long departmentId, String employeeId) throws IOException {
        Response<DepartmentDTO> response = departmentApi.assignManager(departmentId, employeeId).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        return response.body();
    }
}
