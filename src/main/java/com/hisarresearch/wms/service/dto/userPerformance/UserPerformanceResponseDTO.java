package com.hisarresearch.wms.service.dto.userPerformance;

import java.util.ArrayList;
import java.util.List;

public class UserPerformanceResponseDTO {

    private UserPerformanceDTO toplam;
    private List<UserPerformanceDetailDTO> detaylar;

    public UserPerformanceResponseDTO(UserPerformanceDTO dto) {
        this.toplam = dto;
        this.detaylar = new ArrayList<>();
    }


    public List<UserPerformanceDetailDTO> getDetaylar() {
        return detaylar;
    }

    public void setDetaylar(List<UserPerformanceDetailDTO> detaylar) {
        this.detaylar = detaylar;
    }

    public UserPerformanceDTO getToplam() {
        return toplam;
    }

    public void setToplam(UserPerformanceDTO toplam) {
        this.toplam = toplam;
    }
}
