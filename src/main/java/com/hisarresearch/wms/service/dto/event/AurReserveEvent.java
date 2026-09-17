package com.hisarresearch.wms.service.dto.event;

import com.hisarresearch.wms.service.dto.AurReserveDTO;

import java.util.List;

public class AurReserveEvent {
    private List<AurReserveDTO> aurReserveDTOList;

    public AurReserveEvent() {
    }

    public AurReserveEvent(List<AurReserveDTO> aurReserveDTOList) {
        this.aurReserveDTOList = aurReserveDTOList;
    }



    public List<AurReserveDTO> getAurReserveDTOList() {
        return aurReserveDTOList;
    }

    public void setAurReserveDTOList(List<AurReserveDTO> aurReserveDTOList) {
        this.aurReserveDTOList = aurReserveDTOList;
    }
}
