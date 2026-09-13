package com.hisarresearch.wms.service.dto.address;

public class AurDepoUrunAdresUpdateDto {

    private String adres;
    private Boolean toplamaGozu;
    private Boolean geciciAdres;
    private Boolean kontrolAdres;
    private Boolean status;
    private String adresTipi;

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public Boolean getToplamaGozu() {
        return toplamaGozu;
    }

    public void setToplamaGozu(Boolean toplamaGozu) {
        this.toplamaGozu = toplamaGozu;
    }

    public Boolean getGeciciAdres() {
        return geciciAdres;
    }

    public void setGeciciAdres(Boolean geciciAdres) {
        this.geciciAdres = geciciAdres;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAdresTipi() {
        return adresTipi;
    }

    public void setAdresTipi(String adresTipi) {
        this.adresTipi = adresTipi;
    }

    public Boolean getKontrolAdres() {
        return kontrolAdres;
    }

    public void setKontrolAdres(Boolean kontrolAdres) {
        this.kontrolAdres = kontrolAdres;
    }
}
