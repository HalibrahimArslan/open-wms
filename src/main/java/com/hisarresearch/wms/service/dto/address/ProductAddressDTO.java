package com.hisarresearch.wms.service.dto.address;

import java.util.Objects;

public class ProductAddressDTO {

    private Long id;

    private String barcode;

    private Double miktar;

    private Boolean isPiece;

    private Long partialItemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getMiktar() {
        return miktar;
    }

    public void setMiktar(Double miktar) {
        this.miktar = miktar;
    }

    public Boolean getPiece() {
        return isPiece;
    }

    public void setPiece(Boolean piece) {
        isPiece = piece;
    }

    public Long getPartialItemId() {
        return partialItemId;
    }

    public void setPartialItemId(Long partialItemId) {
        this.partialItemId = partialItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductAddressDTO that = (ProductAddressDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProductAddressDTO{" +
            "id=" + id +
            ", barcode='" + barcode + '\'' +
            ", miktar=" + miktar +
            '}';
    }
}
