package com.interview.practice.devstarter;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@AutoConfiguration
@ComponentScan(basePackages = "com.interview.practice.devstarter")
public class DevStarterAutoConfiguration {

    @PostConstruct
    public void loggingStartup() {
        System.out.println("Dev Starter with JPA, H2, Web, and Lombok is configured and ready to use.");
    }

}
