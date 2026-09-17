package com.hisarresearch.wms.service.dto;

import java.util.List;

public class ProductInfoRequestDto {
    private String stokKodu;
    private String stokAdi;
    private String barkod;
    private List<String> barkodList;
    private Integer depoNo;

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public List<String> getBarkodList() {
        return barkodList;
    }

    public void setBarkodList(List<String> barkodList) {
        this.barkodList = barkodList;
    }

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }
}
