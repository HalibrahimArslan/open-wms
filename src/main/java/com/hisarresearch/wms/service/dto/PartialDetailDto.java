package com.hisarresearch.wms.service.dto;

public class PartialDetailDto {
    private int id;
    private Long aurPartialItemId;
    private Boolean isPiece;
    private String orderNo;
    private String stokKodu;
    private String stokAdi;
    private String barkod;
    private Double siparisMiktar;
    private Double teslimMiktar;
    private String sipUid;
    private PieceMasterDTO pieceMaster;
    private Double pieceAmount;
    private Boolean reserve = false;
    private String reserveDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getAurPartialItemId() {
        return aurPartialItemId;
    }

    public void setAurPartialItemId(Long aurPartialItemId) {
        this.aurPartialItemId = aurPartialItemId;
    }

    public Boolean getPiece() {
        return isPiece;
    }

    public void setPiece(Boolean piece) {
        isPiece = piece;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
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

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public PieceMasterDTO getPieceMaster() {
        return pieceMaster;
    }

    public void setPieceMaster(PieceMasterDTO pieceMaster) {
        this.pieceMaster = pieceMaster;
    }

    public Double getPieceAmount() {
        return pieceAmount;
    }

    public void setPieceAmount(Double pieceAmount) {
        this.pieceAmount = pieceAmount;
    }

    public Boolean getReserve() {
        return reserve;
    }

    public void setReserve(Boolean reserve) {
        this.reserve = reserve;
    }

    public String getReserveDescription() {
        return reserveDescription;
    }

    public void setReserveDescription(String reserveDescription) {
        this.reserveDescription = reserveDescription;
    }
}
