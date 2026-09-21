package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class InvalidIdException extends ProblemException {
    private static final long serialVersionUID = 1L;
    public InvalidIdException() {
        super(ErrorConstants.ERR_INVALID_ID,"Invalid id", HttpStatus.BAD_REQUEST);
    }
}
