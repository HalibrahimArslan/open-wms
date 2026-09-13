package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.repository.*;
import com.hisarresearch.wms.service.AurLogService;
import com.hisarresearch.wms.service.MailService;
import com.hisarresearch.wms.service.dto.AurProcessHtmlRequestDto;
import com.hisarresearch.wms.utility.AurHelper;
import com.hisarresearch.wms.exception.validation.InvalidOrderException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Transactional
public class MailReosurce {


    private final MailService mailService;

    private final AurGrupMailAdresRepository aurGrupMailAdresRepository;

    private final AurOrderMasterRepository aurOrderMasterRepository;


    private final UserRepository userRepository;

    private final AurLogService aurLogService;

    private final AurMailAddToRepository aurMailAddToRepository;

    private final WarehouseRepository warehouseRepository;

    private final EntityManager em;

    public MailReosurce(MailService mailService, AurGrupMailAdresRepository aurGrupMailAdresRepository, AurOrderMasterRepository aurOrderMasterRepository, UserRepository userRepository, AurLogService aurLogService, AurMailAddToRepository aurMailAddToRepository, WarehouseRepository warehouseRepository, EntityManager em) {
        this.mailService = mailService;
        this.aurGrupMailAdresRepository = aurGrupMailAdresRepository;
        this.aurOrderMasterRepository = aurOrderMasterRepository;
        this.userRepository = userRepository;
        this.aurLogService = aurLogService;
        this.aurMailAddToRepository = aurMailAddToRepository;
        this.warehouseRepository = warehouseRepository;
        this.em = em;
    }

    @PostMapping("/sendZReportMail")
    public void sendZReport(@RequestBody List<String> senderList){
        try{
            Query q = em.createNativeQuery("SELECT * FROM aur_vw_z_raporu ", AurVwZReport.class);
            List<AurVwZReport> response = q.getResultList();
            mailService.sendZReport(senderList,response);
        }
        catch (Exception e){
            String message = e.getMessage();
            throw new RuntimeException("Z Rapor mail servisinde hata alındı" + message);
        }

    }

    @PostMapping("/sendMail/{mailgrup}")
    public void sendMailBulk(@RequestBody List<String> mailgrup){
        mailService.sendEmailBulk(mailgrup,"Test","Test",false,true);
    }


    @GetMapping ("/sendMailv2/{orderId}")
    public void sendMailv2(@PathVariable Long orderId) throws Exception {
         mailService.sendMailVersion2(orderId);

    }

    @GetMapping ("/sendMailWithAttachment/{orderId}")
    public void sendMailWithAttachment(@PathVariable Long orderId) throws Exception {
        mailService.getObjectFromS3AndSendEmail(orderId);
    }

    @GetMapping ("/sendNotFoundMail")
    public void sendNotFoundMail(){
        mailService.sendNotFoundMail("TEST-001", "Test Cari", "Test Alt Cari");
    }

}
