package com.hisarresearch.wms.service.dto;

public class CountingAddressExceptionDto {
    private Long id;

    private Long addressId;

    private Boolean status = true;

    private Long countingDefinitionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getCountingDefinitionId() {
        return countingDefinitionId;
    }

    public void setCountingDefinitionId(Long countingDefinitionId) {
        this.countingDefinitionId = countingDefinitionId;
    }
}
