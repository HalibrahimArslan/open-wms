package com.hisarresearch.wms.service.dto.address;

import com.hisarresearch.wms.domain.address.AddressType;
import com.hisarresearch.wms.domain.enumeration.AddressFieldType;
import com.hisarresearch.wms.service.dto.address.components.AddressTypeDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class AddressCreateBulkDTO {
    @NotEmpty
    private List<Long> departmentIds;
    @NotEmpty
    private List<Long> hallIds;
    private List<Long> unitIds;
    private List<Long> flatIds;
    private List<Long> roomIds;
    @NotNull
    private AddressType addressType;
    @NotNull
    private int warehouseCode;
    @NotNull
    private String companyCode;
    private Boolean geciciAdres;
    private Boolean toplamaGozu;
    private Boolean kontrolAdres;
    private Boolean countable;

    @NotNull
    @Size(min = 1)
    private List<AddressFieldType> selectedFields;

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<Long> getHallIds() {
        return hallIds;
    }

    public void setHallIds(List<Long> hallIds) {
        this.hallIds = hallIds;
    }

    public List<Long> getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(List<Long> unitIds) {
        this.unitIds = unitIds;
    }

    public List<Long> getFlatIds() {
        return flatIds;
    }

    public void setFlatIds(List<Long> flatIds) {
        this.flatIds = flatIds;
    }

    public List<Long> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(List<Long> roomIds) {
        this.roomIds = roomIds;
    }

    public @NotNull AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(@NotNull AddressType addressType) {
        this.addressType = addressType;
    }

    @NotNull
    public int getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(@NotNull int warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public @NotNull String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(@NotNull String companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getGeciciAdres() {
        return geciciAdres;
    }

    public void setGeciciAdres(Boolean geciciAdres) {
        this.geciciAdres = geciciAdres;
    }

    public Boolean getToplamaGozu() {
        return toplamaGozu;
    }

    public void setToplamaGozu(Boolean toplamaGozu) {
        this.toplamaGozu = toplamaGozu;
    }

    public Boolean getKontrolAdres() {
        return kontrolAdres;
    }

    public void setKontrolAdres(Boolean kontrolAdres) {
        this.kontrolAdres = kontrolAdres;
    }

    public Boolean getCountable() {
        return countable;
    }

    public void setCountable(Boolean countable) {
        this.countable = countable;
    }

    public @Size(min = 1) List<AddressFieldType> getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(@Size(min = 1) List<AddressFieldType> selectedFields) {
        this.selectedFields = selectedFields;
    }
}
