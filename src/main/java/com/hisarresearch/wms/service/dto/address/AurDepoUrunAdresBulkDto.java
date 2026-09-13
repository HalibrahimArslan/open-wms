package com.hisarresearch.wms.service.dto.address;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AurDepoUrunAdresBulkDto {

    private Integer depoNo;
    private String companyCode;
    private String adresTipi;
    private String bolumStart;
    private String bolumFinish;
    @JsonIgnore
    private String reyon;
    private String hucreStart;
    private String hucreFinish;
    private Short katStart;
    private Short katFinish;
    private Boolean geciciAdres;
    private Boolean toplamaGozu;
    private Boolean kontrolAdres;

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getAdresTipi() {
        return adresTipi;
    }

    public void setAdresTipi(String adresTipi) {
        this.adresTipi = adresTipi;
    }

    public String getBolumStart() {
        return bolumStart;
    }

    public void setBolumStart(String bolumStart) {
        this.bolumStart = bolumStart;
    }

    public String getBolumFinish() {
        return bolumFinish;
    }

    public void setBolumFinish(String bolumFinish) {
        this.bolumFinish = bolumFinish;
    }

    public String getReyon() {
        return reyon;
    }

    public void setReyon(String reyon) {
        this.reyon = reyon;
    }

    public String getHucreStart() {
        return hucreStart;
    }

    public void setHucreStart(String hucreStart) {
        this.hucreStart = hucreStart;
    }

    public String getHucreFinish() {
        return hucreFinish;
    }

    public void setHucreFinish(String hucreFinish) {
        this.hucreFinish = hucreFinish;
    }

    public Short getKatStart() {
        return katStart;
    }

    public void setKatStart(Short katStart) {
        this.katStart = katStart;
    }

    public Short getKatFinish() {
        return katFinish;
    }

    public void setKatFinish(Short katFinish) {
        this.katFinish = katFinish;
    }

    public Boolean getGeciciAdres() {
        return geciciAdres;
    }

    public void setGeciciAdres(Boolean geciciAdres) {
        this.geciciAdres = geciciAdres;
    }

    public Boolean getToplamaGozu() {
        return toplamaGozu;
    }

    public void setToplamaGozu(Boolean toplamaGozu) {
        this.toplamaGozu = toplamaGozu;
    }

    public Boolean getKontrolAdres() {
        return kontrolAdres;
    }

    public void setKontrolAdres(Boolean kontrolAdres) {
        this.kontrolAdres = kontrolAdres;
    }
}
