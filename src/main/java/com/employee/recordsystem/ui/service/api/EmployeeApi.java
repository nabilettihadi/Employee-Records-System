package com.employee.recordsystem.ui.service.api;

import com.employee.recordsystem.dto.EmployeeDTO;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface EmployeeApi {
    @GET("employees")
    Call<List<EmployeeDTO>> getAllEmployees();
    
    @GET("employees/{id}")
    Call<EmployeeDTO> getEmployee(@Path("id") String id);
    
    @POST("employees")
    Call<EmployeeDTO> createEmployee(@Body EmployeeDTO employee);
    
    @PUT("employees/{id}")
    Call<EmployeeDTO> updateEmployee(@Path("id") String id, @Body EmployeeDTO employee);
    
    @DELETE("employees/{id}")
    Call<Void> deleteEmployee(@Path("id") String id);
    
    @GET("employees/search")
    Call<List<EmployeeDTO>> searchEmployees(@Query("query") String query);
    
    @GET("employees/department/{departmentId}")
    Call<List<EmployeeDTO>> getEmployeesByDepartment(@Path("departmentId") Long departmentId);
    
    @GET("employees/status/{status}")
    Call<List<EmployeeDTO>> getEmployeesByStatus(@Path("status") String status);
}
