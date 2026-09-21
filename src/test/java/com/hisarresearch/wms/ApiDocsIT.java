package com.hisarresearch.wms;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

/** api-docs profilinde Swagger arayuzu ve OpenAPI dokumani. */
class ApiDocsIT extends AbstractIntegrationTest {

    @Test
    void swaggerUiIsServed() throws Exception {
        mockMvc.perform(get("/swagger-ui/")).andExpect(status().isOk()).andExpect(forwardedUrl("/swagger-ui/index.html"));
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk()).andExpect(content().string(containsString("swagger-ui")));
    }

    @Test
    void openApiDocumentDeclaresJwtBearer() throws Exception {
        mockMvc
            .perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.components.securitySchemes.JWT.scheme").value("bearer"))
            .andExpect(jsonPath("$.paths['/api/depoList']").exists());
    }
}
