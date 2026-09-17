package com.hisarresearch.wms.domain.enumeration;

public enum ErpConnectionType {
    LOCAL(0),
    MIKRO_V16(1),
    UYUMSOFT(2),
    MIKRO_V15(3);

    private final int erpCode;

    ErpConnectionType(int erpCode) {
        this.erpCode = erpCode;
    }

    public int getErpCode() {
        return erpCode;
    }

    public String getErpCodeAsString(){
        return String.valueOf(erpCode);
    }

    public static ErpConnectionType fromId(int erpCode) {
        for (ErpConnectionType type : values()) {
            if (type.getErpCode() == erpCode) {
                return type;
            }
        }
        return null;
    }



}
