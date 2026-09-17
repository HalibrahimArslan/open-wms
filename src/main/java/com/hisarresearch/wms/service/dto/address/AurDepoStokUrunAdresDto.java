package com.hisarresearch.wms.service.dto.address;

import java.util.List;

public class AurDepoStokUrunAdresDto {

    private List<String> stockList;

    private String depoCode;

    public List<String> getStockList() {
        return stockList;
    }

    public void setStockList(List<String> stockList) {
        this.stockList = stockList;
    }

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }
}
