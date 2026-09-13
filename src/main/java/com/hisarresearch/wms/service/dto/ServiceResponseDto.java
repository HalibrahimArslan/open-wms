package com.hisarresearch.wms.service.dto;

import java.io.Serializable;

public class ServiceResponseDto implements Serializable {

    private static final long serialVersionUID = 9113747814244740571L;
    private boolean success;
    private String message;
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

