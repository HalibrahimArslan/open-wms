package com.hisarresearch.wms.service.dto.event;

import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeAddressDTO;

public class UniqueBarcodeAddressUpdatedEvent {

    private final UniqueBarcodeAddressDTO dto;

    public UniqueBarcodeAddressUpdatedEvent(UniqueBarcodeAddressDTO dto) {
        this.dto = dto;
    }

    public UniqueBarcodeAddressDTO getDto() {
        return dto;
    }
}
