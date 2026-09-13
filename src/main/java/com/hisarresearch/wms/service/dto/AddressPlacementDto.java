package com.hisarresearch.wms.service.dto;

public class AddressPlacementDto {

    private String stokKodu;
    private String barcode;
    private Long placementAddressId;
    private Long originAddressId;
    private String depoCode;
    private Double changeAmount;

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getPlacementAddressId() {
        return placementAddressId;
    }

    public void setPlacementAddressId(Long placementAddressId) {
        this.placementAddressId = placementAddressId;
    }

    public Long getOriginAddressId() {
        return originAddressId;
    }

    public void setOriginAddressId(Long originAddressId) {
        this.originAddressId = originAddressId;
    }

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }

    public Double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    @Override
    public String toString() {
        return "AddressPlacementDto{" +
            "stokKodu='" + stokKodu + '\'' +
            ", barcode='" + barcode + '\'' +
            ", placementAddressId=" + placementAddressId +
            ", originAddressId=" + originAddressId +
            ", depoCode='" + depoCode + '\'' +
            ", changeAmount=" + changeAmount +
            '}';
    }
}
