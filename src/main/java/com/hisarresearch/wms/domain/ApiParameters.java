package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * {@code aur_company.api_parameters} (jsonb) kolonunun kanonik sekli.
 *
 * <p>Tum alanlar opsiyoneldir; ERP'ye gore hangi alanlarin dolu olacagi degisir
 * (orn. LOCAL sirketlerde sadece {@code erpApiActive} kullanilir).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean erpApiActive;
    private String username;

    // Bu alan her zaman sifreli tutulur (bkz. ApiPasswordCipher): AurCompanyResource
    // kaydederken sifreler, ErpTokenService token isterken cozer. Entity uzerinde
    // yerinde sifreleme/cozme (JPA callback) yapilmaz; Hibernate'in dirty-checking'i
    // ve ModelMapper'in paylastigi referans yuzunden ERP'ye sifreli deger gidiyordu.
    // Burada Jackson erisim kisitlamasi (WRITE_ONLY vb.) kullanma: hem REST hem de
    // bu jsonb kolonun DB'ye yazilmasi ayni Jackson mekanizmasini kullaniyor.
    private String password;

    public Boolean getErpApiActive() {
        return erpApiActive;
    }

    public void setErpApiActive(Boolean erpApiActive) {
        this.erpApiActive = erpApiActive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
