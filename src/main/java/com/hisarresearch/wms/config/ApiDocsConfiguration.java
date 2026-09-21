package com.hisarresearch.wms.config;

import com.hisarresearch.wms.framework.config.JHipsterConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * OpenAPI dokumaninin basligi ve JWT bearer semasi. Sema sayesinde Swagger
 * arayuzundeki "Authorize" dugmesiyle alinan token tum isteklere eklenir.
 * Yalnizca {@code api-docs} profilinde (dev grubunun parcasi) aciktir.
 */
@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_API_DOCS)
public class ApiDocsConfiguration {

    private static final String JWT_SCHEME = "JWT";

    @Bean
    public OpenAPI wmsOpenApi(@Value("${spring.application.name}") String applicationName) {
        return new OpenAPI()
            .info(new Info().title(applicationName + " API").description(applicationName + " API documentation"))
            .components(
                new Components()
                    .addSecuritySchemes(JWT_SCHEME, new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
            )
            .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME));
    }
}
