package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.domain.MailList;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroolsService {

    @Autowired
    private KieContainer kieContainer;

    public MailList getMailList(AurOrderMaster orderMaster) {
        MailList mailList = new MailList();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("mailList", mailList);
        kieSession.insert(orderMaster);
        kieSession.fireAllRules();
        kieSession.dispose();
        return mailList;
    }

}
