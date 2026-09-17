package com.hisarresearch.wms.service.dto.uyumsoft;

import com.hisarresearch.wms.domain.ProductAddressv2;

import java.io.Serializable;
import java.util.List;

public class OrderResponseDTO implements Serializable {
    private List<OrderDetailDTO> apiList;

    public List<OrderDetailDTO> getApiList() {
        return apiList;
    }

    public void setApiList(List<OrderDetailDTO> apiList) {
        this.apiList = apiList;
    }
}
