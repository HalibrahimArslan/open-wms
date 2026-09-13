package com.hisarresearch.wms.service.dto.base;



import java.io.Serializable;

public class ResponseDto implements Serializable {
    private String success;
    private String message;
    private Object data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
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
