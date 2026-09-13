package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.HttpManagementService;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.erp.MikroServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mikro v16'ya ozel ham servis gecisi.
 *
 * <p>Siparis uclari (depo listesi, firma listesi, siparis listesi/detayi, mal kabul,
 * sevkiyat, barkod uretimi) buradan {@link ErpOrderResource} altina tasindi; oradaki
 * uclar ERP'den bagimsizdir ve sirketin {@code erpTipi} degerine gore yonlenir.
 *
 * <p>Geriye yalnizca Mikro'nun kendi servis adini alip dogrudan cagiran genel gecis
 * ucu kaldi; bu ucun dogasi geregi ERP'ye ozel oldugu icin ortak sozlesmeye tasinmadi.
 */
@RestController
@RequestMapping("/api")
public class Mikrov16Resource {

    private final Logger log = LoggerFactory.getLogger(Mikrov16Resource.class);

    private final UserService userService;
    private final MikroServices mikroServices;
    private final HttpManagementService httpManagementService;

    public Mikrov16Resource(UserService userService, MikroServices mikroServices,
                            HttpManagementService httpManagementService) {
        this.userService = userService;
        this.mikroServices = mikroServices;
        this.httpManagementService = httpManagementService;
    }

    @PostMapping("/executeServiceMikro")
    public Object executeService(@RequestBody RequestDto requestDto) throws Exception {
        log.debug("REST request to generic executeService by request {}", requestDto);
        AurCompanyDTO aurCompanyDto = userService.checkErpType(ErpConnectionType.MIKRO_V16);
        String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        return httpManagementService.executeService(token, aurCompanyDto.getApiEndPoint(), requestDto);
    }
}
