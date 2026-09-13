package com.hisarresearch.wms.service.dto;

import java.util.List;

public class PalletOrderRelIdDto {

    private String stockCode;

    private List<Long> aurTmpDetailIds;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public List<Long> getAurTmpDetailIds() {
        return aurTmpDetailIds;
    }

    public void setAurTmpDetailIds(List<Long> aurTmpDetailIds) {
        this.aurTmpDetailIds = aurTmpDetailIds;
    }
}
