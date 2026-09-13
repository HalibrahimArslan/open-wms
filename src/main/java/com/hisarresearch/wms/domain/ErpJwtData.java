package com.hisarresearch.wms.domain;

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

    @Column(name = "erp_tipi")
    private String erpTipi;

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

    public String getErpTipi() {
        return erpTipi;
    }

    public void setErpTipi(String erpTipi) {
        this.erpTipi = erpTipi;
    }

    @Override
    public String toString() {
        return "ErpJwtData{" +
            "id=" + id +
            ", token='" + token + '\'' +
            ", erpTipi=" + erpTipi + '\''+
            '}';
    }
}
