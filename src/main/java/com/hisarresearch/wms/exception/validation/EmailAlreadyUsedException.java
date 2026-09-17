package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
