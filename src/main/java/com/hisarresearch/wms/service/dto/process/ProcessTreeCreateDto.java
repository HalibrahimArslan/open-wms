package com.hisarresearch.wms.service.dto.process;

import com.hisarresearch.wms.domain.enumeration.ProcessType;

public class ProcessTreeCreateDto {
    private Long id;
    private Long userId;
    private Boolean status;
    private ProcessType processType;
    private Long targetAddress;
    private Double amount;
    private Long processChildId;
    private Boolean isPublic;
    private Long depoCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public Long getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(Long targetAddress) {
        this.targetAddress = targetAddress;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getProcessChildId() {
        return processChildId;
    }

    public void setProcessChildId(Long processChildId) {
        this.processChildId = processChildId;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Long getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(Long depoCode) {
        this.depoCode = depoCode;
    }

}
