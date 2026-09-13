package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.domain.User;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hisarresearch.wms.domain.UserDepoRel} entity.
 */
public class UserDepoRelDTO implements Serializable {

    private Long id;

    private User user;

    private Warehouse warehouse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDepoRelDTO)) {
            return false;
        }

        UserDepoRelDTO userDepoRelDTO = (UserDepoRelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userDepoRelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDepoRelDTO{" +
            "id=" + getId() +
            ", user=" + getUser() +
            "}";
    }
}
