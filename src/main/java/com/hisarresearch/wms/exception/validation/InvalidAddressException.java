package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class InvalidAddressException extends ProblemException {
    public InvalidAddressException() {
        super(ErrorConstants.INVALID_ADDRESS_INFO, "Incorrect addressInfo", HttpStatus.BAD_REQUEST);
    }
}
