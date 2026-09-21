package com.hisarresearch.wms.exception.handler;

import com.hisarresearch.wms.exception.ProblemException;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.api.InvalidPasswordException;
import com.hisarresearch.wms.exception.api.LoginAlreadyUsedException;
import com.hisarresearch.wms.exception.constants.ErrorConstants;
import com.hisarresearch.wms.exception.validation.EmailAlreadyUsedException;
import com.hisarresearch.wms.exception.validation.UsernameAlreadyUsedException;
import com.hisarresearch.wms.framework.config.JHipsterConstants;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;
import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Sunucu tarafindaki hatalari RFC 7807 (application/problem+json) govdelerine cevirir.
 * <p>
 * Govde sekli eski zalando problem-spring-web cevaplariyla aynidir; arayuz {@code message},
 * {@code title}, {@code detail}, {@code params} ve {@code fieldErrors} alanlarini okur.
 * Genel hatalar {@code type}, {@code title}, {@code status}, {@code detail}, {@code path} ve
 * {@code message} ({@code error.http.<kod>}) doner; uygulamanin kendi hatalari
 * ({@link ProblemException}) kendi alanlariyla doner.
 */
@ControllerAdvice
public class ExceptionTranslator {

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String VIOLATIONS_KEY = "violations";
    private static final URI ZALANDO_CONSTRAINT_VIOLATION_TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Environment env;

    public ExceptionTranslator(Environment env) {
        this.env = env;
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result
            .getFieldErrors()
            .stream()
            .map(f ->
                new FieldErrorVM(
                    f.getObjectName().replaceFirst("DTO$", ""),
                    f.getField(),
                    StringUtils.isNotBlank(f.getDefaultMessage()) ? f.getDefaultMessage() : f.getCode()
                )
            )
            .collect(Collectors.toList());

        Map<String, Object> body = baseBody(
            ErrorConstants.CONSTRAINT_VIOLATION_TYPE,
            "Method argument not valid",
            HttpStatus.BAD_REQUEST,
            null,
            request
        );
        body.put(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        body.put(FIELD_ERRORS_KEY, fieldErrors);
        return problem(HttpStatus.BAD_REQUEST, body, null);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleBindException(BindException ex, HttpServletRequest request) {
        List<Map<String, String>> violations = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(f -> violation(f.getField(), f.getDefaultMessage()))
            .collect(Collectors.toList());
        return constraintViolation(violations, request);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<Map<String, String>> violations = ex
            .getConstraintViolations()
            .stream()
            .map(v -> violation(v.getPropertyPath().toString(), v.getMessage()))
            .collect(Collectors.toList());
        return constraintViolation(violations, request);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyUsedException(
        com.hisarresearch.wms.exception.api.EmailAlreadyUsedException ex,
        HttpServletRequest request
    ) {
        return handleBadRequestAlertException(new EmailAlreadyUsedException(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleUsernameAlreadyUsedException(UsernameAlreadyUsedException ex, HttpServletRequest request) {
        return handleBadRequestAlertException(new LoginAlreadyUsedException(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleInvalidPasswordException(
        com.hisarresearch.wms.exception.validation.InvalidPasswordException ex,
        HttpServletRequest request
    ) {
        return handleProblemException(new InvalidPasswordException(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleBadRequestAlertException(BadRequestAlertException ex, HttpServletRequest request) {
        return problem(
            ex.getStatus(),
            ex.toBody(),
            HeaderUtil.createFailureAlert(applicationName, true, ex.getEntityName(), ex.getErrorKey(), ex.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleProblemException(ProblemException ex, HttpServletRequest request) {
        return problem(ex.getStatus(), ex.toBody(), null);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleConcurrencyFailure(ConcurrencyFailureException ex, HttpServletRequest request) {
        Sentry.captureException(ex);
        Map<String, Object> body = baseBody(
            ErrorConstants.DEFAULT_TYPE,
            HttpStatus.CONFLICT.getReasonPhrase(),
            HttpStatus.CONFLICT,
            null,
            request
        );
        body.put(MESSAGE_KEY, ErrorConstants.ERR_CONCURRENCY_FAILURE);
        return problem(HttpStatus.CONFLICT, body, null);
    }

    /**
     * Diger tum hatalar: Spring MVC hatalari (eksik parametre, desteklenmeyen metot, bulunamayan
     * adres...), yetki hatalari (ProblemSecuritySupport uzerinden) ve beklenmeyen hatalar.
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleAny(Throwable throwable, HttpServletRequest request) {
        HttpStatusCode status = resolveStatus(throwable);
        Sentry.captureException(throwable);
        Map<String, Object> body = baseBody(ErrorConstants.DEFAULT_TYPE, reasonPhrase(status), status, detail(throwable), request);
        body.put(MESSAGE_KEY, "error.http." + status.value());
        HttpHeaders headers = null;
        if (throwable instanceof ErrorResponse errorResponse && !errorResponse.getHeaders().isEmpty()) {
            headers = errorResponse.getHeaders();
        }
        return problem(status, body, headers);
    }

    private HttpStatusCode resolveStatus(Throwable throwable) {
        if (throwable instanceof ErrorResponse errorResponse) {
            return errorResponse.getStatusCode();
        }
        if (throwable instanceof AuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (throwable instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        }
        if (throwable instanceof HttpMessageNotReadableException || throwable instanceof TypeMismatchException) {
            return HttpStatus.BAD_REQUEST;
        }
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(throwable.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.code();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String detail(Throwable throwable) {
        if (Arrays.asList(env.getActiveProfiles()).contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            if (throwable instanceof HttpMessageConversionException) {
                return "Unable to convert http message";
            }
            if (throwable instanceof DataAccessException) {
                return "Failure during data access";
            }
            if (containsPackageName(throwable.getMessage())) {
                return "Unexpected runtime exception";
            }
        }
        return throwable.getMessage();
    }

    private ResponseEntity<Map<String, Object>> constraintViolation(List<Map<String, String>> violations, HttpServletRequest request) {
        Map<String, Object> body = baseBody(ZALANDO_CONSTRAINT_VIOLATION_TYPE, "Constraint Violation", HttpStatus.BAD_REQUEST, null, request);
        body.put(VIOLATIONS_KEY, violations);
        body.put(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        return problem(HttpStatus.BAD_REQUEST, body, null);
    }

    private static Map<String, String> violation(String field, String message) {
        Map<String, String> violation = new LinkedHashMap<>();
        violation.put("field", field);
        violation.put("message", message);
        return violation;
    }

    private static Map<String, Object> baseBody(URI type, String title, HttpStatusCode status, String detail, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", type.toString());
        body.put("title", title);
        body.put("status", status.value());
        if (detail != null) {
            body.put("detail", detail);
        }
        body.put(PATH_KEY, request != null ? request.getRequestURI() : StringUtils.EMPTY);
        return body;
    }

    private static String reasonPhrase(HttpStatusCode status) {
        HttpStatus resolved = HttpStatus.resolve(status.value());
        return resolved != null ? resolved.getReasonPhrase() : null;
    }

    private static ResponseEntity<Map<String, Object>> problem(HttpStatusCode status, Map<String, Object> body, HttpHeaders headers) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON);
        if (headers != null) {
            builder.headers(headers);
        }
        return builder.body(body);
    }

    private boolean containsPackageName(String message) {
        // This list is for sure not complete
        return StringUtils.containsAny(message, "org.", "java.", "net.", "javax.", "com.", "io.", "de.", "com.hisarresearch.wms");
    }
}
