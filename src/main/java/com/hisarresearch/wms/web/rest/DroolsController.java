package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.domain.MailList;
import com.hisarresearch.wms.service.AurOrderMasterService;
import com.hisarresearch.wms.service.DroolsService;
import com.hisarresearch.wms.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController()
@RequestMapping("/api")
public class DroolsController {

    @Autowired
    private DroolsService droolsService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    @PostMapping("/get-mail-list")
    public ResponseEntity<MailList> getMailList(@RequestBody AurOrderMaster orderMaster){
        MailList mailList = droolsService.getMailList(orderMaster);
        return new ResponseEntity<>(mailList, HttpStatus.OK);
    }

    @PostMapping("/rule/{orderInfo}/{ruleGroupName}")
    public ResponseEntity<MailList> getMailList(@PathVariable String orderInfo,@PathVariable String ruleGroupName){
        AurOrderMaster orderMaster = aurOrderMasterService.findByOrderInfo(orderInfo).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        MailList mailList = ruleService.executeRulesForMail(orderMaster,ruleGroupName);
        return new ResponseEntity<>(mailList, HttpStatus.OK);
    }

}
