package com.hisarresearch.wms.domain.enumeration;

public enum BatchRequestStatus {
    SUCCESS("READY"),
    PENDING("OUT_PROGRESS"),
    FAILURE("IN_PROGRESS"),
    RECEIVED("OUT_PROGRESS"),
    REVOKED("OUT_PROGRESS"),
    RETRY("IN_PROGRESS"),
    STARTED("OUT_PROGRESS");

    private String value;

    public String getValue() {
        return value;
    }

    BatchRequestStatus(String value){this.value = value;}
}
