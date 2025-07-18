package com.scb.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScbAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScbAssignmentApplication.class, args);
    }
}
