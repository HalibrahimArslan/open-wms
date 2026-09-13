package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.dto.*;
import com.hisarresearch.wms.service.dto.erp.ErpOperationResult;
import com.hisarresearch.wms.service.dto.mikro.StockDetailResponseDto;
import com.hisarresearch.wms.service.dto.mikro.v16.OrderParamsDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * UYUMSOFT icin {@link ErpOrderGateway} adaptoru.
 *
 * <p>Uyumsoft entegrasyonu yalnizca yazma tarafini kapsiyor (irsaliye bazli mal kabul
 * ve sevkiyat, bkz. {@code UyumsoftResource}); siparis okuma ucu yok. Bu kurulumlarda
 * siparisler yerel {@code aur_erp_data} tablosundan okundugu icin okuma cagrilari
 * {@link LocalOrderGateway}'e devredilir.
 *
 * <p>Yazma cagrilari ortak {@code MalKabulRequestDto} / {@code SevkiyatRequestDto}
 * sozlesmesine heniz baglanmadi; Uyumsoft kendi irsaliye DTO'larini kullaniyor.
 * Ortak sekle tasindiginda burasi {@code UyumsoftService}'e delege edecek.
 */
@Service
public class UyumsoftOrderGateway implements ErpOrderGateway {

    private final LocalOrderGateway localOrderGateway;

    public UyumsoftOrderGateway(LocalOrderGateway localOrderGateway) {
        this.localOrderGateway = localOrderGateway;
    }

    @Override
    public Set<ErpConnectionType> erpTypes() {
        return Set.of(ErpConnectionType.UYUMSOFT);
    }

    @Override
    public String getToken(String apiPath, String apiParameters) {
        return null; // okuma tarafi yerelden geldigi icin token gerekmiyor
    }

    // --- okuma: yerel veriden -------------------------------------------------

    @Override
    public List<AurCariDto> getFirmList(String token, String apiPath, int depoNo, int sipTip) {
        return localOrderGateway.getFirmList(token, apiPath, depoNo, sipTip);
    }

    @Override
    public List<AurCariOrderDto> getCariOrderList(String token, String apiPath, AurFirmListDto aurFirmListDto) {
        return localOrderGateway.getCariOrderList(token, apiPath, aurFirmListDto);
    }

    @Override
    public List<AurCariOrderDetailListDto> getCariOrderDetailList(String token, String apiPath, AurFirmListDto aurFirmListDto) {
        return localOrderGateway.getCariOrderDetailList(token, apiPath, aurFirmListDto);
    }

    @Override
    public List<AurCariOrderDetailDto> getOrderDetail(String token, String apiPath, String orderNo,
                                                      Integer sipTip, Integer depoNo) {
        return localOrderGateway.getOrderDetail(token, apiPath, orderNo, sipTip, depoNo);
    }

    @Override
    public Map<String, StockDetailResponseDto> getStockDetails(String token, String apiPath,
                                                               List<String> barcodes, Integer depoNo) {
        return localOrderGateway.getStockDetails(token, apiPath, barcodes, depoNo);
    }

    @Override
    public Object getDepoList(String token, String apiPath, String companyCode) {
        return localOrderGateway.getDepoList(token, apiPath, companyCode);
    }

    @Override
    public Object getOrderComprehensiveDetails(String token, String apiPath, OrderParamsDTO orderParamsDTO) {
        return localOrderGateway.getOrderComprehensiveDetails(token, apiPath, orderParamsDTO);
    }

    // --- yazma: Uyumsoft'un kendi irsaliye uclarindan ------------------------

    @Override
    public Object receiveOrder(String token, String apiPath, MalKabulRequestDto malKabulRequestDto, Long addressId) {
        return ErpOperationResult.notImplemented(
            "Uyumsoft mal kabul (su an /api/uyumsoft-firma-mal-kabul/{orderInfo} ucundan yapiliyor)");
    }

    @Override
    public Object dispatchOrder(String token, String apiPath, SevkiyatRequestDto sevkiyatRequestDto) {
        return ErpOperationResult.notImplemented(
            "Uyumsoft sevkiyat (su an /api/uyumsoft-firma-sevkiyat/{orderInfo} ucundan yapiliyor)");
    }

    @Override
    public Object generateBarcode(String token, String apiPath, String stokKod) {
        return ErpOperationResult.notImplemented("Uyumsoft barkod uretimi");
    }
}
