package com.hisarresearch.wms.service.dto;

public class MicroSipParameterDetailDTO {
    private String sipStokKod;
    private Double sipMiktar;
    private Double sipTeslimMiktar;
    private String stoIsim;
    private String barKodu;
    public String getSipStokKod() {
        return sipStokKod;
    }

    public void setSipStokKod(String sipStokKod) {
        this.sipStokKod = sipStokKod;
    }

    public Double getSipMiktar() {
        return sipMiktar;
    }

    public void setSipMiktar(Double sipMiktar) {
        this.sipMiktar = sipMiktar;
    }

    public Double getSipTeslimMiktar() {
        return sipTeslimMiktar;
    }

    public void setSipTeslimMiktar(Double sipTeslimMiktar) {
        this.sipTeslimMiktar = sipTeslimMiktar;
    }

    public String getBarKodu() {
        return barKodu;
    }

    public void setBarKodu(String barKodu) {
        this.barKodu = barKodu;
    }

    public String getStoIsim() {
        return stoIsim;
    }

    public void setStoIsim(String stoIsim) {
        this.stoIsim = stoIsim;
    }
}
