package com.hisarresearch.wms.service.dto.barcode;

public class PalletInfoDTO {
    private String stockCode;
    private String barcode;
    private Double amount;
    private Long palletBarcodeOrderRelId;
    private String cariName;
    private String orderNo;

    private Long aurTmpDetailId;

    private String stockName;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getPalletBarcodeOrderRelId() {
        return palletBarcodeOrderRelId;
    }

    public void setPalletBarcodeOrderRelId(Long palletBarcodeOrderRelId) {
        this.palletBarcodeOrderRelId = palletBarcodeOrderRelId;
    }

    public String getCariName() {
        return cariName;
    }

    public void setCariName(String cariName) {
        this.cariName = cariName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getAurTmpDetailId() {
        return aurTmpDetailId;
    }

    public void setAurTmpDetailId(Long aurTmpDetailId) {
        this.aurTmpDetailId = aurTmpDetailId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
