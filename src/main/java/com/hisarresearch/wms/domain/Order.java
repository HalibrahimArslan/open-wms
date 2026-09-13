package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order extends AbstractAuditingEntityWithoutJsonIgnore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSequenceGenerator")
    @SequenceGenerator(name = "orderSequenceGenerator",sequenceName = "order_seq",allocationSize = 1)
    private Long id;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "is_transferred")
    private Boolean isTransferred;

    @Column(name = "entrance_warehosue")
    private Integer entranceWarehosue;

    @Column(name = "transfer_warehouse")
    private Integer transferWarehouse;

    @Column(name = "cancel_reason_detail")
    private String cancelReasonDetail;

    @Column(name = "document_type")
    private Integer documentType;

    @Column(name = "erp_document_info")
    private String erpDocumentInfo;

    @Column(name = "cari_code")
    private String cariCode;

    @Column(name = "cari_name")
    private String cariName;

    @Column(name = "micro_transfer_date")
    private Instant microTransferDate;

    @Column(name = "micro_transfer_by")
    private String microTransferBy;

    @Column(name = "shipment_date")
    private Instant shipmentDate;

    @Column(name = "shipment_by")
    private String shipmentBy;

    @Column(name = "acceptance_date")
    private Instant acceptanceDate;

    @Column(name = "acceptance_by")
    private String acceptanceBy;

    @ManyToOne(cascade = CascadeType.MERGE)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<OrderRow> orderRows = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getIsTransferred() {
        return isTransferred;
    }

    public void setIsTransferred(Boolean transferred) {
        isTransferred = transferred;
    }

    public Integer getEntranceWarehosue() {
        return entranceWarehosue;
    }

    public void setEntranceWarehosue(Integer entranceWarehosue) {
        this.entranceWarehosue = entranceWarehosue;
    }

    public Integer getTransferWarehouse() {
        return transferWarehouse;
    }

    public void setTransferWarehouse(Integer transferWarehouse) {
        this.transferWarehouse = transferWarehouse;
    }

    public String getCancelReasonDetail() {
        return cancelReasonDetail;
    }

    public void setCancelReasonDetail(String cancelReasonDetail) {
        this.cancelReasonDetail = cancelReasonDetail;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public String getErpDocumentInfo() {
        return erpDocumentInfo;
    }

    public void setErpDocumentInfo(String erpDocumentInfo) {
        this.erpDocumentInfo = erpDocumentInfo;
    }

    public String getCariCode() {
        return cariCode;
    }

    public void setCariCode(String cariCode) {
        this.cariCode = cariCode;
    }

    public String getCariName() {
        return cariName;
    }

    public void setCariName(String cariName) {
        this.cariName = cariName;
    }

    public Instant getMicroTransferDate() {
        return microTransferDate;
    }

    public void setMicroTransferDate(Instant microTransferDate) {
        this.microTransferDate = microTransferDate;
    }

    public String getMicroTransferBy() {
        return microTransferBy;
    }

    public void setMicroTransferBy(String microTransferBy) {
        this.microTransferBy = microTransferBy;
    }

    public Instant getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Instant shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentBy() {
        return shipmentBy;
    }

    public void setShipmentBy(String shipmentBy) {
        this.shipmentBy = shipmentBy;
    }

    public Instant getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Instant acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public String getAcceptanceBy() {
        return acceptanceBy;
    }

    public void setAcceptanceBy(String acceptanceBy) {
        this.acceptanceBy = acceptanceBy;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<OrderRow> getOrderRows() {
        return orderRows;
    }

    public Order addOrderRow(OrderRow orderRow) {
        this.orderRows.add(orderRow);
        orderRow.setOrder(this);
        return this;
    }

    public Order removeOrderRow(OrderRow orderRow) {
        this.orderRows.remove(orderRow);
        orderRow.setOrder(null);
        return this;
    }

    public void setOrderRows(Set<OrderRow> orderRows) {
        if (this.orderRows != null) {
            this.orderRows.forEach(i -> i.setOrder(null));
        }
        if (orderRows != null) {
            orderRows.forEach(i -> i.setOrder(this));
        }
        this.orderRows = orderRows;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderNo='" + getOrderNo() + "'" +
            ", isTransferred='" + getIsTransferred() + "'" +
            ", entranceWarehosue=" + getEntranceWarehosue() +
            ", transferWarehouse=" + getTransferWarehouse() +
            ", cancelReasonDetail='" + getCancelReasonDetail() + "'" +
            ", documentType=" + getDocumentType() +
            "}";
    }
}
