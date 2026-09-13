package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.websocket.ClientEndpoint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderRow.
 */
@Entity
@Table(name = "order_row")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderRow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderRowSequenceGenerator")
    @SequenceGenerator(name = "orderRowSequenceGenerator",sequenceName = "order_row_seq",allocationSize = 1)
    private Long id;

    @Column(name = "product_quantity")
    private Long productQuantity;

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "status")
    private Boolean status;

    @Column(name= "transfer_amount")
    private Double transferAmount;

    @Column(name = "receiving_amount")
    private Double receivingAmount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderStatus", "orderRows" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderRow id(Long id) {
        this.id = id;
        return this;
    }

    public Long getProductQuantity() {
        return this.productQuantity;
    }

    public OrderRow productQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
        return this;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getStockCode() {
        return this.stockCode;
    }

    public OrderRow stockCode(String stockCode) {
        this.stockCode = stockCode;
        return this;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public OrderRow status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public Order getOrder() {
        return this.order;
    }

    public OrderRow order(Order order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderRow)) {
            return false;
        }
        return id != null && id.equals(((OrderRow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderRow{" +
            "id=" + getId() +
            ", productQuantity=" + getProductQuantity() +
            ", stockCode='" + getStockCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", stockName='" + getStockName() + "'" +
            ",barcode='" + getBarcode() + "'" +
            "}";
    }
}
