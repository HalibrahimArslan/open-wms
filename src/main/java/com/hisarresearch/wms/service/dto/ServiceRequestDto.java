package com.hisarresearch.wms.service.dto;

import java.io.Serializable;

public class ServiceRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serviceName;
    private Object data;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

