package com.hisarresearch.wms.domain;

import java.util.ArrayList;
import java.util.List;

public class MailList {
    List<String> mailList;

    public List<String> getMailList() {
        return mailList;
    }

    public void setMailList(List<String> mailList) {
        this.mailList = mailList;
    }

    public List<String> addMail(String mail) {
        if(this.mailList == null){
            this.mailList = new ArrayList<>();
        }
        this.mailList.add(mail);
        return this.mailList;
    }
}
