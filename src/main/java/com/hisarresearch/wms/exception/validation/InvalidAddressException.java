package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.constants.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidAddressException extends AbstractThrowableProblem {
    public InvalidAddressException() {
        super(ErrorConstants.INVALID_ADDRESS_INFO, "Incorrect addressInfo", Status.BAD_REQUEST);
    }
}
