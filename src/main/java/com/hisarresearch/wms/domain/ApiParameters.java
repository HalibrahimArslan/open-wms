package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

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
    private List<Integer> depoNo;
    private String username;

    // Bu alan her zaman duz metin tutulur; DB'ye yazilirken/okunurken sifrelenip
    // cozulmesi AurCompany'nin @PrePersist/@PostLoad callback'lerinde yapilir
    // (bkz. ApiPasswordCipher). Burada Jackson erisim kisitlamasi (WRITE_ONLY vb.)
    // kullanma: hem REST hem de bu jsonb kolonun DB'ye yazilmasi ayni Jackson
    // mekanizmasini kullaniyor, bu yuzden REST'i kisitlamak DB'ye yazmayi da kirar.
    private String password;

    public Boolean getErpApiActive() {
        return erpApiActive;
    }

    public void setErpApiActive(Boolean erpApiActive) {
        this.erpApiActive = erpApiActive;
    }

    public List<Integer> getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(List<Integer> depoNo) {
        this.depoNo = depoNo;
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
