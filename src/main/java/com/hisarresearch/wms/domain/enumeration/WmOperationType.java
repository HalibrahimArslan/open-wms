package com.hisarresearch.wms.domain.enumeration;

public enum WmOperationType {
    FIRMADAN_MAL_KABUL("FMK"),
    MUSTERI_SEVKIYAT("MSK"),
    DEPOLAR_ARASI_TRANSFER("DAT"),
    DEPOLAR_ARASI_SEVKIYAT("DAS"),
    DEPOLAR_ARASI_KABUL("DAK");
    private String operationType;

    public String getOperationType() {
        return operationType;
    }

    WmOperationType(String operationType) {
        this.operationType = operationType;
    }
}
