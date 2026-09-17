package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hisarresearch.wms.domain.AurPartialDetails} entity.
 */
public class AurPartialDetailsDTO implements Serializable {

    private Long id;

    private String stockCode;

    private Double quantity;

    private String barcode;

    private String stockName;

    private AurPartialItemDTO aurPartialItem;

    private ProductWithoutAddressDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public AurPartialItemDTO getAurPartialItem() {
        return aurPartialItem;
    }

    public void setAurPartialItem(AurPartialItemDTO aurPartialItem) {
        this.aurPartialItem = aurPartialItem;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public ProductWithoutAddressDTO getProduct() {
        return product;
    }

    public void setProduct(ProductWithoutAddressDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurPartialDetailsDTO)) {
            return false;
        }

        AurPartialDetailsDTO aurPartialDetailsDTO = (AurPartialDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aurPartialDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurPartialDetailsDTO{" +
            "id=" + getId() +
            ", stockCode='" + getStockCode() + "'" +
            ", quantity=" + getQuantity() +
            ", barcode=" + getBarcode() +
            ", stockName=" + getStockName() +
            ", aurPartialItem=" + getAurPartialItem() +
            "}";
    }
}
