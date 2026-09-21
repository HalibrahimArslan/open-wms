package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class InvalidErpTypeException extends ProblemException {
    private static final long serialVersionUID = 1L;

    public InvalidErpTypeException() {
        super(ErrorConstants.ERR_INVALID_ERP_TYPE,"Invalid erp type", HttpStatus.BAD_REQUEST);
    }
}
