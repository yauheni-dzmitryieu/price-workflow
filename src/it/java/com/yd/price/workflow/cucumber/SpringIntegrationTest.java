package com.yd.price.workflow.cucumber;

import com.yd.price.workflow.PriceWorkflowApp;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

//@ActiveProfiles("it")
@CucumberContextConfiguration
@SpringBootTest(classes = PriceWorkflowApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringIntegrationTest {

    @Value("${server.port}")
    private int port;

    @PostConstruct
    public void init() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
    }
}
