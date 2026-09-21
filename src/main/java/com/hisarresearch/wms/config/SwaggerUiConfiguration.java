package com.hisarresearch.wms.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.apidoc.customizer.SpringfoxCustomizer;

/**
 * Swagger arayuzunu {@code /swagger-ui/} altinda sunar ve "Authorize" dugmesiyle
 * JWT girilebilmesi icin dokumana bearer semasi ekler. Yalnizca {@code api-docs}
 * profilinde (dev grubunun parcasi) aciktir.
 */
@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_API_DOCS)
public class SwaggerUiConfiguration implements WebMvcConfigurer {

    private static final String JWT_SCHEME = "JWT";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
            .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
    }

    @Bean
    public SpringfoxCustomizer jwtSecuritySpringfoxCustomizer() {
        return docket ->
            docket
                .securitySchemes(List.of(HttpAuthenticationScheme.JWT_BEARER_BUILDER.name(JWT_SCHEME).build()))
                .securityContexts(
                    List.of(
                        SecurityContext
                            .builder()
                            .securityReferences(List.of(new SecurityReference(JWT_SCHEME, new AuthorizationScope[0])))
                            .build()
                    )
                );
    }
}
