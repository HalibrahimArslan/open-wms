package com.hisarresearch.wms.exception.constants;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");

    public static final URI INVALID_ORDER_INFO = URI.create(PROBLEM_BASE_URL + "/invalid-order-info");

    public static final URI INVALID_MENU_INFO = URI.create(PROBLEM_BASE_URL + "/invalid-menu-info");

    public static final URI INVALID_ADDRESS_INFO = URI.create(PROBLEM_BASE_URL + "/invalid-address-info");
    public static final URI INVALID_PRODUCT_ADDRESS_INFO = URI.create(PROBLEM_BASE_URL + "/invalid-product-address-info");
    public static final URI ERR_GENERIC_FAILURE =  URI.create(PROBLEM_BASE_URL + "/error.generic.failure");
    public static final URI ERR_INVALID_ID = URI.create(PROBLEM_BASE_URL + "/error.invalid-id");
    public static final URI ERR_INVALID_ERP_TYPE = URI.create(PROBLEM_BASE_URL + "/error.invalid-erp-type");


    private ErrorConstants() {}
}
