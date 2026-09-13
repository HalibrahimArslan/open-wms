package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.constants.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidOrderException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public InvalidOrderException() {
        super(ErrorConstants.INVALID_ORDER_INFO, "Incorrect orderInfo", Status.BAD_REQUEST);
    }
}
