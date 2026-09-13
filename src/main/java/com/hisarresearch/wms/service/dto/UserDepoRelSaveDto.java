package com.hisarresearch.wms.service.dto;
import com.hisarresearch.wms.domain.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserDepoRelSaveDto {

    @NotNull
    private List<WarehouseDTO> warehouseList;

    @NotNull
    private List<User> userList;


    public @NotNull List<WarehouseDTO> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(@NotNull List<WarehouseDTO> warehouseList) {
        this.warehouseList = warehouseList;
    }

    public @NotNull List<User> getUserList() {
        return userList;
    }

    public void setUserList(@NotNull List<User> userList) {
        this.userList = userList;
    }
}
