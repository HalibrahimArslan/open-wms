package com.hisarresearch.wms.exception.validation;

/**
 * Eskiden kullanilan Undertow {@code io.undertow.util.BadRequestException} sinifinin yerine
 * gecer; Spring Boot 4 Undertow'u kaldirdi.
 * <p>
 * Davranis bilerek ayni tutuldu: sinif bir HTTP durum kodu tasimadigi icin
 * ExceptionTranslator bu hatayi (adina ragmen) 500 ve mesajiyla doner. 400 donmesi
 * isteniyorsa bu ayri bir davranis degisikligi olarak ele alinmalidir.
 */
public class BadRequestException extends Exception {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }
}
