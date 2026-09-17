package com.hisarresearch.wms.service.dto;

import java.util.List;

public class TransactionResponseByDocNoDto {
    private String stockCode;
    private String barcode;
    private String stockName;
    private Double orderAmount;
    private Double processAmount;
    private Double observerAmount;

    private List<AddressTransactionsDetailDto> transactionsDetailList;

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

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getProcessAmount() {
        return processAmount;
    }

    public void setProcessAmount(Double processAmount) {
        this.processAmount = processAmount;
    }

    public Double getObserverAmount() {
        return observerAmount;
    }

    public void setObserverAmount(Double observerAmount) {
        this.observerAmount = observerAmount;
    }

    public List<AddressTransactionsDetailDto> getTransactionsDetailList() {
        return transactionsDetailList;
    }

    public void setTransactionsDetailList(List<AddressTransactionsDetailDto> transactionsDetailList) {
        this.transactionsDetailList = transactionsDetailList;
    }
}
