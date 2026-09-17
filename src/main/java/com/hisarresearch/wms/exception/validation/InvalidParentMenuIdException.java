package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.constants.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidParentMenuIdException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public InvalidParentMenuIdException() {
        super(ErrorConstants.INVALID_MENU_INFO, "Not found  menu related parentMenuId", Status.BAD_REQUEST);
    }
}
