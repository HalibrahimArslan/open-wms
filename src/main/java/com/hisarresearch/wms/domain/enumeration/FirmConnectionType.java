package com.hisarresearch.wms.domain.enumeration;

public enum FirmConnectionType {
    CUSTOMER("0"),
    SELLER("1"),
    VENDOR("4");

    private String value;

    public String getValue() {
        return value;
    }

    public int getValuesAsInteger(){
        return Integer.parseInt(value);
    }

    FirmConnectionType(String value) {
        this.value = value;
    }

    public static FirmConnectionType fromValue(int value) {
        String strValue = String.valueOf(value);
        for (FirmConnectionType type : values()) {
            if (type.value.equals(strValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown connection type: " + value);
    }


}
