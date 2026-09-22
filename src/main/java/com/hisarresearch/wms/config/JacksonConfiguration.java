package com.hisarresearch.wms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.datatype.hibernate7.Hibernate7Module;

/**
 * MVC'nin kullandigi Jackson 3 JsonMapper'ina eklenen moduller. java.time ve Optional
 * destegi Jackson 3'te cekirdekte oldugu icin ayri modul gerekmez.
 */
@Configuration
public class JacksonConfiguration {

    /*
     * Support for Hibernate types in Jackson.
     */
    @Bean
    public Hibernate7Module hibernate7Module() {
        return new Hibernate7Module();
    }
}
