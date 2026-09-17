package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.User;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hisarresearch.wms.domain.UserFirmRel} entity.
 */
public class UserFirmRelDTO implements Serializable {

    private Long id;

    private String firmCode;

    private String firmName;

    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserFirmRelDTO)) {
            return false;
        }

        UserFirmRelDTO userFirmRelDTO = (UserFirmRelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userFirmRelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserFirmRelDTO{" +
            "id=" + getId() +
            ", firmCode='" + getFirmCode() + "'" +
            ", firmName='" + getFirmName() + "'" +
            ", userId=" + getUser() +
            "}";
    }
}
