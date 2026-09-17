package com.hisarresearch.wms.service.dto.address.components;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddressDepartmentDTO {
    private Long id;

    @NotNull
    @Size(min = 1, max = 2)
    private String code;

    @NotNull
    private String description;

    @NotNull
    private Boolean status;

    @NotNull
    private String companyCode;

    @NotNull
    private String depoCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }
}
