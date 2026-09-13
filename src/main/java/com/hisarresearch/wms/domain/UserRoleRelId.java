package com.hisarresearch.wms.domain;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleRelId implements Serializable {
    private Long user;

    private Long role;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleRelId that = (UserRoleRelId) o;
        return Objects.equals(user, that.user) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }

    @Override
    public String toString() {
        return "UserRoleRelId{" +
            "user=" + user +
            ", role=" + role +
            '}';
    }
}
