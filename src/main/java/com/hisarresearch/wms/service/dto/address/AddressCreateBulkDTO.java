package com.hisarresearch.wms.service.dto.address;

import com.hisarresearch.wms.domain.address.AddressType;
import com.hisarresearch.wms.domain.enumeration.AddressFieldType;
import com.hisarresearch.wms.service.dto.address.components.AddressTypeDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class AddressCreateBulkDTO {
    @NotNull
    private Long firstDepartmentId;
    @NotNull
    private Long lastDepartmentId;
    @NotNull
    private Long firstHallId;
    @NotNull
    private Long lastHallId;
    private Long firstUnitId;
    private Long lastUnitId;
    private Long firstFlatId;
    private Long lastFlatId;
    private Long firstRoomId;
    private Long lastRoomId;
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

    @Size(min = 1)
    private List<AddressFieldType> selectedFields;

    public Long getFirstDepartmentId() {
        return firstDepartmentId;
    }

    public void setFirstDepartmentId(Long firstDepartmentId) {
        this.firstDepartmentId = firstDepartmentId;
    }

    public Long getLastDepartmentId() {
        return lastDepartmentId;
    }

    public void setLastDepartmentId(Long lastDepartmentId) {
        this.lastDepartmentId = lastDepartmentId;
    }

    public Long getFirstHallId() {
        return firstHallId;
    }

    public void setFirstHallId(Long firstHallId) {
        this.firstHallId = firstHallId;
    }

    public Long getLastHallId() {
        return lastHallId;
    }

    public void setLastHallId(Long lastHallId) {
        this.lastHallId = lastHallId;
    }

    public Long getFirstUnitId() {
        return firstUnitId;
    }

    public void setFirstUnitId(Long firstUnitId) {
        this.firstUnitId = firstUnitId;
    }

    public Long getLastUnitId() {
        return lastUnitId;
    }

    public void setLastUnitId(Long lastUnitId) {
        this.lastUnitId = lastUnitId;
    }

    public Long getFirstFlatId() {
        return firstFlatId;
    }

    public void setFirstFlatId(Long firstFlatId) {
        this.firstFlatId = firstFlatId;
    }

    public Long getLastFlatId() {
        return lastFlatId;
    }

    public void setLastFlatId(Long lastFlatId) {
        this.lastFlatId = lastFlatId;
    }

    public Long getFirstRoomId() {
        return firstRoomId;
    }

    public void setFirstRoomId(Long firstRoomId) {
        this.firstRoomId = firstRoomId;
    }

    public Long getLastRoomId() {
        return lastRoomId;
    }

    public void setLastRoomId(Long lastRoomId) {
        this.lastRoomId = lastRoomId;
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
