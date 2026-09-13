package com.hisarresearch.wms.service.dto.base;

import com.google.gson.JsonObject;

public class RequestDto {
    private Object data;
    private String serviceName;


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
