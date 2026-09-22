package com.hisarresearch.wms.exception.validation;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class InvalidParentMenuIdException extends ProblemException {
    private static final long serialVersionUID = 1L;

    public InvalidParentMenuIdException() {
        super(ErrorConstants.INVALID_MENU_INFO, "Not found  menu related parentMenuId", HttpStatus.BAD_REQUEST);
    }
}
