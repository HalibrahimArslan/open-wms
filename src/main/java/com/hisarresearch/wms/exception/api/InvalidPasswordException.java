package com.hisarresearch.wms.exception.api;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class InvalidPasswordException extends ProblemException {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", HttpStatus.BAD_REQUEST);
    }
}
