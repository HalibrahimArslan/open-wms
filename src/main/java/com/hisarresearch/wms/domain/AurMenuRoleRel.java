package com.hisarresearch.wms.domain;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AurMenuRoleRel.
 */
@Entity
@Table(name = "aur_menu_role_rel")
@IdClass(RoleMenuRelId.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurMenuRoleRel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private AurMenu menu;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private AurRole role;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AurMenuRoleRel that = (AurMenuRoleRel) o;
        return Objects.equals(menu, that.menu) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, role);
    }

    public AurMenu getMenu() {
        return menu;
    }

    public void setMenu(AurMenu menu) {
        this.menu = menu;
    }

    public AurRole getRole() {
        return role;
    }

    public void setRole(AurRole role) {
        this.role = role;
    }
}
