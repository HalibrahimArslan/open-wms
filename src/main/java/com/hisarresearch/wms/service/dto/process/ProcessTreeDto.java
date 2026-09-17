package com.hisarresearch.wms.service.dto.process;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.ProcessType;

import javax.validation.constraints.Null;
import java.util.List;

public class ProcessTreeDto {
    private Long id;
    private Long userId;

    private Boolean status;

    private ProcessType processType;

    private AurDepoUrunAdres targetAddress;

    private Double amount;

    private Long processChildId;

    private Boolean isPublic;

    private Long depoCode;

    private List<ProcessLeafDto> processLeaves;

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

    public AurDepoUrunAdres getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(AurDepoUrunAdres targetAddress) {
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

    public List<ProcessLeafDto> getProcessLeaves() {
        return processLeaves;
    }

    public void setProcessLeaves(List<ProcessLeafDto> processLeaves) {
        this.processLeaves = processLeaves;
    }
}
