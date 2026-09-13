package com.hisarresearch.wms.service.dto.address;

public class AurDepoUrunAdresDto {

    private Integer depoNo;
    private String companyCode;
    private String adresTipi;
    private String bolum;
    private String reyon;
    private String unite;
    private Short kat;
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

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getReyon() {
        return reyon;
    }

    public void setReyon(String reyon) {
        this.reyon = reyon;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Short getKat() {
        return kat;
    }

    public void setKat(Short kat) {
        this.kat = kat;
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
