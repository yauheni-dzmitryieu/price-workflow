package com.yd.price.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PriceWorkflowApp {

    public static void main(String[] args) {
        SpringApplication.run(PriceWorkflowApp.class, args);
    }
}
