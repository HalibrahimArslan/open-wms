package com.hisarresearch.wms.service.dto.barcode;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class PalletDetailDto {
    private List<Long> palletBarcodeOrderRelId;
    private String stockCode;
    private String stockName;
    private Double quantity;
    private Boolean status = true;
    public List<Long> getPalletBarcodeOrderRelId() {
        return palletBarcodeOrderRelId;
    }

    public void setPalletBarcodeOrderRelId(List<Long> palletBarcodeOrderRelId) {
        this.palletBarcodeOrderRelId = palletBarcodeOrderRelId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }
}
