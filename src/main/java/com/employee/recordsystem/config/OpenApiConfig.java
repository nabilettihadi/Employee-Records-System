package com.employee.recordsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter JWT token")))
            .info(new Info()
                .title("Employee Records Management System API")
                .version("1.0")
                .description("""
                    RESTful API for managing employee records across departments.
                    
                    ## Features
                    - Complete employee information management
                    - Role-based access control (HR, Managers, Administrators)
                    - Advanced search and filtering capabilities
                    - Audit trail for all changes
                    - Reporting functionality
                    
                    ## Authentication
                    All API endpoints are secured using JWT authentication.
                    Include the JWT token in the Authorization header:
                    `Authorization: Bearer your-token-here`
                    
                    ## Roles and Permissions
                    - **HR**: Full CRUD operations on employee data
                    - **Managers**: View and update employees in their department
                    - **Administrators**: Full system access
                    """)
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                    .name("Employee Records System Support")
                    .email("support@employee-records.com")
                    .url("https://employee-records.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")))
            .servers(Arrays.asList(
                new Server()
                    .url("http://localhost:8080")
                    .description("Development server"),
                new Server()
                    .url("https://staging.employee-records.com")
                    .description("Staging server"),
                new Server()
                    .url("https://api.employee-records.com")
                    .description("Production server")))
            .addTagsItem(new Tag()
                .name("Employee Management")
                .description("Operations for managing employee records"))
            .addTagsItem(new Tag()
                .name("Department Management")
                .description("Operations for managing departments"))
            .addTagsItem(new Tag()
                .name("User Management")
                .description("Operations for managing system users"))
            .addTagsItem(new Tag()
                .name("Reports")
                .description("Operations for generating reports"))
            .addTagsItem(new Tag()
                .name("Audit")
                .description("Operations for viewing audit logs"));
    }
}
