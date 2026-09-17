package com.hisarresearch.wms.service.dto.interwarehouse;

import java.util.Objects;

public class InterwarehouseOrderRequestDTO {
    private String ssipBelgeNo = "";
    private String ssipStokKod = "";
    private Double ssipMiktar ;
    private String ssipAciklama = "";

    private String ssipEvraknoSeri = "";

    private int ssipEvraknoSira = 0;
    private int ssipGirdepo;
    private int ssipCikdepo;


    public String getSsipBelgeNo() {
        return ssipBelgeNo;
    }

    public void setSsipBelgeNo(String ssipBelgeNo) {
        this.ssipBelgeNo = ssipBelgeNo;
    }

    public String getSsipStokKod() {
        return ssipStokKod;
    }

    public void setSsipStokKod(String ssipStokKod) {
        this.ssipStokKod = ssipStokKod;
    }

    public Double getSsipMiktar() {
        return ssipMiktar;
    }

    public void setSsipMiktar(Double ssipMiktar) {
        this.ssipMiktar = ssipMiktar;
    }

    public String getSsipAciklama() {
        return ssipAciklama;
    }

    public void setSsipAciklama(String ssipAciklama) {
        this.ssipAciklama = ssipAciklama;
    }

    public int getSsipGirdepo() {
        return ssipGirdepo;
    }

    public void setSsipGirdepo(int ssipGirdepo) {
        this.ssipGirdepo = ssipGirdepo;
    }

    public int getSsipCikdepo() {
        return ssipCikdepo;
    }

    public void setSsipCikdepo(int ssipCikdepo) {
        this.ssipCikdepo = ssipCikdepo;
    }

    public String getSsipEvraknoSeri() {
        return ssipEvraknoSeri;
    }

    public void setSsipEvraknoSeri(String ssipEvraknoSeri) {
        this.ssipEvraknoSeri = ssipEvraknoSeri;
    }

    public int getSsipEvraknoSira() {
        return ssipEvraknoSira;
    }

    public void setSsipEvraknoSira(int ssipEvraknoSira) {
        this.ssipEvraknoSira = ssipEvraknoSira;
    }

    @Override
    public String toString() {
        return "InterwarehouseOrderRequestDTO{" +
            "ssipBelgeNo='" + ssipBelgeNo + '\'' +
            ", ssipStokKod='" + ssipStokKod + '\'' +
            ", ssipMiktar=" + ssipMiktar +
            ", ssipAciklama='" + ssipAciklama + '\'' +
            ", ssipEvraknoSeri='" + ssipEvraknoSeri + '\'' +
            ", ssipEvraknoSira=" + ssipEvraknoSira +
            ", ssipGirdepo=" + ssipGirdepo +
            ", ssipCikdepo=" + ssipCikdepo +
            '}';
    }
}
