package com.hisarresearch.wms.service.dto;

public class DepolarArasiTransferDetailDto {
    private String stokKodu;
    private Double transferMiktar;
    private String referansSiparisNo;
    private String refUid;

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public Double getTransferMiktar() {
        return transferMiktar;
    }

    public void setTransferMiktar(Double transferMiktar) {
        this.transferMiktar = transferMiktar;
    }

    public String getReferansSiparisNo() {
        return referansSiparisNo;
    }

    public void setReferansSiparisNo(String referansSiparisNo) {
        this.referansSiparisNo = referansSiparisNo;
    }

    public String getRefUid() {
        return refUid;
    }

    public void setRefUid(String refUid) {
        this.refUid = refUid;
    }
}
