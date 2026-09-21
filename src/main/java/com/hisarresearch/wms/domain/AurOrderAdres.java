package com.hisarresearch.wms.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aur_order_adres")
public class AurOrderAdres implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurOrderAdresGenerator")
    @SequenceGenerator(
        name = "aurOrderAdresGenerator",
        sequenceName = "aur_order_adres_seq",
        allocationSize = 1
    )
    private Long id;

    @Column(name = "erp_order_info")
    private String erpOrderInfo;

    @Column(name = "magento_order_id")
    private String magentoOrderId;

    @Column(name = "sevk_adres_1")
    private String sevkAdres1;

    @Column(name = "sevk_adres_2")
    private String sevkAdres2;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "sevk_il")
    private String sevkIl;

    @Column(name = "sevk_ilce")
    private String sevkIlce;

    @Column(name = "sevk_ulke")
    private String sevkUlke;

    @Column(name = "sevk_tel")
    private String sevkTel;

    @Column(name = "sevk_muhatap")
    private String sevkMuhatap;


    public AurOrderAdres() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErpOrderInfo() {
        return erpOrderInfo;
    }

    public void setErpOrderInfo(String erpOrderInfo) {
        this.erpOrderInfo = erpOrderInfo;
    }

    public String getMagentoOrderId() {
        return magentoOrderId;
    }

    public void setMagentoOrderId(String magentoOrderId) {
        this.magentoOrderId = magentoOrderId;
    }

    public String getSevkAdres1() {
        return sevkAdres1;
    }

    public void setSevkAdres1(String sevkAdres1) {
        this.sevkAdres1 = sevkAdres1;
    }

    public String getSevkAdres2() {
        return sevkAdres2;
    }

    public void setSevkAdres2(String sevkAdres2) {
        this.sevkAdres2 = sevkAdres2;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getSevkIl() {
        return sevkIl;
    }

    public void setSevkIl(String sevkIl) {
        this.sevkIl = sevkIl;
    }

    public String getSevkIlce() {
        return sevkIlce;
    }

    public void setSevkIlce(String sevkIlce) {
        this.sevkIlce = sevkIlce;
    }

    public String getSevkUlke() {
        return sevkUlke;
    }

    public void setSevkUlke(String sevkUlke) {
        this.sevkUlke = sevkUlke;
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

    @Override
    public String toString() {
        return "AurOrderAdres{" +
            "id=" + id +
            ", erpOrderInfo=" + erpOrderInfo +
            ", magentoOrderId=" + magentoOrderId +
            ", sevkAdres1='" + sevkAdres1 + '\'' +
            ", sevkAdres2='" + sevkAdres2 + '\'' +
            ", postCode='" + postCode + '\'' +
            ", sevkIl='" + sevkIl + '\'' +
            ", sevkIlce='" + sevkIlce + '\'' +
            ", sevkUlke='" + sevkUlke + '\'' +
            ", sevkTel='" + sevkTel + '\'' +
            ", sevkMuhatap='" + sevkMuhatap + '\'' +
            '}';
    }
}
