package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.HttpService;
import com.hisarresearch.wms.service.erp.MikroServices;
import com.hisarresearch.wms.service.dto.DepolarArasiTransferErpDto;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.MalKabulRequestDto;
import com.hisarresearch.wms.service.dto.mikro.v15.MikroV15QueryDTO;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Transactional
public class Mikrov15Resource {
    private final Logger log = LoggerFactory.getLogger(Mikrov15Resource.class);

    private final UserService userService;

    private final MikroServices mikroServices;

    private final HttpService httpService;

    public Mikrov15Resource(UserService userService, MikroServices mikroServices,
                            HttpService httpService) {
        this.userService = userService;
        this.mikroServices = mikroServices;
        this.httpService = httpService;
    }

    @PostMapping ("/mikro-v15")
    public Object executeServiceMicro15(@RequestBody RequestDto dto) throws Exception{
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return httpService.executeService(token, aurCompanyDto.getApiEndPoint(), dto);
        }
        return null;

    }

    @PostMapping("/depolar-arasi-transfer-mikro-v15/{masterId}/{orderNo}/{sipTip}")
    public Object interWarehouseTransfer(@RequestBody DepolarArasiTransferErpDto depolarArasiTransferDto, @PathVariable String masterId, @PathVariable String orderNo, @PathVariable String sipTip) throws Exception {
        log.debug("REST request to generate inter-warehouse-transfer ");
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return mikroServices.depolarArasiTransfer(token, aurCompanyDto.getApiEndPoint(), depolarArasiTransferDto,masterId,orderNo,sipTip);
        }
        return null;
    }

    @PostMapping("/depolar-arasi-urun-kabul-mikro-v15/{masterId}/{orderNo}/{sipTip}")
    public Object interWarehouseTransferAccept(@RequestBody DepolarArasiTransferErpDto depolarArasiTransferDto, @PathVariable String masterId, @PathVariable String orderNo, @PathVariable String sipTip) throws Exception {
        log.debug("REST request to generate inter-warehouse-transfer accept ");
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return mikroServices.depolarArasiUrunKabul(token, aurCompanyDto.getApiEndPoint(), depolarArasiTransferDto,masterId,orderNo,sipTip);
        }
        return null;
    }

    @PostMapping("/firmadan-mal-kabul-mikro-v15/{addressId}")
    public Object receivingMicroV15(@PathVariable(name = "addressId") Long addressId, @RequestBody MalKabulRequestDto malKabulRequestDto) throws Exception {
        log.debug("REST request to generate receiving document");
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return mikroServices.receiveOrder(token, aurCompanyDto.getApiEndPoint(), malKabulRequestDto,addressId);
        }
        return null;
    }

    @PostMapping("/mikro/v15/siparissizGiris")
    public Object receivingWithoutOrder(@RequestBody MalKabulRequestDto malKabulRequestDto) throws Exception {
        log.debug("REST request to generate receiving without order document");
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        int erpCode = Integer.parseInt(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return mikroServices.receivingWithoutOrder(token, aurCompanyDto.getApiEndPoint(), malKabulRequestDto);
        }
        return null;
    }

    @PostMapping("/mikro/v15/iadeIrsaliyesi")
    public Object returnWaybill(@RequestBody MalKabulRequestDto malKabulRequestDto) throws Exception {
        log.debug("REST request to generate return waybill document");
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return mikroServices.returnWaybill(token, aurCompanyDto.getApiEndPoint(), malKabulRequestDto);
        }
        return null;
    }

    @PostMapping("/mikro/v15/sarfIrsaliyesi")
    public Object sarfIrsaliye(@RequestBody MalKabulRequestDto malKabulRequestDto) throws Exception {
        log.debug("REST request to generate Sarf document");
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return mikroServices.sarfWaybill(token, aurCompanyDto.getApiEndPoint(), malKabulRequestDto);
        }
        return null;
    }

    @GetMapping ("/v15/mikro/query")
    public Object microQuery(MikroV15QueryDTO queryParams) throws Exception{
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return mikroServices.mikroV15Query(token, aurCompanyDto.getApiEndPoint(), queryParams);
        }
        return null;

    }
    @GetMapping ("/v15/mikro/query/count")
    public Object microQueryCount(MikroV15QueryDTO queryParams) throws Exception{
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.MIKRO_V15.getErpCode() == erpCode) {
            String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return mikroServices.countOfMikroV15Query(token, aurCompanyDto.getApiEndPoint(), queryParams);
        }
        return null;

    }

}
