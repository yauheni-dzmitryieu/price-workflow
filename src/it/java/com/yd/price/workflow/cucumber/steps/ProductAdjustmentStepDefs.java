package com.yd.price.workflow.cucumber.steps;

import com.yd.price.workflow.cucumber.storage.RestResponseStorage;
import com.yd.price.workflow.rest.dto.PriceAdjustmentRequest;
import io.cucumber.docstring.DocString;
import io.cucumber.docstring.DocStringType;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductAdjustmentStepDefs {

    @Autowired
    private RestResponseStorage restResponseStorage;


    @When("PW: send request to add prise adjustment for product with id [{long}]")
    public void addProductAdjustment(long productId, DocString request) {
        assertThat(request)
                .isNotNull()
                .extracting(DocString::getContentType)
                .isEqualTo("json");

        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request.getContent())
                .post("api/v1/admin/products/{productId}/adjustments", productId)
                .then();

        restResponseStorage.saveResponse(response);
    }
}
