package com.hisarresearch.wms.service.dto;

import javax.validation.constraints.NotNull;

public class WarehouseDTO {

    private Long id;

    private String companyCode;

    private Boolean isReal;

    private Boolean countable;

    @NotNull
    private String transferCode;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private Boolean autoScan = false;


    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getReal() {
        return isReal;
    }

    public void setReal(Boolean real) {
        isReal = real;
    }

    public Boolean getCountable() {
        return countable;
    }

    public void setCountable(Boolean countable) {
        this.countable = countable;
    }

    public @NotNull String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(@NotNull String transferCode) {
        this.transferCode = transferCode;
    }

    public @NotNull String getCode() {
        return code;
    }

    public void setCode(@NotNull String code) {
        this.code = code;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull Boolean getAutoScan() {return autoScan;}

    public void setAutoScan(Boolean autoScan) {this.autoScan = autoScan;}
}
