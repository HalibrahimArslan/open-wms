package com.hisarresearch.wms.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AurUserRoleRel.
 */
@Entity
@Table(name = "aur_user_role_rel")
@IdClass(UserRoleRelId.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurUserRoleRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AurUser user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private AurRole role;
    public AurUserRoleRel() {

    }

    public AurUserRoleRel(AurUser user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AurUserRoleRel that = (AurUserRoleRel) o;
        return Objects.equals(user, that.user) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }

    public AurUser getUser() {
        return user;
    }

    public void setUser(AurUser user) {
        this.user = user;
    }

    public AurRole getRole() {
        return role;
    }

    public void setRole(AurRole role) {
        this.role = role;
    }

}
