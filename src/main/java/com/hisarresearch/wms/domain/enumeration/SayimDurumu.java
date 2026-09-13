package com.hisarresearch.wms.domain.enumeration;

/**
 * The SayimDurumu enumeration.
 */
public enum SayimDurumu {
    ACTIVE("Aktif"),
    PASSIVE("Pasif"),
    REJECTED("İptal"),
    CONFIRMED("Onaylandı"),
    PARKING("Park"),
    COMPLETED("Tamamlandı");

    private final String label;

    SayimDurumu(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
