package com.hisarresearch.wms.exception.api;

import com.hisarresearch.wms.exception.ProblemException;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.hisarresearch.wms.exception.constants.ErrorConstants;

public class BadRequestAlertException extends ProblemException {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, HttpStatus.BAD_REQUEST, null, getAlertParameters(entityName, errorKey));
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
