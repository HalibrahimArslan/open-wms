package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.AurOrderDetailSkt;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class ProductAddressSaveDTO implements Cloneable {

    private String stokKod;
    private String barcode;
    private String urunAdres;
    private String orderNo;
    private String depoCode;
    private String companyCode;
    private String barkodTipi = "RAF";
    private Boolean status = Boolean.TRUE;

    @NotNull
    @PositiveOrZero
    private Double miktar;
    private Boolean checkPartial;

    @NotNull
    @NotBlank
    private String stokAdi;
    private List<AurOrderDetailSkt> sktDateList;


    public String getStokKod() {
        return stokKod;
    }

    public void setStokKod(String stokKod) {
        this.stokKod = stokKod;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUrunAdres() {
        return urunAdres;
    }

    public void setUrunAdres(String urunAdres) {
        this.urunAdres = urunAdres;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getBarkodTipi() {
        return barkodTipi;
    }

    public void setBarkodTipi(String barkodTipi) {
        this.barkodTipi = barkodTipi;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getMiktar() {
        return miktar;
    }

    public void setMiktar(Double miktar) {
        this.miktar = miktar;
    }

    public Boolean getCheckPartial() {
        return checkPartial;
    }

    public void setCheckPartial(Boolean checkPartial) {
        this.checkPartial = checkPartial;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public List<AurOrderDetailSkt> getSktDateList() {
        return sktDateList;
    }

    public void setSktDateList(List<AurOrderDetailSkt> sktDateList) {
        this.sktDateList = sktDateList;
    }

    @Override
    public ProductAddressSaveDTO clone() throws CloneNotSupportedException {
        ProductAddressSaveDTO clone = (ProductAddressSaveDTO) super.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "ProductAddressSaveDTO{" +
            "stokKod='" + stokKod + '\'' +
            ", barcode='" + barcode + '\'' +
            ", urunAdres='" + urunAdres + '\'' +
            ", orderNo='" + orderNo + '\'' +
            ", depoCode='" + depoCode + '\'' +
            ", companyCode='" + companyCode + '\'' +
            ", barkodTipi='" + barkodTipi + '\'' +
            ", status=" + status +
            ", miktar=" + miktar +
            ", checkPartial=" + checkPartial +
            ", stokAdi='" + stokAdi + '\'' +
            ", sktDateList=" + sktDateList +
            '}';
    }
}
