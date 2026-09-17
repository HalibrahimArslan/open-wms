package com.hisarresearch.wms.service.barcode.statemachine;

public enum UniqueBarcodeState {
    CREATED,
    RECEIVING_SCANNED,
    IN_TEMPORARY_AREA
}