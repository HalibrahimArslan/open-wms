package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.constants.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidIdException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;
    public InvalidIdException() {
        super(ErrorConstants.ERR_INVALID_ID,"Invalid id", Status.BAD_REQUEST);
    }
}
