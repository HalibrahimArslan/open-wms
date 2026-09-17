package com.hisarresearch.wms.service.dto.barcode;

import com.hisarresearch.wms.service.barcode.statemachine.UniqueBarcodeState;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class UniqueBarcodeResponseDTO {

    private Long id;
    private String barcode;
    private String partiCode;
    private Long lotNumber;
    private UniqueBarcodeState status;
    private BigDecimal quantity;
    private Instant receivingDate;
    private String customerCode;
    private String erpOrderInfo;
    private Map<String, Object> description;
    private Instant lastModifiedDate;

    private String stokKodu;
    private String stokAdi;
    private String anaGrup;
    private String kategoriAdi;
    private String stokBirimi;
    private String barkod;
    private Boolean lotBasedTracking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPartiCode() {
        return partiCode;
    }

    public void setPartiCode(String partiCode) {
        this.partiCode = partiCode;
    }

    public Long getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(Long lotNumber) {
        this.lotNumber = lotNumber;
    }

    public UniqueBarcodeState getStatus() {
        return status;
    }

    public void setStatus(UniqueBarcodeState status) {
        this.status = status;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Instant getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(Instant receivingDate) {
        this.receivingDate = receivingDate;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getErpOrderInfo() {
        return erpOrderInfo;
    }

    public void setErpOrderInfo(String erpOrderInfo) {
        this.erpOrderInfo = erpOrderInfo;
    }

    public Map<String, Object> getDescription() {
        return description;
    }

    public void setDescription(Map<String, Object> description) {
        this.description = description;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getAnaGrup() {
        return anaGrup;
    }

    public void setAnaGrup(String anaGrup) {
        this.anaGrup = anaGrup;
    }

    public String getKategoriAdi() {
        return kategoriAdi;
    }

    public void setKategoriAdi(String kategoriAdi) {
        this.kategoriAdi = kategoriAdi;
    }

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public Boolean getLotBasedTracking() {
        return lotBasedTracking;
    }

    public void setLotBasedTracking(Boolean lotBasedTracking) {
        this.lotBasedTracking = lotBasedTracking;
    }

}
