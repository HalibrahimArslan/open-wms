package com.hisarresearch.wms.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "getZReport",
            query = "select * from aur_vw_z_raporu ",
            resultClass = AurVwZReport.class
        ),
    }
)
public class AurVwZReport implements Serializable {
    @Id
    private Long id;

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "firm_name")
    private String firmName;

    @Column(name = "belge_no")
    private String belgeNo;

    @Column(name = "op_type")
    private String opType;

    @Column(name = "erp_order_info")
    private String erpOrderInfo;

    @Column(name = "miktar")
    private int miktar;
    @Column(name = "baglanti_tipi")
    private String baglantiTipi;

    @Column(name = "alt_cari_adi")
    private String altCariAdi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getBelgeNo() {
        return belgeNo;
    }

    public void setBelgeNo(String belgeNo) {
        this.belgeNo = belgeNo;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getErpOrderInfo() {
        return erpOrderInfo;
    }

    public void setErpOrderInfo(String erpOrderInfo) {
        this.erpOrderInfo = erpOrderInfo;
    }

    public int getMiktar() {
        return miktar;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    public String getBaglantiTipi() {
        return baglantiTipi;
    }

    public void setBaglantiTipi(String baglantiTipi) {
        this.baglantiTipi = baglantiTipi;
    }

    public String getAltCariAdi() {
        return altCariAdi;
    }

    public void setAltCariAdi(String altCariAdi) {
        this.altCariAdi = altCariAdi;
    }
}
