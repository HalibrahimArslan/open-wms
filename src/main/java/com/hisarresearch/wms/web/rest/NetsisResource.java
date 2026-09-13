package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.erp.NetsisServices;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.netsis.AurSaveOrderDetailsRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Transactional
public class NetsisResource {
    private final Logger log = LoggerFactory.getLogger(NetsisResource.class);

    private final UserService userService;

    private final NetsisServices netsisServices;

    public NetsisResource(UserService userService, NetsisServices netsisServices) {
        this.userService = userService;
        this.netsisServices = netsisServices;
    }

    @PostMapping("/saveOrderDetails")
    public String saveOrderDetails(@RequestBody AurSaveOrderDetailsRequestDto aurSaveOrderRequestDto) throws Exception {
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        Integer erpCode = Integer.valueOf(aurCompanyDto.getErpTipi());

        if (ErpConnectionType.NETSIS.getErpCode() == erpCode) {
            String token = netsisServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            return netsisServices.saveOrderDetails(aurSaveOrderRequestDto, token, aurCompanyDto.getApiEndPoint());
        }

        return null;
    }
}
