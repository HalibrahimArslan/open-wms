package com.hisarresearch.wms.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "jhi_user_authority")
@IdClass(UserAuthorityId.class)
public class UserAuthority implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "authority_name")
    private Authority authority;

    // Constructors, getters, and setters
    public UserAuthority() {
    }

    public UserAuthority(User user, Authority authority) {
        this.user = user;
        this.authority = authority;
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
