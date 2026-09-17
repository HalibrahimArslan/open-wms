package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.FirmOrdersByCariKodRequestDto;
import com.hisarresearch.wms.service.dto.OrderDetailListRequestDto;
import com.hisarresearch.wms.service.erp.MikroServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mikro v16'ya ozel, heniz ortak {@code ErpOrderGateway} sozlesmesine tasinmamis uclar.
 *
 * <p>Diger siparis uclari (depo listesi, firma listesi, siparis listesi/detayi, mal
 * kabul, sevkiyat, barkod uretimi) {@link ErpOrderResource} altindadir; oradaki uclar
 * ERP'den bagimsizdir ve sirketin {@code erpType} degerine gore yonlenir.
 */
@RestController
@RequestMapping("/api")
public class Mikrov16Resource {

    private final Logger log = LoggerFactory.getLogger(Mikrov16Resource.class);

    private final UserService userService;
    private final MikroServices mikroServices;

    public Mikrov16Resource(UserService userService, MikroServices mikroServices) {
        this.userService = userService;
        this.mikroServices = mikroServices;
    }

    /**
     * Tek bir cari icin acik siparisleri (orderList/orderLineItemCount) getirir;
     * dogrudan-cari sevkiyat akisinin ihtiyaci. {@link ErpOrderResource#getFirmList}
     * ile ayni islevi paylasir ama su an ayri bir uc; ileride birlestirilebilir.
     */
    @PostMapping("/mikro/firmOrdersByCariKod")
    public Object getFirmOrdersByCariKod(@RequestBody FirmOrdersByCariKodRequestDto request) throws Exception {
        log.debug("REST request to get Mikro firm orders by cariKod = {}", request.getCariKod());
        AurCompanyDTO aurCompanyDto = userService.checkErpType(ErpConnectionType.MIKRO_V16);
        String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        return mikroServices.getFirmOrdersByCariKod(token, aurCompanyDto.getApiEndPoint(), request);
    }

    /**
     * Belirtilen siparis numaralarinin kalem detaylarini getirir; {@link ErpOrderResource}
     * altindaki {@code getCariOrderDetailList} ile ayni zenginlestirmeyi paylasir ama
     * su an ayri bir uc; ileride birlestirilebilir.
     */
    @PostMapping("/mikro/orderDetailsByOrderNos")
    public Object getOrderDetailListByOrderNos(@RequestBody OrderDetailListRequestDto request) throws Exception {
        log.debug("REST request to get Mikro order details by orderNoList = {}", request.getOrderNoList());
        AurCompanyDTO aurCompanyDto = userService.checkErpType(ErpConnectionType.MIKRO_V16);
        String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        return mikroServices.getOrderDetailListByOrderNos(token, aurCompanyDto.getApiEndPoint(), request);
    }
}
