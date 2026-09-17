package com.hisarresearch.wms.service.dto.productaddress;

import com.hisarresearch.wms.domain.ProductAddressSkt;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class ProductAddressDefinitionDTO {

    @NotNull
    private String depoNo;

    @NotNull
    @NotBlank
    private String barcode;

    @NotNull
    private long urunAdresId;

    @NotNull
    @PositiveOrZero
    private Double miktar;

    private String stokKodu;

    @NotNull
    @NotBlank
    private String stokAdi;

    @Nullable
    private List<ProductAddressSkt> sktDateList;

    public @NotNull String getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(@NotNull String depoNo) {
        this.depoNo = depoNo;
    }

    public @NotNull @NotBlank String getBarcode() {
        return barcode;
    }

    public void setBarcode(@NotNull @NotBlank String barcode) {
        this.barcode = barcode;
    }

    @NotNull
    public long getUrunAdresId() {
        return urunAdresId;
    }

    public void setUrunAdresId(@NotNull long urunAdresId) {
        this.urunAdresId = urunAdresId;
    }

    public @PositiveOrZero Double getMiktar() {
        return miktar;
    }

    public void setMiktar(@PositiveOrZero Double miktar) {
        this.miktar = miktar;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public @NotNull @NotBlank String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(@NotNull @NotBlank String stokAdi) {
        this.stokAdi = stokAdi;
    }

    @Nullable
    public List<ProductAddressSkt> getSktDateList() {
        return sktDateList;
    }

    public void setSktDateList(@Nullable List<ProductAddressSkt> sktDateList) {
        this.sktDateList = sktDateList;
    }
}
