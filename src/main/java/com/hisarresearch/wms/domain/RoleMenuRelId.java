package com.hisarresearch.wms.domain;

import java.io.Serializable;
import java.util.Objects;

public class RoleMenuRelId implements Serializable {
    private Long menu;
    private Long role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleMenuRelId that = (RoleMenuRelId) o;
        return Objects.equals(menu, that.menu) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, role);
    }

    public Long getMenu() {
        return menu;
    }

    public void setMenu(Long menu) {
        this.menu = menu;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RoleMenuRelId{" +
            "menu=" + menu +
            ", role=" + role +
            '}';
    }
}
