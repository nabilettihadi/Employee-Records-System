package com.employee.recordsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI employeeRecordSystemOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Employee Records System API")
                .description("Spring Boot REST API for Employee Records Management")
                .version("1.0.0")
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")));
    }
}
