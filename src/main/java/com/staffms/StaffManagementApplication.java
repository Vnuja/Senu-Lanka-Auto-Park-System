package com.staffms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StaffManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffManagementApplication.class, args);
    }
}
