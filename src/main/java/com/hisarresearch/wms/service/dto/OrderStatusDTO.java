package com.hisarresearch.wms.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hisarresearch.wms.domain.OrderStatus} entity.
 */
public class OrderStatusDTO implements Serializable {

    private Long id;

    private String status;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderStatusDTO)) {
            return false;
        }

        OrderStatusDTO orderStatusDTO = (OrderStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderStatusDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
