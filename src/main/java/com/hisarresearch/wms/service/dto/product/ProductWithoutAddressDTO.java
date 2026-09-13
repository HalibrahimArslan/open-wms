package com.hisarresearch.wms.service.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class ProductWithoutAddressDTO {

    @NotNull
    private String barcode;

    @NotBlank
    private String companyCode;

    @NotBlank
    private String stokAdi;

    @NotBlank
    private String stokKodu;

    @NotNull
    private String anaGrup;

    @NotNull
    private String kategoriAdi;

    @NotBlank
    private String stokBirimi;

    private Double miktar;
    private String description;
    private Boolean sktFlag = false;
    private Boolean lotBasedTracking = false;
    private Map<String, Object> physicalAttributes;


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getAnaGrup() {
        return anaGrup;
    }

    public void setAnaGrup(String anaGrup) {
        this.anaGrup = anaGrup;
    }

    public String getKategoriAdi() {
        return kategoriAdi;
    }

    public void setKategoriAdi(String kategoriAdi) {
        this.kategoriAdi = kategoriAdi;
    }

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
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

    public Boolean getSktFlag() {
        return sktFlag;
    }

    public void setSktFlag(Boolean sktFlag) {
        this.sktFlag = sktFlag;
    }

    public Boolean getLotBasedTracking() {
        return lotBasedTracking;
    }

    public void setLotBasedTracking(Boolean lotBasedTracking) {
        this.lotBasedTracking = lotBasedTracking;
    }

    public Map<String, Object> getPhysicalAttributes() {
        return physicalAttributes;
    }

    public void setPhysicalAttributes(Map<String, Object> physicalAttributes) {
        this.physicalAttributes = physicalAttributes;
    }
}
