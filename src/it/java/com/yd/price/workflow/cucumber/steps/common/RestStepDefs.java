package com.yd.price.workflow.cucumber.steps.common;

import com.yd.price.workflow.cucumber.SpringIntegrationTest;
import com.yd.price.workflow.cucumber.storage.DataFolderStorage;
import com.yd.price.workflow.cucumber.storage.RestResponseStorage;
import com.yd.price.workflow.cucumber.utils.TestUtil;
import io.cucumber.docstring.DocString;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.core.Option;
import org.springframework.beans.factory.annotation.Autowired;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class RestStepDefs {

  @Autowired
  private DataFolderStorage dataFolderStorage;

  @Autowired
  private RestResponseStorage restService;

  @Given("Hello World")
  public void stepHelloWorld() {
    System.out.println("Hello World");
  }

  @Then("response status should be {int} with JSON content type")
  public void responseStatusShouldBe(int statusCode) {
    restService.getResponse()
        .log().all()
        .statusCode(statusCode)
        .contentType(ContentType.JSON);
  }

  @Then("response body is equal [{string}]")
  public void responseBodyIsEqual(String jsonPath) {
    String output = dataFolderStorage.getDataFolder().rest().output();
    String expectedResponse = TestUtil.resourceToString(output + jsonPath);

    String responseBody = restService.getResponse()
        .log().all()
        .extract()
        .body()
        .asString();

    assertThatJson(responseBody)
        .when(Option.FAIL_FAST)
        .isEqualTo(expectedResponse);
  }

  @Then("response body is equal to json")
  public void responseBodyIsEqualToJson(DocString expectedJson) {
    assertThatJson(restService.getResponse().extract().body().asString())
        .when(Option.FAIL_FAST)
        .isEqualTo(expectedJson.getContent());
  }
}
