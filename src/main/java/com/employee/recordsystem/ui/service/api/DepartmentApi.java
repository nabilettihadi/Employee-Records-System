package com.employee.recordsystem.ui.service.api;

import com.employee.recordsystem.dto.DepartmentDTO;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface DepartmentApi {
    @GET("departments")
    Call<List<DepartmentDTO>> getAllDepartments();
    
    @GET("departments/{id}")
    Call<DepartmentDTO> getDepartment(@Path("id") Long id);
    
    @POST("departments")
    Call<DepartmentDTO> createDepartment(@Body DepartmentDTO department);
    
    @PUT("departments/{id}")
    Call<DepartmentDTO> updateDepartment(@Path("id") Long id, @Body DepartmentDTO department);
    
    @DELETE("departments/{id}")
    Call<Void> deleteDepartment(@Path("id") Long id);
    
    @PUT("departments/{departmentId}/manager/{employeeId}")
    Call<DepartmentDTO> assignManager(
        @Path("departmentId") Long departmentId,
        @Path("employeeId") String employeeId
    );
}
