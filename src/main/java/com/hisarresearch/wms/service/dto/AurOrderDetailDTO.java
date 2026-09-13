package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeAssignDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class AurOrderDetailDTO {
    private Long id;
    private String status;
    private String stokKodu;
    private String barkod;
    private String stokBirimi;
    @NotNull
    @Positive
    private Double siparisMiktar;

    @NotNull
    private Double teslimMiktar;
    private String stokAdi;
    private String sipUid;
    private String siparisNo;

    @NotNull
    private Double observerAmount;
    private Boolean isPiece;
    private Long aurPartialItemId;
    private List<AurOrderDetailSktDTO> aurTmpDetailSktList;
    private Long orderId;
    @Valid
    private UniqueBarcodeAssignDTO uniqueBarcodeAssign;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }

    public Double getSiparisMiktar() {
        return siparisMiktar;
    }

    public void setSiparisMiktar(Double siparisMiktar) {
        this.siparisMiktar = siparisMiktar;
    }

    public Double getTeslimMiktar() {
       return teslimMiktar;
    }

    public void setTeslimMiktar(Double teslimMiktar) {
        this.teslimMiktar = teslimMiktar;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public String getSiparisNo() {
        return siparisNo;
    }

    public void setSiparisNo(String siparisNo) {
        this.siparisNo = siparisNo;
    }

    public Double getObserverAmount() {
        return observerAmount;
    }

    public void setObserverAmount(Double observerAmount) {
        this.observerAmount = observerAmount;
    }

    public Boolean getPiece() {
        return isPiece;
    }

    public void setPiece(Boolean piece) {
        isPiece = piece;
    }

    public Long getAurPartialItemId() {
        return aurPartialItemId;
    }

    public void setAurPartialItemId(Long aurPartialItemId) {
        this.aurPartialItemId = aurPartialItemId;
    }

    public List<AurOrderDetailSktDTO> getAurTmpDetailSktList() {
        return aurTmpDetailSktList;
    }

    public void setAurTmpDetailSktList(List<AurOrderDetailSktDTO> aurTmpDetailSktList) {
        this.aurTmpDetailSktList = aurTmpDetailSktList;
    }

    public UniqueBarcodeAssignDTO getUniqueBarcodeAssign() {
        return uniqueBarcodeAssign;
    }

    public void setUniqueBarcodeAssign(UniqueBarcodeAssignDTO uniqueBarcodeAssign) {
        this.uniqueBarcodeAssign = uniqueBarcodeAssign;
    }
}
