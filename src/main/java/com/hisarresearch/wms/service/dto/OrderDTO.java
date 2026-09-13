package com.hisarresearch.wms.service.dto;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.hisarresearch.wms.domain.Order} entity.
 */
public class OrderDTO implements Serializable {

    private Long id;

    private String orderNo;

    private Boolean isTransferred;

    private Integer entranceWarehosue;

    private Integer transferWarehouse;

    private String cancelReasonDetail;

    private Integer documentType;
    private String erpDocumentInfo;
    private Instant microTransferDate;

    private String microTransferBy;

    private Instant shipmentDate;

    private String shipmentBy;

    private Instant acceptanceDate;

    private String acceptanceBy;

    private String cariCode;

    private String cariName;

    private Instant createdDate;

    private String createdBy;
    private OrderStatusDTO orderStatus;

    private List<OrderRowDTO> orderRows;

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

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OrderStatusDTO getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusDTO orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderRowDTO> getOrderRows() {
        return orderRows;
    }

    public void setOrderRows(List<OrderRowDTO> orderRows) {
        this.orderRows = orderRows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + id +
            ", orderNo='" + orderNo + '\'' +
            ", isTransferred=" + isTransferred +
            ", entranceWarehosue=" + entranceWarehosue +
            ", transferWarehouse=" + transferWarehouse +
            ", cancelReasonDetail='" + cancelReasonDetail + '\'' +
            ", documentType=" + documentType +
            ", erpDocumentInfo='" + erpDocumentInfo + '\'' +
            ", microTransferDate=" + microTransferDate +
            ", microTransferBy='" + microTransferBy + '\'' +
            ", shipmentDate=" + shipmentDate +
            ", shipmentBy='" + shipmentBy + '\'' +
            ", acceptanceDate=" + acceptanceDate +
            ", acceptanceBy='" + acceptanceBy + '\'' +
            ", cariCode='" + cariCode + '\'' +
            ", cariName='" + cariName + '\'' +
            ", orderStatus=" + orderStatus +
            ", orderRows=" + orderRows +
            '}';
    }

    // prettier-ignore

}
