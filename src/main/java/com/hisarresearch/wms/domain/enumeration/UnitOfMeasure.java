package com.hisarresearch.wms.domain.enumeration;

public enum UnitOfMeasure {

    ADET,
    METRE,
    METREKARE,
    METRETUL,
    KILOGRAM;

    public static UnitOfMeasure fromValue(String value) {
        if (value == null) return null;
        String normalized = value.trim()
            .replace("İ", "I").replace("ı", "i")
            .replace("Ü", "U").replace("ü", "u")
            .replace("Ş", "S").replace("ş", "s")
            .replace("Ğ", "G").replace("ğ", "g")
            .replace("Ö", "O").replace("ö", "o")
            .replace("Ç", "C").replace("ç", "c")
            .toUpperCase();
        switch (normalized) {
            case "ADET":            return ADET;
            case "KG":
            case "KILOGRAM":        return KILOGRAM;
            case "MT":
            case "METRE":           return METRE;
            case "MTUL":
            case "METRETUL":        return METRETUL;
            case "M2":
            case "METREKARE":       return METREKARE;
            default:                return null;
        }
    }

    public boolean requiresQuantityOne() {
        return this == METRE || this == METREKARE || this == METRETUL || this == KILOGRAM;
    }

    public boolean requiresAdetOne() {
        return this == ADET;
    }
}
