package com.hisarresearch.wms.service.dto.uyumsoft;

public class QueueStatusSearchDto {
    private String firma_kod;
    private String batch_request_id;
    private String msip_no;
    private String sevk_tarih = "";
    private String sevk_saat = "";

    public String getFirma_kod() {
        return firma_kod;
    }

    public void setFirma_kod(String firma_kod) {
        this.firma_kod = firma_kod;
    }

    public String getBatch_request_id() {
        return batch_request_id;
    }

    public void setBatch_request_id(String batch_request_id) {
        this.batch_request_id = batch_request_id;
    }

    public String getMsip_no() {
        return msip_no;
    }

    public void setMsip_no(String msip_no) {
        this.msip_no = msip_no;
    }

    public String getSevk_tarih() {
        return sevk_tarih;
    }

    public void setSevk_tarih(String sevk_tarih) {
        this.sevk_tarih = sevk_tarih;
    }

    public String getSevk_saat() {
        return sevk_saat;
    }

    public void setSevk_saat(String sevk_saat) {
        this.sevk_saat = sevk_saat;
    }
}
