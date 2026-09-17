package com.hisarresearch.wms.service.dto.process;

import com.hisarresearch.wms.domain.ProcessTree;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProcessLeafDto {
    private Long id;
    @JsonIgnore
    private ProcessTree process;
    private Boolean status;
    private String barcode;
    private AurDepoUrunAdres address;
    private Double amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessTree getProcess() {
        return process;
    }

    public void setProcess(ProcessTree process) {
        this.process = process;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
