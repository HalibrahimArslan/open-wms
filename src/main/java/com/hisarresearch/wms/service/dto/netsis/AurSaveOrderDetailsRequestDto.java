package com.hisarresearch.wms.service.dto.netsis;

import com.hisarresearch.wms.service.dto.AurCariOrderDetailDto;
import java.util.List;

public class AurSaveOrderDetailsRequestDto {

    private Integer depoCode;
    private String firmCode;
    private String fatirsNumber;
    private List<AurCariOrderDetailDto> orderDetailList;

    public Integer getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(Integer depoCode) {
        this.depoCode = depoCode;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getFatirsNumber() {
        return fatirsNumber;
    }

    public void setFatirsNumber(String fatirsNumber) {
        this.fatirsNumber = fatirsNumber;
    }

    public List<AurCariOrderDetailDto> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<AurCariOrderDetailDto> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
