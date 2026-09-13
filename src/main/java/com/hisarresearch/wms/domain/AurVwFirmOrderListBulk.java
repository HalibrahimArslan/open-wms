package com.hisarresearch.wms.domain;

import javax.persistence.*;

@Entity
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "getFirmOrderDetailBulk",
            query = "select id,stokkodu,stokadi,siparismiktar,teslimmiktar,barkod,stokbirimi,sipuid,orderno,orderdate from aur_vw_firm_orderlist_bulk " +
                "    where siptip = :sipTip and " +
                "          depono IN :depoList and " +
                "          musterikod = :musteriKod and " +
                "          caribaglantitipi = :cariBaglantiTipi",
            resultClass = AurVwFirmOrderListBulk.class
        ),
    }
)
public class AurVwFirmOrderListBulk {
    @Id
    private Long id;

    @Column(name = "stokkodu")
    private String stokKodu;

    @Column(name = "stokadi")
    private String stokAdi;

    @Column(name = "siparismiktar")
    private Double siparisMiktar;

    @Column(name = "teslimmiktar")
    private Double teslimMiktar;

    @Column(name = "barkod")
    private String barkod;

    @Column(name = "stokbirimi")
    private String stokBirimi;

    @Column(name ="sipuid")
    private String sipUid;

    @Column(name ="orderno")
    private String orderNo;

    @Column(name ="orderdate")
    private String orderDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
