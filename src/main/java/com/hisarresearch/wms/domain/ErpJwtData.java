package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;

@Entity
@Table(name = "erp_jwt_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ErpJwtData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "erpJwtDataGenerator")
    @SequenceGenerator(name = "erpJwtDataGenerator",sequenceName = "erp_jwt_data_seq",allocationSize = 1)
    private Long id;

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "erp_type")
    private ErpConnectionType erpType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ErpConnectionType getErpType() {
        return erpType;
    }

    public void setErpType(ErpConnectionType erpType) {
        this.erpType = erpType;
    }

    @Override
    public String toString() {
        return "ErpJwtData{" +
            "id=" + id +
            ", token='" + token + '\'' +
            ", erpType=" + erpType + '\''+
            '}';
    }
}
