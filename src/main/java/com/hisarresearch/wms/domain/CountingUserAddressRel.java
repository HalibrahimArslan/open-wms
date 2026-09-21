package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "counting_user_address_rel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountingUserAddressRel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countingUserAddressRelGenerator")
    @SequenceGenerator(name = "countingUserAddressRelGenerator", sequenceName = "counting_user_address_rel_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private AurUser user;

    @ManyToOne
    private AurDepoUrunAdres address;

    @ManyToOne
    private AurSayimTanim countingDefinition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurUser getUser() {
        return user;
    }

    public void setUser(AurUser user) {
        this.user = user;
    }

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

    public AurSayimTanim getCountingDefinition() {
        return countingDefinition;
    }

    public void setCountingDefinition(AurSayimTanim countingDefinition) {
        this.countingDefinition = countingDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountingUserAddressRel)) return false;
        CountingUserAddressRel that = (CountingUserAddressRel) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(address, that.address) && Objects.equals(countingDefinition, that.countingDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, address, countingDefinition);
    }

    @Override
    public String toString() {
        return "CountingUserAddressRel{" +
            "id=" + id +
            ", user=" + user +
            ", address=" + address +
            ", countingDefinition=" + countingDefinition +
            '}';
    }
}
