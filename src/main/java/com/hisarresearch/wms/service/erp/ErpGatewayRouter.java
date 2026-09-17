package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.ApiParameters;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.*;
import com.hisarresearch.wms.service.dto.mikro.StockDetailResponseDto;
import com.hisarresearch.wms.service.dto.mikro.v16.OrderParamsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ERP cagrilari icin tek giris noktasi.
 *
 * <p>Oturumdaki kullanicinin sirketindeki {@code erpType} degerine gore
 * {@link ErpGatewayRegistry}'den uygun adaptoru bulur, uzak ERP icin gereken
 * token/apiPath'i uretir ve cagriyi devreder. Boylece resource ve servis katmani
 * hangi ERP'nin bagli oldugunu hic bilmez.
 *
 * <p>Entegrasyon kapaliysa ({@code erpType = LOCAL} ya da {@code erpApiActive != "1"})
 * her zaman yerel adaptor kullanilir. Sirketin ERP tipi icin kayitli adaptor yoksa
 * yine yerel adaptora dusulur; sistem hata vermek yerine calismaya devam eder.
 *
 * <p>{@code @Primary} olmasinin sebebi {@link OrderDetailQueryService}'in tek bir
 * {@code ErpOrderGateway} enjekte etmesi. Kendisi kayit defterine girmemek icin
 * {@link #erpTypes()} bos kume doner.
 */
@Service
@Primary
public class ErpGatewayRouter implements ErpOrderGateway {

    private final Logger log = LoggerFactory.getLogger(ErpGatewayRouter.class);

    /** Uzak ERP cagrisi icin oturum bilgisi; yerel modda ikisi de {@code null}. */
    public static final class ErpCallContext {
        private final String token;
        private final String apiPath;

        ErpCallContext(String token, String apiPath) {
            this.token = token;
            this.apiPath = apiPath;
        }

        public String getToken() {
            return token;
        }

        public String getApiPath() {
            return apiPath;
        }
    }

    private final ErpGatewayRegistry registry;
    private final UserService userService;

    public ErpGatewayRouter(ErpGatewayRegistry registry, UserService userService) {
        this.registry = registry;
        this.userService = userService;
    }

    @Override
    public Set<ErpConnectionType> erpTypes() {
        return Collections.emptySet(); // yonlendirici kendisi kayit defterine girmez
    }

    /** Oturumdaki sirket icin secilen adaptor. */
    public ErpOrderGateway gatewayFor(AurCompanyDTO company) {
        if (ErpModeResolver.isLocalMode(company)) {
            return registry.local();
        }
        ErpConnectionType type = company.getErpType();
        return registry.find(type).orElseGet(() -> {
            log.warn("{} ERP tipi icin adaptor bulunamadi, yerel adaptore dusuluyor", type);
            return registry.local();
        });
    }

    private ErpOrderGateway gateway() {
        return gatewayFor(userService.getUserCompanyInfo());
    }

    /**
     * Oturumdaki sirket icin cagri baglami uretir. Token'i secilen adaptorun kendisi
     * verir; yerel modda ERP'ye hic gidilmedigi icin ikisi de {@code null} doner.
     */
    public ErpCallContext context() throws Exception {
        AurCompanyDTO company = userService.getUserCompanyInfo();
        if (ErpModeResolver.isLocalMode(company)) {
            return new ErpCallContext(null, null);
        }
        String token = gatewayFor(company).getToken(company.getApiEndPoint(), company.getApiParameters());
        return new ErpCallContext(token, company.getApiEndPoint());
    }

    /** Oturumdaki sirketin kodu; yerel adaptorde depo filtresi icin kullanilir. */
    public String currentCompanyCode() {
        return String.valueOf(userService.getUserCompanyInfo().getCompanyCode());
    }

    @Override
    public String getToken(String apiPath, ApiParameters apiParameters) throws Exception {
        return gateway().getToken(apiPath, apiParameters);
    }

    @Override
    public List<AurCariDto> getFirmList(String token, String apiPath, int depoNo, int sipTip) throws Exception {
        return gateway().getFirmList(token, apiPath, depoNo, sipTip);
    }

    @Override
    public List<AurCariOrderDto> getCariOrderList(String token, String apiPath, AurFirmListDto aurFirmListDto) throws Exception {
        return gateway().getCariOrderList(token, apiPath, aurFirmListDto);
    }

    @Override
    public List<AurCariOrderDetailListDto> getCariOrderDetailList(String token, String apiPath, AurFirmListDto aurFirmListDto) throws Exception {
        return gateway().getCariOrderDetailList(token, apiPath, aurFirmListDto);
    }

    @Override
    public List<AurCariOrderDetailDto> getOrderDetail(String token, String apiPath, String orderNo,
                                                      Integer sipTip, Integer depoNo) throws Exception {
        return gateway().getOrderDetail(token, apiPath, orderNo, sipTip, depoNo);
    }

    @Override
    public Map<String, StockDetailResponseDto> getStockDetails(String token, String apiPath,
                                                               List<String> barcodes, Integer depoNo) throws Exception {
        return gateway().getStockDetails(token, apiPath, barcodes, depoNo);
    }

    @Override
    public List<StockDetailResponseDto> getProductInfo(String token, String apiPath, ProductInfoRequestDto request) throws Exception {
        return gateway().getProductInfo(token, apiPath, request);
    }

    @Override
    public Object getDepoList(String token, String apiPath, String companyCode) throws Exception {
        return gateway().getDepoList(token, apiPath, companyCode);
    }

    @Override
    public Object getOrderComprehensiveDetails(String token, String apiPath, OrderParamsDTO orderParamsDTO) throws Exception {
        return gateway().getOrderComprehensiveDetails(token, apiPath, orderParamsDTO);
    }

    @Override
    public List<AurWaybillDto> getWaybillList(String token, String apiPath, WaybillQueryRequestDto request) throws Exception {
        return gateway().getWaybillList(token, apiPath, request);
    }

    @Override
    public List<AurCariOrderDetailListDto> getFirmStockOrderList(String token, String apiPath, FirmStockOrderListRequestDto request) throws Exception {
        return gateway().getFirmStockOrderList(token, apiPath, request);
    }

    @Override
    public Object receiveOrder(String token, String apiPath, MalKabulRequestDto malKabulRequestDto, Long addressId) throws Exception {
        return gateway().receiveOrder(token, apiPath, malKabulRequestDto, addressId);
    }

    @Override
    public Object dispatchOrder(String token, String apiPath, SevkiyatRequestDto sevkiyatRequestDto) throws Exception {
        return gateway().dispatchOrder(token, apiPath, sevkiyatRequestDto);
    }

    @Override
    public Object generateBarcode(String token, String apiPath, String stokKod) throws Exception {
        return gateway().generateBarcode(token, apiPath, stokKod);
    }
}
