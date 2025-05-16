package com.example.librarymanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;


@Log4j2
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "com.example.librarymanagement")
@EnableScheduling
public class LibrarymanagementApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LibrarymanagementApplication.class, args);

        Environment env = context.getEnvironment();

        String appName = env.getProperty("spring.application.name");

        if (appName != null) {
            appName = appName.toUpperCase();
        }
        String port = env.getProperty("server.port");
        log.info("-------------------------START {} Application------------------------------", appName);
        log.info("   Application         : {}", appName);
        log.info("   Url swagger-ui      : http://localhost:{}/swagger-ui.html", port);
        log.info("-------------------------START SUCCESS {} Application----------------------", appName);
    }

}
