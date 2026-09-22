package com.hisarresearch.wms;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/** JWT girisi ve uclarin yetki kurallari. */
class SecurityIT extends AbstractIntegrationTest {

    @Test
    void authenticateReturnsTokenThatOpensAccount() throws Exception {
        mockMvc
            .perform(get("/api/account").header("Authorization", adminToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.login").value("admin"))
            .andExpect(jsonPath("$.authorities", hasItem("ROLE_ADMIN")));
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        mockMvc
            .perform(
                post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"admin\",\"password\":\"yanlis\"}")
            )
            .andExpect(status().isUnauthorized());
    }

    @Test
    void apiRequiresToken() throws Exception {
        mockMvc.perform(get("/api/account")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/depoList")).andExpect(status().isUnauthorized());
    }

    @Test
    void invalidTokenIsRejected() throws Exception {
        mockMvc.perform(get("/api/account").header("Authorization", "Bearer gecersiz.token.degeri")).andExpect(status().isUnauthorized());
    }

    @Test
    void healthIsPublicAndUp() throws Exception {
        mockMvc.perform(get("/management/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void managementEndpointsRequireAdmin() throws Exception {
        mockMvc.perform(get("/management/env")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/management/env").header("Authorization", adminToken())).andExpect(status().isOk());
    }
}
