package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.dto.*;
import com.hisarresearch.wms.service.dto.mikro.v16.OrderParamsDTO;
import com.hisarresearch.wms.service.erp.ErpGatewayRegistry;
import com.hisarresearch.wms.service.erp.ErpGatewayRouter;
import com.hisarresearch.wms.service.erp.OrderDetailQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * ERP'den bagimsiz tek siparis ucu.
 *
 * <p>Hangi ERP'ye gidilecegine burada karar verilmez; {@link ErpGatewayRouter} oturumdaki
 * sirketin {@code erpType} degerine bakarak adaptoru secer. Yeni bir ERP eklendiginde
 * bu sinifta hicbir degisiklik gerekmez: {@code ErpOrderGateway} uygulayan yeni bir
 * {@code @Service} yazip {@code erpTypes()} ile kendi tipini bildirmesi yeterlidir.
 *
 * <p>Uc adresleri ve yanit sekilleri korunmustur; istemci tarafinda degisiklik gerekmez.
 */
@RestController
@RequestMapping("/api")
public class ErpOrderResource {

    private final Logger log = LoggerFactory.getLogger(ErpOrderResource.class);

    private final ErpGatewayRouter erpGatewayRouter;
    private final ErpGatewayRegistry erpGatewayRegistry;
    private final OrderDetailQueryService orderDetailQueryService;

    public ErpOrderResource(ErpGatewayRouter erpGatewayRouter, ErpGatewayRegistry erpGatewayRegistry,
                            OrderDetailQueryService orderDetailQueryService) {
        this.erpGatewayRouter = erpGatewayRouter;
        this.erpGatewayRegistry = erpGatewayRegistry;
        this.orderDetailQueryService = orderDetailQueryService;
    }

    @GetMapping("/depoList")
    public Object getDepoList() throws Exception {
        log.debug("REST request to get depo List");
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.getDepoList(ctx.getToken(), ctx.getApiPath(), erpGatewayRouter.currentCompanyCode());
    }

    @GetMapping("/firmList/{depoNo}/{sipTip}")
    public Object getFirmList(@PathVariable int depoNo, @PathVariable int sipTip) throws Exception {
        log.debug("REST request to get firm list by depoNo = {}, sipTip = {}", depoNo, sipTip);
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.getFirmList(ctx.getToken(), ctx.getApiPath(), depoNo, sipTip);
    }

    @PostMapping("/firmOrderList")
    public Object getOrderListByCariKod(@RequestBody AurFirmListDto aurFirmListDto) throws Exception {
        log.debug("REST request to get orders by cari kod {}", aurFirmListDto.getFirmCode());
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.getCariOrderList(ctx.getToken(), ctx.getApiPath(), aurFirmListDto);
    }

    @PostMapping("/firmOrderBulkList")
    public Object getOrderListBulk(@RequestBody AurFirmListDto aurFirmListDto) throws Exception {
        log.debug("REST request to get orders bulk for cari kod {}", aurFirmListDto.getFirmCode());
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.getCariOrderDetailList(ctx.getToken(), ctx.getApiPath(), aurFirmListDto);
    }

    @GetMapping("/orderDetail/{orderNo}/{sipTip}/{depoNo}")
    public Object getOrderDetailById(@PathVariable String orderNo, @PathVariable Integer sipTip,
                                     @PathVariable Integer depoNo) throws Exception {
        log.debug("REST request to get order details by orderNo = {}, sipTip = {}, depoNo = {}", orderNo, sipTip, depoNo);
        // OrderDetailQueryService ERP'den bagimsizdir: satirlari gateway'den alir, urun ve
        // parcali urun bilgisiyle zenginlestirir. Kaynak secimini router yapar.
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return orderDetailQueryService.getOrderDetail(ctx.getToken(), ctx.getApiPath(), orderNo, sipTip, depoNo);
    }

    @PostMapping("/v16/order-details")
    public Object getOrderComprehensiveDetails(@RequestBody OrderParamsDTO orderParamsDTO) throws Exception {
        log.debug("REST request to get comprehensive order details by request {}", orderParamsDTO);
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.getOrderComprehensiveDetails(ctx.getToken(), ctx.getApiPath(), orderParamsDTO);
    }

    @PostMapping("/firmadanMalKabul/{addressId}")
    public Object receiveOrder(@PathVariable Long addressId,
                               @RequestBody @Valid MalKabulRequestDto malKabulRequestDto) throws Exception {
        log.debug("REST request to receive order with addressId {}", addressId);
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.receiveOrder(ctx.getToken(), ctx.getApiPath(), malKabulRequestDto, addressId);
    }

    @PostMapping("/musteriSevkiyat")
    public Object dispatchOrder(@RequestBody @Valid SevkiyatRequestDto sevkiyatRequestDto) throws Exception {
        log.debug("REST request to dispatch order");
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.dispatchOrder(ctx.getToken(), ctx.getApiPath(), sevkiyatRequestDto);
    }

    @PostMapping("/firmStockOrderList")
    public Object getFirmStockOrderList(@RequestBody FirmStockOrderListRequestDto request) throws Exception {
        log.debug("REST request to get firm stock order list {}", request);
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.getFirmStockOrderList(ctx.getToken(), ctx.getApiPath(), request);
    }

    @PostMapping("/waybillList")
    public Object getWaybillList(@RequestBody WaybillQueryRequestDto request) throws Exception {
        log.debug("REST request to get waybill list {}", request);
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.getWaybillList(ctx.getToken(), ctx.getApiPath(), request);
    }

    @PostMapping("/productInfo")
    public Object getProductInfo(@RequestBody ProductInfoRequestDto request) throws Exception {
        log.debug("REST request to get product info {}", request);
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.getProductInfo(ctx.getToken(), ctx.getApiPath(), request);
    }

    @GetMapping("/produceBarkod/{stockCode}")
    public Object produceBarcode(@PathVariable String stockCode) throws Exception {
        log.debug("REST request to produce barcode from stockCode {}", stockCode);
        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        return erpGatewayRouter.generateBarcode(ctx.getToken(), ctx.getApiPath(), stockCode);
    }

    /** Hangi ERP tipinin hangi adaptore bagli oldugunu gosterir; kurulum dogrulamasi icin. */
    @GetMapping("/erp/adapters")
    public Map<ErpConnectionType, String> registeredAdapters() {
        return erpGatewayRegistry.registered();
    }
}
