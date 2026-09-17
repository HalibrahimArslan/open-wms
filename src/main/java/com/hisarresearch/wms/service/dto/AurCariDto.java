package com.hisarresearch.wms.service.dto;

import java.util.List;

public class AurCariDto {

    private String cariKod;
    private String cariUnvan;
    private String bolgeKodu;
    private String bolgeAdi;
    private String cariHareketTipi;
    private String cariBaglantiTipi;

    /**
     * Tek bir cariye (cariKod ile) ozel sorgularda dolar; toplu liste sorgusunda
     * (cariKod = "ALL") bos/null gelir, bu haliyle ekrana yansimaz.
     */
    private Integer orderLineItemCount;

    /** {@link #orderLineItemCount} ile ayni kosulda dolar. */
    private List<AurCariOrderSummaryDto> orderList;


    public String getCariKod() {
        return cariKod;
    }

    public void setCariKod(String cariKod) {
        this.cariKod = cariKod;
    }

    public String getCariUnvan() {
        return cariUnvan;
    }

    public void setCariUnvan(String cariUnvan) {
        this.cariUnvan = cariUnvan;
    }

    public String getBolgeKodu() {
        return bolgeKodu;
    }

    public void setBolgeKodu(String bolgeKodu) {
        this.bolgeKodu = bolgeKodu;
    }

    public String getBolgeAdi() {
        return bolgeAdi;
    }

    public void setBolgeAdi(String bolgeAdi) {
        this.bolgeAdi = bolgeAdi;
    }

    public String getCariHareketTipi() {
        return cariHareketTipi;
    }

    public void setCariHareketTipi(String cariHareketTipi) {
        this.cariHareketTipi = cariHareketTipi;
    }

    public String getCariBaglantiTipi() {
        return cariBaglantiTipi;
    }

    public void setCariBaglantiTipi(String cariBaglantiTipi) {
        this.cariBaglantiTipi = cariBaglantiTipi;
    }

    public Integer getOrderLineItemCount() {
        return orderLineItemCount;
    }

    public void setOrderLineItemCount(Integer orderLineItemCount) {
        this.orderLineItemCount = orderLineItemCount;
    }

    public List<AurCariOrderSummaryDto> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<AurCariOrderSummaryDto> orderList) {
        this.orderList = orderList;
    }
}
