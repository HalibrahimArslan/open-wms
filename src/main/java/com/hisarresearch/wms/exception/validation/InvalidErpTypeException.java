package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.constants.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidErpTypeException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public InvalidErpTypeException() {
        super(ErrorConstants.ERR_INVALID_ERP_TYPE,"Invalid erp type", Status.BAD_REQUEST);
    }
}
