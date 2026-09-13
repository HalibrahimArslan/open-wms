package com.hisarresearch.wms.domain.enumeration;

public enum AddressFieldType {
    DEPARTMENT("department"),
    HALL("hall"),
    UNIT("unit"),
    FLAT("flat"),
    ROOM("room");

    private final String value;

    AddressFieldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

