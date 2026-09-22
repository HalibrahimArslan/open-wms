package com.hisarresearch.wms.exception;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * RFC 7807 (application/problem+json) cevabina donusen uygulama hatalarinin tabani.
 * <p>
 * Eskiden zalando problem kutuphanesinin {@code AbstractThrowableProblem} sinifi
 * kullaniliyordu; Spring Boot 4 / Jackson 3 ile calismadigi icin ayni cevap seklini
 * ({@code type}, {@code title}, {@code status}, {@code detail} ve ek alanlar) ureten bu
 * sinif yazildi. Govdeyi {@code ExceptionTranslator} {@link #toBody()} ile olusturur.
 */
public abstract class ProblemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final URI type;

    private final String title;

    private final HttpStatus status;

    private final String detail;

    private final Map<String, Object> parameters;

    protected ProblemException(URI type, String title, HttpStatus status) {
        this(type, title, status, null, Collections.emptyMap());
    }

    protected ProblemException(URI type, String title, HttpStatus status, String detail) {
        this(type, title, status, detail, Collections.emptyMap());
    }

    protected ProblemException(URI type, String title, HttpStatus status, String detail, Map<String, Object> parameters) {
        super(joinTitleAndDetail(title, detail));
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.parameters = Collections.unmodifiableMap(new LinkedHashMap<>(parameters));
    }

    public URI getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    /** Alt siniflarin cevaba ekledigi alanlar (ornegin entityName, errorKey). */
    protected Map<String, Object> extraProperties() {
        return Collections.emptyMap();
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", type.toString());
        body.put("title", title);
        body.put("status", status.value());
        if (detail != null) {
            body.put("detail", detail);
        }
        body.putAll(parameters);
        body.putAll(extraProperties());
        return body;
    }

    private static String joinTitleAndDetail(String title, String detail) {
        if (title == null) {
            return detail;
        }
        return detail == null ? title : title + ": " + detail;
    }
}
