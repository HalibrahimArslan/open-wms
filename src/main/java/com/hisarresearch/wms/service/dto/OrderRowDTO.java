package com.hisarresearch.wms.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hisarresearch.wms.domain.OrderRow} entity.
 */
public class OrderRowDTO implements Serializable {

    private Long id;

    private Long productQuantity;

    private String stockCode;

    private Boolean status;

    private String stockName;

    private String barcode;

    private Double transferAmount;

    private Double receivingAmount;

    private OrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Double getReceivingAmount() {
        return receivingAmount;
    }

    public void setReceivingAmount(Double receivingAmount) {
        this.receivingAmount = receivingAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderRowDTO)) {
            return false;
        }

        OrderRowDTO orderRowDTO = (OrderRowDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderRowDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderRowDTO{" +
            "id=" + getId() +
            ", productQuantity=" + getProductQuantity() +
            ", stockCode='" + getStockCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", order=" + getOrder() +
            ", stockName= " + getStockName() +
            ", barcode= " + getBarcode() +
            "}";
    }
}
