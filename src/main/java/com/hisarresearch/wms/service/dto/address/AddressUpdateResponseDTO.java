package com.hisarresearch.wms.service.dto.address;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressUpdateResponseDTO {
    private List<Map<String, AurDepoUrunAdres>> errorList = new ArrayList<>();
    private List<AurDepoUrunAdres> successList = new ArrayList<>();

    public List<Map<String, AurDepoUrunAdres>> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Map<String, AurDepoUrunAdres>> errorList) {
        this.errorList = errorList;
    }

    public List<AurDepoUrunAdres> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<AurDepoUrunAdres> successList) {
        this.successList = successList;
    }

    public void addSuccess(AurDepoUrunAdres successItem) {
        if (successItem != null) {
            this.successList.add(successItem);
        }
    }

    public void addError(String errorMessage, AurDepoUrunAdres errorItem) {
        if (errorMessage != null && errorItem != null) {
            this.errorList.add(Map.of(errorMessage, errorItem));
        }
    }
}
