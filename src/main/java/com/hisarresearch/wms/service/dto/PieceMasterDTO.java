package com.hisarresearch.wms.service.dto;

public class PieceMasterDTO {
    String stokKodu;
    String stokAdi;
    String barcode;
    Boolean reserve = false;
    String reserveDescription;

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Boolean getReserve() {
        return reserve;
    }

    public void setReserve(Boolean reserve) {
        this.reserve = reserve;
    }

    public String getReserveDescription() {
        return reserveDescription;
    }

    public void setReserveDescription(String reserveDescription) {
        this.reserveDescription = reserveDescription;
    }
}
