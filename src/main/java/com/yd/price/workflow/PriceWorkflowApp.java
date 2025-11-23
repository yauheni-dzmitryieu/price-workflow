package com.yd.price.workflow;

import com.yd.price.workflow.config.CacheProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableConfigurationProperties(value = {CacheProperties.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class PriceWorkflowApp {

    public static void main(String[] args) {
        SpringApplication.run(PriceWorkflowApp.class, args);
    }
}
