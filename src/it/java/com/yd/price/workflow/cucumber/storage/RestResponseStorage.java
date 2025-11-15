package com.yd.price.workflow.cucumber.storage;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.ValidatableResponse;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class RestResponseStorage {
    private ValidatableResponse response;

    public void saveResponse(ValidatableResponse response) {
        this.response = response;
    }

    public ValidatableResponse getResponse() {
        return response;
    }
}
