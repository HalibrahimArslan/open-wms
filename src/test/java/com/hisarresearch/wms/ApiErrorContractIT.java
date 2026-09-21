package com.hisarresearch.wms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * Hata cevaplarinin sekli. Arayuz {@code message}, {@code title}, {@code detail},
 * {@code params} ve {@code fieldErrors} alanlarini okudugu icin bu alanlar surum
 * gecislerinde (problem kutuphanesi degisirken) ayni kalmalidir.
 */
class ApiErrorContractIT extends AbstractIntegrationTest {

    @Test
    void missingRequestParameter() throws Exception {
        assertError("error-400-missing-param", get("/api/aur-roles"), 400, true);
    }

    @Test
    void validationErrorsListFields() throws Exception {
        assertError(
            "error-400-validation",
            post("/api/musteriSevkiyat").contentType(MediaType.APPLICATION_JSON).content("{}"),
            400,
            true
        );
    }

    @Test
    void badRequestAlert() throws Exception {
        assertError(
            "error-400-bad-request-alert",
            post("/api/aur-roles").contentType(MediaType.APPLICATION_JSON).content("{\"id\":1,\"roleName\":\"X\"}"),
            400,
            true
        );
    }

    @Test
    void businessException() throws Exception {
        assertError("error-business", get("/api/address-by-depo-code/BULUNMAYAN/1"), 417, true);
    }

    @Test
    void methodNotAllowed() throws Exception {
        assertError("error-405", get("/api/address-type"), 405, true);
    }

    @Test
    void unknownEndpoint() throws Exception {
        // Govdeyi gercek sunucuda /error ucu yazar; MockMvc o yonlendirmeyi yapmadigi icin yalnizca durum kodu
        mockMvc.perform(get("/api/bulunmayan-uc").header("Authorization", adminToken())).andExpect(status().isNotFound());
    }

    @Test
    void missingToken() throws Exception {
        assertError("error-401", get("/api/account"), 401, false);
    }

    private void assertError(String snapshot, MockHttpServletRequestBuilder request, int expectedStatus, boolean authenticated)
        throws Exception {
        if (authenticated) {
            request.header("Authorization", adminToken());
        }
        String body = mockMvc.perform(request).andExpect(status().is(expectedStatus)).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        ApiSnapshots.assertMatches(snapshot, body);
    }
}
