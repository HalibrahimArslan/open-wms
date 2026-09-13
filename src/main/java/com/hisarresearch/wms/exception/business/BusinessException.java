package com.hisarresearch.wms.exception.business;

import com.hisarresearch.wms.exception.constants.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BusinessException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public BusinessException(String defaultMessage, String entityName) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, "notFound");
    }

    public BusinessException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BusinessException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.EXPECTATION_FAILED, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
