package com.hisarresearch.wms.domain;

import java.io.Serializable;
import java.util.Objects;

public class UserAuthorityId implements Serializable {
    private Long user;
    private String authority;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthorityId that = (UserAuthorityId) o;
        return Objects.equals(user, that.user) && Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, authority);
    }

    @Override
    public String toString() {
        return "UserAuthorityId{" +
            "user=" + user +
            ", authority=" + authority +
            '}';
    }
}
