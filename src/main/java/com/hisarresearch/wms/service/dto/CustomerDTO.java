package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.enumeration.FirmConnectionType;

import javax.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class CustomerDTO {
    @Nullable
    private Long id;

    @NotNull
    private String customerCode;

    private int districtCode;

    private String mail;

    @NotNull
    private String companyCode;

    private FirmConnectionType connectionType;
    private String customerName;
    private String districtName;


    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public @NotNull String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(@NotNull String customerCode) {
        this.customerCode = customerCode;
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public @NotNull String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(@NotNull String companyCode) {
        this.companyCode = companyCode;
    }

    public FirmConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(FirmConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
