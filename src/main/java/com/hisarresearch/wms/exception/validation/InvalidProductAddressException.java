package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.constants.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidProductAddressException extends AbstractThrowableProblem {
    public InvalidProductAddressException(String barcode, Long addressId, String detailMessage) {
        super(ErrorConstants.INVALID_PRODUCT_ADDRESS_INFO, "Invalid Product Address", Status.BAD_REQUEST, String.format("Barkod: %s, Adres: %s, Detay: %s", barcode, addressId, detailMessage)
        );
    }
}
