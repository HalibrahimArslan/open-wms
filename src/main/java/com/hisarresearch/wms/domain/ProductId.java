package com.hisarresearch.wms.domain;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductId implements Serializable {
    @Column(name = "barkod")
    private String barkod;

    @Column(name = "company_code")
    private String companyCode;

    public ProductId() {}

    public ProductId(String barkod, String companyCode) {
        this.barkod = barkod;
        this.companyCode = companyCode;
    }

    // Getters and setters
    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId that = (ProductId) o;
        return Objects.equals(barkod, that.barkod) &&
            Objects.equals(companyCode, that.companyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barkod, companyCode);
    }
}
