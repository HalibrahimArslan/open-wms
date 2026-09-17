package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.constants.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidSituationGenericError extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidSituationGenericError(String title) {
        super(ErrorConstants.ERR_GENERIC_FAILURE, title, Status.INTERNAL_SERVER_ERROR);
    }
}
