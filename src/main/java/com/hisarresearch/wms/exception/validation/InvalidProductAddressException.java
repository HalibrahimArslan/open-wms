package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class InvalidProductAddressException extends ProblemException {
    public InvalidProductAddressException(String barcode, Long addressId, String detailMessage) {
        super(ErrorConstants.INVALID_PRODUCT_ADDRESS_INFO, "Invalid Product Address", HttpStatus.BAD_REQUEST, String.format("Barkod: %s, Adres: %s, Detay: %s", barcode, addressId, detailMessage)
        );
    }
}
