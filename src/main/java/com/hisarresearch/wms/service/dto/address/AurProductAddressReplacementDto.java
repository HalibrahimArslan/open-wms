package com.hisarresearch.wms.service.dto.address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AurProductAddressReplacementDto {

    @NotNull
    private Integer depoNo;

    @NotNull
    private String barcode;

    @NotNull
    private long oldUrunAdresId;

    @NotNull
    private long newUrunAdresId;

    @NotNull
    @Positive
    private Double miktar;

    private String stokKodu;

    public @NotNull Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(@NotNull Integer depoNo) {
        this.depoNo = depoNo;
    }

    public @NotNull String getBarcode() {
        return barcode;
    }

    public void setBarcode(@NotNull String barcode) {
        this.barcode = barcode;
    }

    @NotNull
    public long getOldUrunAdresId() {
        return oldUrunAdresId;
    }

    public void setOldUrunAdresId(@NotNull long oldUrunAdresId) {
        this.oldUrunAdresId = oldUrunAdresId;
    }

    @NotNull
    public long getNewUrunAdresId() {
        return newUrunAdresId;
    }

    public void setNewUrunAdresId(@NotNull long newUrunAdresId) {
        this.newUrunAdresId = newUrunAdresId;
    }

    public @NotNull @Positive Double getMiktar() {
        return miktar;
    }

    public void setMiktar(@NotNull @Positive Double miktar) {
        this.miktar = miktar;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }
}
