package com.hisarresearch.wms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tech.jhipster.config.JHipsterConstants;

/**
 * OpenAPI dokumanina JWT bearer semasi ekler; boylece Swagger arayuzundeki
 * "Authorize" dugmesiyle alinan token tum isteklere eklenir. Yalnizca
 * {@code api-docs} profilinde (dev grubunun parcasi) aciktir.
 */
@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_API_DOCS)
public class ApiDocsConfiguration {

    private static final String JWT_SCHEME = "JWT";

    @Bean
    public OpenApiCustomiser jwtSecurityOpenApiCustomiser() {
        return openApi -> {
            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }
            openApi
                .getComponents()
                .addSecuritySchemes(JWT_SCHEME, new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
            openApi.addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME));
        };
    }
}
