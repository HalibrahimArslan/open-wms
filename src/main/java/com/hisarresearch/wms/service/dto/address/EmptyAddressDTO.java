package com.hisarresearch.wms.service.dto.address;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;

import java.util.List;

public class EmptyAddressDTO {
    private int totalAddressCount;
    private int emptyAddressCount;
    private int filledAddressCount;
    private int filledRatio;
    private List<AurDepoUrunAdres> emptyAddresses;

    public int getTotalAddressCount() {
        return totalAddressCount;
    }

    public void setTotalAddressCount(int totalAddressCount) {
        this.totalAddressCount = totalAddressCount;
    }

    public int getEmptyAddressCount() {
        return emptyAddressCount;
    }

    public void setEmptyAddressCount(int emptyAddressCount) {
        this.emptyAddressCount = emptyAddressCount;
    }

    public int getFilledAddressCount() {
        return filledAddressCount;
    }

    public void setFilledAddressCount(int filledAddressCount) {
        this.filledAddressCount = filledAddressCount;
    }

    public int getFilledRatio() {
        return filledRatio;
    }

    public void setFilledRatio(int filledRatio) {
        this.filledRatio = filledRatio;
    }

    public List<AurDepoUrunAdres> getEmptyAddresses() {
        return emptyAddresses;
    }

    public void setEmptyAddresses(List<AurDepoUrunAdres> emptyAddresses) {
        this.emptyAddresses = emptyAddresses;
    }
}
