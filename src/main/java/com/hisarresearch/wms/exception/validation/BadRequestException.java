package com.hisarresearch.wms.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Istemcinin gecersiz istegi icin firlatilir; ExceptionTranslator {@code @ResponseStatus}
 * uzerinden 400 ve {@code message: error.http.400} ile doner.
 * <p>
 * Eskiden Undertow'un {@code io.undertow.util.BadRequestException} sinifi kullaniliyordu.
 * O sinif Spring'e bir durum kodu bildirmedigi icin bu hatalar adina ragmen 500 donuyordu.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }
}
