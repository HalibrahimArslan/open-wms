package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.config.Constants;
import com.hisarresearch.wms.domain.AurLookupTable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

public class MalKabulRequestDto {
    String belgeNo;
    Integer depoNo;
    String tarih;
    String firmCode;
    String orderNo;
    String erpUserCode;

    @NotNull
    Long orderId;

    String aracPlakaNo;
    String dorsePlakaNo;

    @NotBlank
    @Size(min = 1, max = 50)
    private String soforAdi;

    @NotBlank
    String soforSoyadi;

    @NotBlank(message = "Kimlik numarası boş olamaz")
    @Size(min = 11, max = 11, message = "Kimlik numarası 11 haneli olmalıdır")
    String soforTckn;

    @NotBlank
    @Pattern(regexp = Constants.PHONE_REGEX)
    private String soforTel;

    private AurLookupTable transportationType;
    private AurLookupTable companyLogistics;
    private AurLookupTable carryType;

    List<OrderLineItemDto> orderDetailList;

    public String getBelgeNo() {
        return belgeNo;
    }

    public void setBelgeNo(String belgeNo) {
        this.belgeNo = belgeNo;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getErpUserCode() {
        return erpUserCode;
    }

    public void setErpUserCode(String erpUserCode) {
        this.erpUserCode = erpUserCode;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getAracPlakaNo() {
        return aracPlakaNo;
    }

    public void setAracPlakaNo(String aracPlakaNo) {
        this.aracPlakaNo = aracPlakaNo;
    }

    public String getDorsePlakaNo() {
        return dorsePlakaNo;
    }

    public void setDorsePlakaNo(String dorsePlakaNo) {
        this.dorsePlakaNo = dorsePlakaNo;
    }

    public String getSoforAdi() {
        return soforAdi;
    }

    public void setSoforAdi(String soforAdi) {
        this.soforAdi = soforAdi;
    }

    public String getSoforSoyadi() {
        return soforSoyadi;
    }

    public void setSoforSoyadi(String soforSoyadi) {
        this.soforSoyadi = soforSoyadi;
    }

    public String getSoforTckn() {
        return soforTckn;
    }

    public void setSoforTckn(String soforTckn) {
        this.soforTckn = soforTckn;
    }


    public String getSoforTel() {
        return soforTel;
    }

    public void setSoforTel(String soforTel) {
        this.soforTel = soforTel;
    }

    public AurLookupTable getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(AurLookupTable transportationType) {
        this.transportationType = transportationType;
    }

    public AurLookupTable getCompanyLogistics() {
        return companyLogistics;
    }

    public void setCompanyLogistics(AurLookupTable companyLogistics) {
        this.companyLogistics = companyLogistics;
    }

    public AurLookupTable getCarryType() {
        return carryType;
    }

    public void setCarryType(AurLookupTable carryType) {
        this.carryType = carryType;
    }

    public List<OrderLineItemDto> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderLineItemDto> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
