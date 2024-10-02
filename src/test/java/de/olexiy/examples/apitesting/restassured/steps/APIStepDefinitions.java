package de.olexiy.examples.apitesting.restassured.steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class APIStepDefinitions {

    private Response response;
    private String baseUrl = "https://jsonplaceholder.typicode.com";

    @Given("die API ist verfügbar")
    public void die_api_ist_verfügbar() {
        given().baseUri(baseUrl).when().get("/posts").then().statusCode(200);
    }

    @When("ich einen GET-Request an {string} sende")
    public void ich_einen_get_request_sende(String endpoint) {
        response = given().baseUri(baseUrl).when().get(endpoint);
    }

    @When("ich einen POST-Request an {string} mit dem Titel {string} und Body {string} sende")
    public void ich_einen_post_request_sende(String endpoint, String title, String body) {
        response = given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(String.format("{\"title\": \"%s\", \"body\": \"%s\", \"userId\": 1}", title, body))
                .when()
                .post(endpoint);
    }

    @Then("der Statuscode sollte {int} sein")
    public void der_statuscode_sollte_sein(Integer statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("die Antwort sollte den Titel {string} enthalten")
    public void die_antwort_sollte_den_titel_enthalten(String title) {
        response.then().body("title", equalTo(title));
    }

    @Then("die Antwort sollte eine ID enthalten")
    public void die_antwort_sollte_eine_id_enthalten() {
        response.then().body("id", notNullValue());
    }

    @Then("die Antwort sollte leer sein")
    public void die_antwort_sollte_leer_sein() {
        response.then().body(equalTo("{}"));
    }
}
