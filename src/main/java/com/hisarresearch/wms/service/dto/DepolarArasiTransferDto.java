package com.hisarresearch.wms.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DepolarArasiTransferDto {
    int girisDepo;

    int cikisDepo;

    @NotNull
    String barcode;

    @NotNull
    String stokKodu;

    @NotNull
    Long urunAdresId;

    @Positive
    Double miktar;

    @NotBlank
    String description;

    public int getGirisDepo() {
        return girisDepo;
    }

    public void setGirisDepo(int girisDepo) {
        this.girisDepo = girisDepo;
    }

    public int getCikisDepo() {
        return cikisDepo;
    }

    public void setCikisDepo(int cikisDepo) {
        this.cikisDepo = cikisDepo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public Long getUrunAdresId() {
        return urunAdresId;
    }

    public void setUrunAdresId(Long urunAdresId) {
        this.urunAdresId = urunAdresId;
    }

    public Double getMiktar() {
        return miktar;
    }

    public void setMiktar(Double miktar) {
        this.miktar = miktar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
