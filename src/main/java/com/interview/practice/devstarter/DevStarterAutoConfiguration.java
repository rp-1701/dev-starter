package com.interview.practice.devstarter;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DataSource.class)
public class DevStarterAutoConfiguration {

    @PostConstruct
    public void loggingStartup() {
        System.out.println("Dev Starter with JPA, H2, Web, and Lombok is configured and ready to use.");
    }

}
