package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.dto.AurCariDto;
import com.hisarresearch.wms.service.dto.AurCariOrderDetailDto;
import com.hisarresearch.wms.service.dto.AurCariOrderDetailListDto;
import com.hisarresearch.wms.service.dto.AurCariOrderDto;
import com.hisarresearch.wms.service.dto.AurFirmListDto;
import com.hisarresearch.wms.service.dto.MalKabulRequestDto;
import com.hisarresearch.wms.service.dto.SevkiyatRequestDto;
import com.hisarresearch.wms.service.dto.mikro.StockDetailResponseDto;
import com.hisarresearch.wms.service.dto.mikro.v16.OrderParamsDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Siparis okuma islemleri icin tek sozlesme.
 *
 * <p>Uygulamanin geri kalani ERP'nin ne oldugunu bilmez; her ERP bu arayuzun bir
 * adaptorudur ({@link MikroServices}, {@link LocalOrderGateway}) ve hepsi ayni
 * kanonik DTO'lari dondurur. Yeni bir ERP eklendiginde ya da mevcut birinin
 * yaniti degistiginde sadece ilgili adaptor guncellenir; resource/servis katmani
 * ve istemci sozlesmesi ayni kalir.
 *
 * <p>{@code token} ve {@code apiPath} uzak ERP cagrilari icindir; yerel modda
 * {@code null} gelir ve kullanilmaz. Ikisini de {@link ErpGatewayRouter#context()}
 * uretir.
 */
public interface ErpOrderGateway {

    /**
     * Bu adaptorun hangi ERP tip(ler)ini karsiladigi.
     *
     * <p>{@link ErpGatewayRegistry} tum {@code ErpOrderGateway} bean'lerini bu degere
     * gore indeksler. Yeni bir ERP eklemek icin tek yapilmasi gereken, bu arayuzu
     * uygulayan yeni bir {@code @Service} yazip burada kendi tipini bildirmesidir;
     * router, resource ve istemci sozlesmesi degismez.
     *
     * <p>Bos kume donen bean'ler kayit defterine alinmaz (orn. router'in kendisi).
     */
    Set<ErpConnectionType> erpTypes();

    /**
     * Uzak ERP icin oturum anahtari. Yerel adaptor {@code null} doner; cagiran taraf
     * token'i {@link ErpGatewayRouter#context()} uzerinden alir.
     */
    String getToken(String apiPath, String apiParameters) throws Exception;

    /** Belirtilen depo ve siparis tipi icin acik siparisi olan cariler. */
    List<AurCariDto> getFirmList(String token, String apiPath, int depoNo, int sipTip) throws Exception;

    /** Bir carinin acik siparisleri, siparis basligi bazinda gruplanmis halde. */
    List<AurCariOrderDto> getCariOrderList(String token, String apiPath, AurFirmListDto aurFirmListDto) throws Exception;

    /** Bir carinin acik siparis satirlari, duz liste halinde (WMS durumuyla zenginlestirilmis). */
    List<AurCariOrderDetailListDto> getCariOrderDetailList(String token, String apiPath, AurFirmListDto aurFirmListDto) throws Exception;

    /** Tek bir siparisin satir detaylari. */
    List<AurCariOrderDetailDto> getOrderDetail(String token, String apiPath, String orderNo, Integer sipTip, Integer depoNo) throws Exception;

    /** Barkod bazinda stok/urun detaylari. */
    Map<String, StockDetailResponseDto> getStockDetails(String token, String apiPath, List<String> barcodes, Integer depoNo) throws Exception;

    /** Depo listesi. */
    Object getDepoList(String token, String apiPath, String companyCode) throws Exception;

    /** Siparisin tum satirlariyla genis detayi. */
    Object getOrderComprehensiveDetails(String token, String apiPath, OrderParamsDTO orderParamsDTO) throws Exception;

    // ---------------------------------------------------------------------
    // Yazan islemler.
    //
    // Bu uclu heniz kanonik bir yanit sekline sahip degil: Mikro adaptoru ERP'nin
    // ham cevabini geciriyor, yerel adaptor ise ErpOperationResult donduruyor.
    // Yerel is kurallari yazildiginda ortak sekil burada sabitlenmeli.
    // ---------------------------------------------------------------------

    /** Firmadan mal kabul. */
    Object receiveOrder(String token, String apiPath, MalKabulRequestDto malKabulRequestDto, Long addressId) throws Exception;

    /** Musteriye sevkiyat. */
    Object dispatchOrder(String token, String apiPath, SevkiyatRequestDto sevkiyatRequestDto) throws Exception;

    /** Stok kodundan barkod uretimi. */
    Object generateBarcode(String token, String apiPath, String stokKod) throws Exception;
}
