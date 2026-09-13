package com.hisarresearch.wms.service.dto;

public class CustomerAddressDTO {
    private Long id;
    private Long addressId;
    private String cariCode;
    private String sevkAddress;
    private String sevkTel;
    private String sevkMuhatap;
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
