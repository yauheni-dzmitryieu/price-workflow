package com.yd.price.workflow.cucumber.steps;

import com.yd.price.workflow.cucumber.storage.RestResponseStorage;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;

public class ProductStepDefs {

    @Autowired
    private RestResponseStorage restResponseStorage;


    @When("PW: send request to get product price by productId [{long}]")
    public void addProductAdjustment(long productId) {
        ValidatableResponse response = given()
                .accept(ContentType.JSON)
                .get("api/v1/products/{productId}/price", productId)
                .then();

        restResponseStorage.saveResponse(response);
    }
}
