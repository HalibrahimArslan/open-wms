package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class InvalidOrderException extends ProblemException {
    private static final long serialVersionUID = 1L;

    public InvalidOrderException() {
        super(ErrorConstants.INVALID_ORDER_INFO, "Incorrect orderInfo", HttpStatus.BAD_REQUEST);
    }
}
