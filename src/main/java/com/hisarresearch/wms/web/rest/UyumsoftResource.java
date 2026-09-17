package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.dto.base.ResponseDto;
import com.hisarresearch.wms.service.erp.MikroServices;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.erp.UyumsoftService;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.dto.uyumsoft.CheckQueueStatusDTO;
import com.hisarresearch.wms.service.dto.uyumsoft.SevkiyatIrsaliyeRequestDTO;
import com.hisarresearch.wms.service.dto.uyumsoft.UrunKabulIrsaliyeRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Transactional
public class UyumsoftResource {
    private final Logger log = LoggerFactory.getLogger(UyumsoftResource.class);

    private final UserService userService;

    private final UyumsoftService uyumsoftService;

    private final MikroServices mikroServices;

    public UyumsoftResource(UserService userService, UyumsoftService uyumsoftService, MikroServices mikroServices) {
        this.userService = userService;
        this.uyumsoftService = uyumsoftService;
        this.mikroServices = mikroServices;
    }

    @PostMapping("/uyumsoft-firma-mal-kabul/{orderInfo}")
    public Object receiver(@RequestBody UrunKabulIrsaliyeRequestDTO urunKabulIrsaliyeRequestDTO, @PathVariable String orderInfo) throws Exception {
        log.debug("REST request to generate receiving document");
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        if (aurCompanyDto.getErpType() == ErpConnectionType.UYUMSOFT) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return uyumsoftService.firmadanMalKabulUyumsoft(token, aurCompanyDto.getApiEndPoint(), urunKabulIrsaliyeRequestDTO,orderInfo);
        }
        return null;
    }

    @PostMapping("/uyumsoft-firma-sevkiyat/{orderInfo}")
    public Object dispatchOrder (@Valid @RequestBody SevkiyatIrsaliyeRequestDTO sevkiyatIrsaliyeRequestDTO, @PathVariable String orderInfo) throws Exception {
        log.debug("REST request to dispatch order by request {}",sevkiyatIrsaliyeRequestDTO);
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        if (aurCompanyDto.getErpType() == ErpConnectionType.UYUMSOFT) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return uyumsoftService.sevkiyatProcessUyumsoft(token, aurCompanyDto.getApiEndPoint(), sevkiyatIrsaliyeRequestDTO, orderInfo);
        }
        return null;
    }

    @PostMapping ("/uyumsoft")
    public Object executeService(@RequestBody RequestDto requestDto) throws Exception{
        log.debug("REST request to generic executeService by request {}",requestDto);
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        if (aurCompanyDto.getErpType() == ErpConnectionType.UYUMSOFT) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return uyumsoftService.executeServiceWrapper(token, aurCompanyDto.getApiEndPoint(), requestDto);
        }
        return null;

    }

    @PostMapping ("/check-queue-status")
    public String checkQueueStatus(@Valid @RequestBody CheckQueueStatusDTO checkQueueStatusDTO) throws Exception{
        log.debug("REST request to check order queue status by request {}",checkQueueStatusDTO);
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        if (aurCompanyDto.getErpType() == ErpConnectionType.UYUMSOFT) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return uyumsoftService.checkQueueStatus(token, aurCompanyDto.getApiEndPoint(), checkQueueStatusDTO);
        }
        throw new RuntimeException("Invalid ErpConnection type");

    }

    @PostMapping("/async-uyumsoft-firma-sevkiyat/{orderInfo}")
    public Object asyncDispatcher(@RequestBody @Valid SevkiyatIrsaliyeRequestDTO sevkiyatIrsaliyeRequestDTO, @PathVariable String orderInfo, @RequestHeader("Authorization") String tokenHeader) throws Exception {
        log.debug("REST request to async dispatch order by request {}",sevkiyatIrsaliyeRequestDTO);
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        sevkiyatIrsaliyeRequestDTO.setToken(tokenHeader);

        if (aurCompanyDto.getErpType() == ErpConnectionType.UYUMSOFT) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return uyumsoftService.asyncDispatchProcess(token, aurCompanyDto.getApiEndPoint(), sevkiyatIrsaliyeRequestDTO, orderInfo);
        }
        return null;
    }

    @GetMapping("/siparisKapat/{orderInfo}")
    public void completeOrder(@PathVariable String orderInfo) throws Exception {
        log.debug("REST request to complete order after check queue status by orderInfo : {}",orderInfo);
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        if (aurCompanyDto.getErpType() == ErpConnectionType.UYUMSOFT) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            uyumsoftService.completeOrder(token, aurCompanyDto.getApiEndPoint(), orderInfo);
        }
    }

    @PostMapping("/fallback-dispatch-order")
    public Object fallbackDispatchOrder(@RequestBody ResponseDto responseDto) throws Exception {
        log.debug("REST request to generate fallback process");
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        if (aurCompanyDto.getErpType() == ErpConnectionType.UYUMSOFT) {
            uyumsoftService.fallbackDispatchProcess(responseDto);
        }
        return null;
    }

}
