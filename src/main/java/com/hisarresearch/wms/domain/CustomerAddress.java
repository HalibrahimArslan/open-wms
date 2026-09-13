package com.hisarresearch.wms.domain;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "customer_address")
public class CustomerAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerAddressGenerator")
    @SequenceGenerator(name = "customerAddressGenerator", sequenceName = "customer_address_seq", allocationSize = 1)
    private Long id;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "cari_code")
    private String cariCode;

    @Column(name = "sevk_address")
    private String sevkAddress;

    @Column(name = "sevk_tel")
    private String sevkTel;

    @Column(name = "sevk_muhatap")
    private String sevkMuhatap;

    @Column(name = "sevk_acik_address")
    private String sevkAcikAdress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getCariCode() {
        return cariCode;
    }

    public void setCariCode(String cariCode) {
        this.cariCode = cariCode;
    }

    public String getSevkAddress() {
        return sevkAddress;
    }

    public void setSevkAddress(String sevkAddress) {
        this.sevkAddress = sevkAddress;
    }

    public String getSevkTel() {
        return sevkTel;
    }

    public void setSevkTel(String sevkTel) {
        this.sevkTel = sevkTel;
    }

    public String getSevkMuhatap() {
        return sevkMuhatap;
    }

    public void setSevkMuhatap(String sevkMuhatap) {
        this.sevkMuhatap = sevkMuhatap;
    }

    public String getSevkAcikAdress() {
        return sevkAcikAdress;
    }

    public void setSevkAcikAdress(String sevkAcikAdress) {
        this.sevkAcikAdress = sevkAcikAdress;
    }
}
