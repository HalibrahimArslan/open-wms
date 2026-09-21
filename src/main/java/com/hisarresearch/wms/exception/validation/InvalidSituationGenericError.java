package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class InvalidSituationGenericError extends ProblemException {

    private static final long serialVersionUID = 1L;

    public InvalidSituationGenericError(String title) {
        super(ErrorConstants.ERR_GENERIC_FAILURE, title, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
