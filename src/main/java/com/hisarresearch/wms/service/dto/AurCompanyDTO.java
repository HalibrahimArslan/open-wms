package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.ApiParameters;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AurCompanyDTO {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Integer companyCode;

    private String companyName;

    @JsonIgnore
    private ErpConnectionType erpType;

    @JsonIgnore
    private String apiEndPoint;

    @JsonIgnore
    private ApiParameters apiParameters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ErpConnectionType getErpType() {
        return erpType;
    }

    public void setErpType(ErpConnectionType erpType) {
        this.erpType = erpType;
    }

    public String getApiEndPoint() {
        return apiEndPoint;
    }

    public void setApiEndPoint(String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
    }

    public ApiParameters getApiParameters() {
        return apiParameters;
    }

    public void setApiParameters(ApiParameters apiParameters) {
        this.apiParameters = apiParameters;
    }
}
