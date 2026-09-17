package com.hisarresearch.wms.service.dto.mikro;

public class MicroOrderDetailDto {
    private String stokKodu;
    private double hareketMiktar;

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public double getHareketMiktar() {
        return hareketMiktar;
    }

    public void setHareketMiktar(double hareketMiktar) {
        this.hareketMiktar = hareketMiktar;
    }
}
