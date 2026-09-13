package com.hisarresearch.wms.service.dto.counting;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;

import java.time.Instant;
import java.util.Map;

public class CountingSummaryDTO {
    private String barcode;
    private String stokKod; // Add this field
    private AurDepoUrunAdres address;
    private double totalMiktar;
    private Map<Instant, Double> sktDateMiktarMap;

    public CountingSummaryDTO(String barcode, String stokKod, AurDepoUrunAdres address, double totalMiktar, Map<Instant, Double> sktDateMiktarMap) {
        this.barcode = barcode;
        this.stokKod = stokKod;
        this.address = address;
        this.totalMiktar = totalMiktar;
        this.sktDateMiktarMap = sktDateMiktarMap;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStokKod() {
        return stokKod;
    }

    public void setStokKod(String stokKod) {
        this.stokKod = stokKod;
    }

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

    public double getTotalMiktar() {
        return totalMiktar;
    }

    public void setTotalMiktar(double totalMiktar) {
        this.totalMiktar = totalMiktar;
    }

    public Map<Instant, Double> getSktDateMiktarMap() {
        return sktDateMiktarMap;
    }

    public void setSktDateMiktarMap(Map<Instant, Double> sktDateMiktarMap) {
        this.sktDateMiktarMap = sktDateMiktarMap;
    }
}
