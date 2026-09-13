package com.hisarresearch.wms.service.dto.counting;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;

import java.util.Objects;

public class CountingSummaryKeyDTO {
    private String barcode;
    private String stokKod;
    private AurDepoUrunAdres address;

    public CountingSummaryKeyDTO(String barcode, String stokKod, AurDepoUrunAdres address) {
        this.barcode = barcode;
        this.stokKod = stokKod;
        this.address = address;
    }

    // Getters and Setters
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountingSummaryKeyDTO that = (CountingSummaryKeyDTO) o;
        return Objects.equals(barcode, that.barcode) &&
            Objects.equals(stokKod, that.stokKod) &&
            Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barcode, stokKod, address);
    }
}
