package com.hisarresearch.wms.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

public class AurLogCriteria implements Serializable, Criteria {
    private LongFilter logId;
    private StringFilter serviceName;
    private StringFilter requestBody;
    private StringFilter responseBody;
    private StringFilter serviceUrl;


    public AurLogCriteria(){}

    public AurLogCriteria(AurLogCriteria other){
        this.logId = other.logId == null ? null : other.logId.copy();
        this.serviceName = other.serviceName == null ? null : other.serviceName.copy();
        this.serviceUrl = other.serviceUrl == null ? null : other.serviceUrl.copy();
        this.requestBody = other.requestBody == null ? null : other.responseBody.copy();
        this.responseBody = other.responseBody == null ? null : other.responseBody.copy();
    }

    @Override
    public AurLogCriteria copy(){return new AurLogCriteria(this);}

    public LongFilter getLogId() {
        return logId;
    }

    public void setLogId(LongFilter logId) {
        this.logId = logId;
    }

    public StringFilter getServiceName() {
        return serviceName;
    }

    public void setServiceName(StringFilter serviceName) {
        this.serviceName = serviceName;
    }

    public StringFilter getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(StringFilter requestBody) {
        this.requestBody = requestBody;
    }

    public StringFilter getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(StringFilter responseBody) {
        this.responseBody = responseBody;
    }

    public StringFilter getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(StringFilter serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AurLogCriteria that = (AurLogCriteria) o;
        return Objects.equals(logId, that.logId) && Objects.equals(serviceName, that.serviceName) && Objects.equals(requestBody, that.requestBody) && Objects.equals(responseBody, that.responseBody) && Objects.equals(serviceUrl, that.serviceUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId, serviceName, requestBody, responseBody, serviceUrl);
    }

    @Override
    public String toString() {
        return "AurLogCriteria{" +
            "logId=" + logId +
            ", serviceName=" + serviceName +
            ", requestBody=" + requestBody +
            ", responseBody=" + responseBody +
            ", serviceUrl=" + serviceUrl +
            '}';
    }
}
