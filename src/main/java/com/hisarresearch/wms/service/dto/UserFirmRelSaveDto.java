package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.User;

import java.util.List;

public class UserFirmRelSaveDto {
    public List<FirmDto> firmList;

    public List<User> userList;

    public List<FirmDto> getFirmList() {
        return firmList;
    }

    public void setFirmList(List<FirmDto> firmList) {
        this.firmList = firmList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
