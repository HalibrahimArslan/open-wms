package com.hisarresearch.wms.web.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.hisarresearch.wms.WmsApp;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@CucumberContextConfiguration
@SpringBootTest(classes = WmsApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringIntegrationTest {
    public static ResponseResults latestResponse = null;
    public static String token = null;


    private RestTemplate restTemplate = new RestTemplate();


    public String executeAuthenticate(String username, String password) throws IOException {
        String authUrl = "http://localhost:8080/api/authenticate";

        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("username", username);
        authRequest.put("password", password);

        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        requestCallback.setBody(new ObjectMapper().writeValueAsString(authRequest)); // Body'yi JSON olarak ayarla
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
            .execute(authUrl, HttpMethod.POST, requestCallback, response -> {
                if (errorHandler.hadError) {
                    return (errorHandler.getResults());
                } else {
                    return (new ResponseResults(response));
                }
            });

        if (latestResponse != null && latestResponse.getBody() != null) {
            Map<String, String> responseMap = new ObjectMapper().readValue(latestResponse.getBody(), Map.class);
            token = responseMap.get("id_token");
        }
        return token;
    }


    public void executeGet(String url) throws IOException {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.hadError) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }

    public void executePost(String url,Object body) throws IOException {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        requestCallback.setBody((String) body);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
            .execute(url, HttpMethod.POST, requestCallback, response -> {
                if (errorHandler.hadError) {
                    return (errorHandler.getResults());
                } else {
                    return (new ResponseResults(response));
                }
            });
    }

    public void executePostWithToken(String url, Object body) throws IOException {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

        if (token != null) {
            headers.put("Authorization", "Bearer " + token);
        }

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        requestCallback.setBody(new ObjectMapper().writeValueAsString(body)); // JSON body olarak ayarla
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
            .execute(url, HttpMethod.POST, requestCallback, response -> {
                if (errorHandler.hadError) {
                    return (errorHandler.getResults());
                } else {
                    return (new ResponseResults(response));
                }
            });
    }

    private class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults() {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hadError = response.getStatusCode().value() >= 400;
            return hadError;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            results = new ResponseResults(response);
        }
    }
}
