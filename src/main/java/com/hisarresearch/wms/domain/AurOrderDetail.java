package com.hisarresearch.wms.domain;


import com.hisarresearch.wms.domain.barcode.UniqueBarcode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@Table(name = "aur_order_detail")
public class AurOrderDetail implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurOrderDetailGenerator")
    @SequenceGenerator(name = "aurOrderDetailGenerator",sequenceName = "aur_order_detail_seq",allocationSize = 1)
    private Long id;

    @Column(name = "status")
    private String status;

    @NotAudited
    @Column(name = "stok_kodu")
    private String stokKodu;

    @NotAudited
    @Column(name = "barcode")
    private String barkod;

    @NotAudited
    @Column(name = "stok_birimi")
    private String stokBirimi;

    @NotAudited
    @Column(name="siparis_miktar")
    private Double siparisMiktar;

    @Column(name="teslim_miktar")
    private Double teslimMiktar;

    @NotAudited
    @Column(name="stok_adi")
    private String stokAdi;

    @NotAudited
    @Column(name="sip_uid")
    private String sipUid;

    @NotAudited
    @Column(name="siparis_no")
    private String siparisNo;

    @Column(name="observer_amount")
    private Double observerAmount;

    @NotAudited
    @Column(name="is_piece")
    private Boolean isPiece;

    @NotAudited
    @Column(name="aur_partial_item_id")
    private Long aurPartialItemId;

    @NotAudited
    @Version
    private int version;

    @NotAudited
    @OneToMany(mappedBy = "aurTmpDetail",fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value =  {"aurTmpDetail"},allowSetters = true)
    private Set<AurOrderDetailSkt> aurTmpDetailSktList = new HashSet<>();

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "aur_order_id")
    @JsonIgnoreProperties(value = { "orderTmpDetailList" }, allowSetters = true)
    private AurOrderMaster order;

    @NotAudited
    @OneToMany(mappedBy = "aurOrderDetail", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"aurOrderDetail"}, allowSetters = true)
    private Set<UniqueBarcode> uniqueBarcodes = new HashSet<>();

    public AurOrderDetail() {
    }

    public AurOrderDetail(AurOrderDetail other) {
        this.id = null;
        this.order = other.order;
        this.status = other.status;
        this.stokKodu = other.stokKodu;
        this.barkod = other.barkod;
        this.stokBirimi = other.stokBirimi;
        this.siparisMiktar = other.siparisMiktar;
        this.teslimMiktar = other.teslimMiktar;
        this.stokAdi = other.stokAdi;
        this.sipUid = other.sipUid;
        this.siparisNo = other.siparisNo;
        this.observerAmount = other.observerAmount;
        this.isPiece = other.isPiece;
        this.aurPartialItemId = other.aurPartialItemId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if(barkod == null){
            return "";
        }
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Set<AurOrderDetailSkt> getAurTmpDetailSktList() {
        return aurTmpDetailSktList;
    }

    public void setAurTmpDetailSktList(Set<AurOrderDetailSkt> aurTmpDetailSktList) {
        this.aurTmpDetailSktList = aurTmpDetailSktList;
    }

    public AurOrderMaster getOrder() {
        return order;
    }

    public void setOrder(AurOrderMaster order) {
        this.order = order;
    }

    public Set<UniqueBarcode> getUniqueBarcodes() {
        return uniqueBarcodes;
    }

    public void setUniqueBarcodes(Set<UniqueBarcode> uniqueBarcodes) {
        this.uniqueBarcodes = uniqueBarcodes;
    }

    @Override
    public AurOrderDetail clone() throws CloneNotSupportedException {
        AurOrderDetail clone = (AurOrderDetail) super.clone();
        clone.setId(null);
        clone.aurTmpDetailSktList = new HashSet<>();
        return clone;
    }
}
