package com.example.librarymanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final String API_KEY = "Bearer Token";

    @Bean
    OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI().info(
                new Info()
                        .title("Project Library Manager Spring API")
                        .version("1.0")
                        .description("Documentation Project Library Manager Spring API v1.0")
        );
        openAPI.components(
                new Components()
                        .addSecuritySchemes(
                                API_KEY,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                        )
        );
        openAPI.addSecurityItem(new SecurityRequirement().addList(API_KEY));
        return openAPI;
    }

}