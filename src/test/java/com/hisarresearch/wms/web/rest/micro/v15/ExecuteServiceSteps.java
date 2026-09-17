package com.hisarresearch.wms.web.rest.micro.v15;

import com.hisarresearch.wms.web.rest.SpringIntegrationTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExecuteServiceSteps extends SpringIntegrationTest {

    @Given("the user makes a request to the authenticate endpoint and receives a token")
    public void the_user_makes_a_request_to_the_authenticate_endpoint_and_receives_a_token() throws IOException {
        String id_token = executeAuthenticate("testuser", "test_123");
        Assertions.assertNotNull(id_token, "Token could not be retrieved.");
    }

    @When("the user makes a request to the mikro v15 endpoint with the token")
    public void the_user_makes_a_request_to_the_mikro_v15_endpoint_with_the_token() throws IOException {
        String url = "http://localhost:8080/api/mikro-v15";

        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();
        filter.put("field", "cariUnvan");
        filter.put("operator", "LK");
        filter.put("value", "mus");
        filter.put("values", new ArrayList<>());

        data.put("filters", Collections.singletonList(filter));
        payload.put("data", data);
        payload.put("serviceName", "depoService.cariListByFilters");

        executePostWithToken(url, payload);
    }

    @Then("the response should return 200")
    public void the_response_should_return_200() throws IOException {
        Assertions.assertEquals(200, latestResponse.getTheResponse().getStatusCode().value());
    }
}
