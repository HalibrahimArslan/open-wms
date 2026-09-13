package com.hisarresearch.wms.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AurCompanyDTO {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Integer companyCode;

    private String companyName;

    @JsonIgnore
    private String erpTipi;

    @JsonIgnore
    private String apiEndPoint;

    @JsonIgnore
    private String apiParameters;

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

    public String getErpTipi() {
        return erpTipi;
    }

    public void setErpTipi(String erpTipi) {
        this.erpTipi = erpTipi;
    }

    public String getApiEndPoint() {
        return apiEndPoint;
    }

    public void setApiEndPoint(String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
    }

    public String getApiParameters() {
        return apiParameters;
    }

    public void setApiParameters(String apiParameters) {
        this.apiParameters = apiParameters;
    }
}
