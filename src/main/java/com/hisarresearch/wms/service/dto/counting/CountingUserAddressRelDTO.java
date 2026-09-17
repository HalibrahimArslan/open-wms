package com.hisarresearch.wms.service.dto.counting;

import javax.validation.constraints.NotNull;

public class CountingUserAddressRelDTO {
    private Long id;

    @NotNull
    private Long addressId;

    private String countingAddress;

    private String login;

    @NotNull
    private Long userId;

    @NotNull
    private Long countingDefinitionId;

    private boolean counted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Long getAddressId() {
        return addressId;
    }

    public void setAddressId(@NotNull Long addressId) {
        this.addressId = addressId;
    }

    public String getCountingAddress() {
        return countingAddress;
    }

    public void setCountingAddress(String countingAddress) {
        this.countingAddress = countingAddress;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public @NotNull Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public @NotNull Long getCountingDefinitionId() {
        return countingDefinitionId;
    }

    public void setCountingDefinitionId(@NotNull Long countingDefinitionId) {
        this.countingDefinitionId = countingDefinitionId;
    }

    public boolean isCounted() {
        return counted;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }
}
