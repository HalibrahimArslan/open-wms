package com.hisarresearch.wms.service.dto.event;

public class OrderMailEvent {
    private Long orderId;

    public OrderMailEvent() {
    }

    public OrderMailEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
