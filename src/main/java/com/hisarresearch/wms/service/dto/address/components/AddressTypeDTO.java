package com.hisarresearch.wms.service.dto.address.components;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressTypeDTO {
    private Long id;

    @NotNull
    @Size(min = 1, max = 25)
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

    public @NotNull @Size(min = 1, max = 25) String getCode() {
        return code;
    }

    public void setCode(@NotNull @Size(min = 1, max = 25) String code) {
        this.code = code;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    public @NotNull Boolean getStatus() {
        return status;
    }

    public void setStatus(@NotNull Boolean status) {
        this.status = status;
    }

    public @NotNull String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(@NotNull String companyCode) {
        this.companyCode = companyCode;
    }

    public @NotNull String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(@NotNull String depoCode) {
        this.depoCode = depoCode;
    }
}
