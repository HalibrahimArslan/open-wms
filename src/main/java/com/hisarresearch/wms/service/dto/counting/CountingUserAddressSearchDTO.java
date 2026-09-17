package com.hisarresearch.wms.service.dto.counting;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CountingUserAddressSearchDTO {
    @NotNull
    private Long countingDefinitionId;

    @NotEmpty
    private List<AurDepoUrunAdres> addresses;

    public Long getCountingDefinitionId() {
        return countingDefinitionId;
    }

    public void setCountingDefinitionId(Long countingDefinitionId) {
        this.countingDefinitionId = countingDefinitionId;
    }

    public List<AurDepoUrunAdres> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AurDepoUrunAdres> addresses) {
        this.addresses = addresses;
    }
}
