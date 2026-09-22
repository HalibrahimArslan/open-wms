package com.hisarresearch.wms.exception.business;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BusinessException extends ProblemException {
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
        super(type, defaultMessage, HttpStatus.EXPECTATION_FAILED, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    @Override
    protected Map<String, Object> extraProperties() {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("entityName", entityName);
        properties.put("errorKey", errorKey);
        return properties;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
