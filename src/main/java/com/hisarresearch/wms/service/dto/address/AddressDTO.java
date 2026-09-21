package com.hisarresearch.wms.service.dto.address;

import javax.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class AddressDTO {
    private Long id;
    private Boolean status;
    @NotNull
    private String address;
    private int warehouseCode;
    private String companyCode;
    private String addressType;
    private String bolum;
    private String reyon;

    @Nullable
    private String unite;

    @Nullable
    private String kat;

    @Nullable
    private  String oda;

    private Boolean geciciAdres;
    private Boolean toplamaGozu;
    private Boolean kontrolAdres;
    private Boolean countable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(int warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getReyon() {
        return reyon;
    }

    public void setReyon(String reyon) {
        this.reyon = reyon;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getKat() {
        return kat;
    }

    public void setKat(String kat) {
        this.kat = kat;
    }

    public Boolean getGeciciAdres() {
        return geciciAdres;
    }

    public void setGeciciAdres(Boolean geciciAdres) {
        this.geciciAdres = geciciAdres;
    }

    public Boolean getToplamaGozu() {
        return toplamaGozu;
    }

    public void setToplamaGozu(Boolean toplamaGozu) {
        this.toplamaGozu = toplamaGozu;
    }

    public Boolean getKontrolAdres() {
        return kontrolAdres;
    }

    public void setKontrolAdres(Boolean kontrolAdres) {
        this.kontrolAdres = kontrolAdres;
    }

    public Boolean getCountable() {
        return countable;
    }

    public void setCountable(Boolean countable) {
        this.countable = countable;
    }

    @Nullable
    public String getOda() {
        return oda;
    }

    public void setOda(@Nullable String oda) {
        this.oda = oda;
    }
}
