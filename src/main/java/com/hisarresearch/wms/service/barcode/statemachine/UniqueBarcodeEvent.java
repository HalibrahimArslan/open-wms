package com.hisarresearch.wms.service.barcode.statemachine;

public enum UniqueBarcodeEvent {
    RECEIVE_SCAN,
    CANCEL_RECEIVE_SCAN,
    TRANSFER_TO_TEMPORARY_AREA
}