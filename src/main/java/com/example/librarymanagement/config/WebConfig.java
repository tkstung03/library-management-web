package com.example.librarymanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./app/public/images/");

        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:./app/public/files/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");

    }
}
