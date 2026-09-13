package com.hisarresearch.wms.service.dto;

import java.util.List;

public class AurPartialResponseDto {

    private Long id;
    private String packageCode;

    private String packageBarcode;

    private String packageName;

    private List<AurPartialDetailsDTO> packageDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageBarcode() {
        return packageBarcode;
    }

    public void setPackageBarcode(String packageBarcode) {
        this.packageBarcode = packageBarcode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<AurPartialDetailsDTO> getPackageDetail() {
        return packageDetail;
    }

    public void setPackageDetail(List<AurPartialDetailsDTO> packageDetail) {
        this.packageDetail = packageDetail;
    }
}
