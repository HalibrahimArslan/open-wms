package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.ApiParameters;
import com.hisarresearch.wms.service.AurOrderMasterService;
import com.hisarresearch.wms.service.ReceivingAddressService;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.AurCariDto;
import com.hisarresearch.wms.service.dto.AurCariOrderDetailDto;
import com.hisarresearch.wms.service.dto.AurCariOrderDetailListDto;
import com.hisarresearch.wms.service.dto.AurCariOrderDto;
import com.hisarresearch.wms.service.dto.AurFirmListDto;
import com.hisarresearch.wms.service.dto.AurWaybillDto;
import com.hisarresearch.wms.service.dto.DepolarArasiTransferErpDto;
import com.hisarresearch.wms.service.dto.FirmStockOrderListRequestDto;
import com.hisarresearch.wms.service.dto.MalKabulRequestDto;
import com.hisarresearch.wms.service.dto.OrderLineItemDto;
import com.hisarresearch.wms.service.dto.ProductInfoRequestDto;
import com.hisarresearch.wms.service.dto.SevkiyatRequestDto;
import com.hisarresearch.wms.service.dto.WaybillQueryRequestDto;
import com.hisarresearch.wms.service.dto.erp.ErpOperationResult;
import com.hisarresearch.wms.service.dto.mikro.StockDetailResponseDto;
import com.hisarresearch.wms.service.dto.mikro.v16.OrderParamsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ERP entegrasyonu olmadan calisan {@link ErpOrderGateway} implementasyonu.
 *
 * <p>Siparis ve stok bilgisini uzak bir ERP servisi yerine dogrudan yerel
 * tablolardan okur:
 * <ul>
 *     <li>siparis satirlari  -> {@code aur_erp_data}</li>
 *     <li>stok/urun bilgisi  -> {@code product}</li>
 *     <li>depodaki miktar    -> {@code aur_depo_urun_adres_stok}</li>
 * </ul>
 *
 * <p>{@code token} ve {@code apiPath} parametreleri arayuz sozlesmesi geregi
 * alinir ama kullanilmaz.
 *
 * <p>Hangi implementasyonun kullanilacagina {@link ErpGatewayRouter} karar verir.
 */
@Service
public class LocalOrderGateway implements ErpOrderGateway {

    private final Logger log = LoggerFactory.getLogger(LocalOrderGateway.class);

    private static final String ORDER_DETAIL_SQL =
        "select aed.barkod, aed.sip_teslim_miktar, aed.planlanan_sevk_tarihi, aed.sip_guid, " +
        "       aed.sip_miktar, aed.stok_adi, aed.stok_birimi, aed.sip_stok_kod, " +
        "       aed.teslim_tarihi, aed.sip_depo_no " +
        "  from aur_erp_data aed " +
        " where aed.sip_belge_no = :orderNo " +
        "   and aed.sip_tip = :sipTip " +
        "   and aed.sip_depo_no = :depoNo " +
        " order by aed.sip_satir_no";

    private static final String STOCK_DETAIL_SQL =
        "select p.barkod, p.stok_kodu, p.stok_adi, p.stok_birimi, p.kategori_adi, " +
        "       p.ana_grup, p.description, " +
        "       coalesce((select sum(s.miktar) " +
        "                   from aur_depo_urun_adres_stok s " +
        "                  where s.barcode = p.barkod " +
        "                    and s.company_code = p.company_code " +
        "                    and (cast(:depoNo as varchar) is null or s.depo_code = :depoNo)), 0) as depodaki_miktar " +
        "  from product p " +
        " where p.barkod in (:barcodes) " +
        "   and p.company_code = :companyCode";

    private static final String PRODUCT_BARCODES_BY_STOCK_CODE_SQL =
        "select p.barkod from product p where p.stok_kodu = :stockCode and p.company_code = :companyCode";

    private static final String RECEIVE_LINE_SQL =
        "update aur_erp_data set sip_teslim_miktar = sip_teslim_miktar + :amount " +
        " where sip_guid = :sipUid and sip_tip = 1";

    private final EntityManager em;
    private final UserService userService;
    private final AurLocalService aurLocalService;
    private final ReceivingAddressService receivingAddressService;
    private final AurOrderMasterService aurOrderMasterService;

    public LocalOrderGateway(EntityManager em, UserService userService, AurLocalService aurLocalService,
                             @Lazy ReceivingAddressService receivingAddressService,
                             @Lazy AurOrderMasterService aurOrderMasterService) {
        this.em = em;
        this.userService = userService;
        this.aurLocalService = aurLocalService;
        this.receivingAddressService = receivingAddressService;
        this.aurOrderMasterService = aurOrderMasterService;
    }

    @Override
    public List<AurCariDto> getFirmList(String token, String apiPath, int depoNo, int sipTip) {
        // cariBaglantiTipi, Mikro tarafinda da siparis tipinin metin karsiligi olarak kullaniliyor.
        return aurLocalService.getFirmList((short) sipTip, String.valueOf(sipTip), (short) depoNo);
    }

    @Override
    public List<AurCariOrderDto> getCariOrderList(String token, String apiPath, AurFirmListDto aurFirmListDto) {
        return aurLocalService.getCariOrderList(aurFirmListDto);
    }

    @Override
    public List<AurCariOrderDetailListDto> getCariOrderDetailList(String token, String apiPath, AurFirmListDto aurFirmListDto) {
        List<AurCariOrderDetailListDto> orderDetails = aurLocalService.getCariOrderDetailList(aurFirmListDto);
        fillDepoStockAmount(token, apiPath, orderDetails, aurFirmListDto);
        return orderDetails;
    }

    /**
     * Siparis satirlarina {@code stokMiktar} (depodaki mevcut miktar) yazar.
     *
     * <p>Mikro modunda bu alan ERP yanitinin icinde geliyor; yerel modda ERP olmadigi
     * icin ayni deger {@code aur_depo_urun_adres_stok} toplamindan uretilir. Alan
     * doldurulmazsa yanit ERP'ye gore farkli sekilde donmus olur (adaptor kurali 3) ve
     * Sevkiyat ekrani depoda stogu gorunmeyen satirlari secilemez kabul ettigi icin
     * yerel modda hicbir siparis sevkiyata alinamaz.
     */
    private void fillDepoStockAmount(String token, String apiPath, List<AurCariOrderDetailListDto> orderDetails,
                                     AurFirmListDto aurFirmListDto) {
        List<String> barcodes = orderDetails.stream()
            .map(AurCariOrderDetailListDto::getBarkod)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        if (barcodes.isEmpty()) {
            return;
        }

        // Yerel modda ERP depo numarasi ile WMS depo kodu ayni; istek tek depo listesiyle geliyor.
        Integer depoNo = aurFirmListDto.getDepoList() == null || aurFirmListDto.getDepoList().isEmpty()
            ? null
            : aurFirmListDto.getDepoList().get(0);

        Map<String, StockDetailResponseDto> stockByBarcode = getStockDetails(token, apiPath, barcodes, depoNo);

        for (AurCariOrderDetailListDto dto : orderDetails) {
            StockDetailResponseDto stock = stockByBarcode.get(dto.getBarkod());
            dto.setStokMiktar(stock == null ? 0d : stock.getDepodakiMiktar());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AurCariOrderDetailDto> getOrderDetail(String token, String apiPath, String orderNo,
                                                      Integer sipTip, Integer depoNo) {
        log.debug("Local ERP: {} nolu siparisin detaylari okunuyor (sipTip={}, depoNo={})", orderNo, sipTip, depoNo);

        Query q = em.createNativeQuery(ORDER_DETAIL_SQL);
        q.setParameter("orderNo", orderNo);
        q.setParameter("sipTip", sipTip);
        q.setParameter("depoNo", depoNo);

        List<Object[]> rows = q.getResultList();
        List<AurCariOrderDetailDto> result = new ArrayList<>(rows.size());

        for (Object[] row : rows) {
            AurCariOrderDetailDto dto = new AurCariOrderDetailDto();
            dto.setBarkod(asString(row[0]));
            dto.setTeslimMiktar(asDouble(row[1]));
            dto.setPlanlananSevkTarihi(asString(row[2]));
            dto.setSipUid(asString(row[3]));
            dto.setSiparisMiktar(asDouble(row[4]));
            dto.setStokAdi(asString(row[5]));
            dto.setStokBirimi(asString(row[6]));
            dto.setStokKodu(asString(row[7]));
            dto.setTeslimTarihi(asString(row[8]));
            dto.setDepoNo((int) asDouble(row[9]));
            // Mikro tarafinda "0" acik siparis satirini ifade ediyor; ayni sozlesmeyi koruyoruz.
            dto.setDurum("0");
            result.add(dto);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, StockDetailResponseDto> getStockDetails(String token, String apiPath,
                                                               List<String> barcodes, Integer depoNo) {
        if (barcodes == null || barcodes.isEmpty()) {
            return Collections.emptyMap();
        }
        log.debug("Local ERP: {} barkod icin stok detayi okunuyor (depoNo={})", barcodes.size(), depoNo);

        Query q = em.createNativeQuery(STOCK_DETAIL_SQL);
        q.setParameter("barcodes", barcodes);
        q.setParameter("companyCode", String.valueOf(userService.getUserCompanyCode()));
        q.setParameter("depoNo", depoNo == null ? null : String.valueOf(depoNo));

        List<Object[]> rows = q.getResultList();
        Map<String, StockDetailResponseDto> result = new HashMap<>(rows.size());

        for (Object[] row : rows) {
            StockDetailResponseDto dto = new StockDetailResponseDto();
            dto.setBarkod(asString(row[0]));
            dto.setStokKodu(asString(row[1]));
            dto.setStokAdi(asString(row[2]));
            dto.setStokBirimi(asString(row[3]));
            dto.setKategoriAdi(asString(row[4]));
            dto.setAnagrupAdi(asString(row[5]));
            dto.setDescription(asString(row[6]));
            dto.setDepodakiMiktar(asDouble(row[7]));
            result.putIfAbsent(dto.getBarkod(), dto);
        }
        return result;
    }

    /**
     * Urun bilgisini barkod(lar)a ya da stok koduna gore yerel {@code product} tablosundan okur.
     * Arayuz barkodla (sayim, urun adres tanimi) ya da yalnizca stok koduyla (sevkiyat) sorar;
     * {@code depoNo} 0 ya da bos ise stok tum depolarda toplanir.
     * TODO stokAdi ile arama henuz yok.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<StockDetailResponseDto> getProductInfo(String token, String apiPath, ProductInfoRequestDto request) {
        List<String> barcodes = new ArrayList<>();
        if (request.getBarkod() != null && !request.getBarkod().isBlank()) {
            barcodes.add(request.getBarkod());
        }
        if (request.getBarkodList() != null) {
            request.getBarkodList().stream().filter(b -> b != null && !b.isBlank()).forEach(barcodes::add);
        }
        if (barcodes.isEmpty() && request.getStokKodu() != null && !request.getStokKodu().isBlank()) {
            Query q = em.createNativeQuery(PRODUCT_BARCODES_BY_STOCK_CODE_SQL);
            q.setParameter("stockCode", request.getStokKodu());
            q.setParameter("companyCode", String.valueOf(userService.getUserCompanyCode()));
            barcodes.addAll(q.getResultList());
        }
        Integer depoNo = request.getDepoNo() == null || request.getDepoNo() == 0 ? null : request.getDepoNo();
        return new ArrayList<>(getStockDetails(token, apiPath, barcodes, depoNo).values());
    }

    @Override
    public java.util.Set<com.hisarresearch.wms.domain.enumeration.ErpConnectionType> erpTypes() {
        return java.util.Set.of(com.hisarresearch.wms.domain.enumeration.ErpConnectionType.LOCAL);
    }

    @Override
    public String getToken(String apiPath, ApiParameters apiParameters) {
        return null; // yerel modda ERP'ye gidilmiyor
    }

    @Override
    public Object getDepoList(String token, String apiPath, String companyCode) {
        return aurLocalService.getDepoList(companyCode);
    }

    // =====================================================================
    // Asagidaki metotlar sozlesmeyi tamamlamak icin tanimli; is kurallari
    // heniz yazilmadi. Cagri hata firlatmak yerine islenebilir bir sonuc
    // doner, boylece ekranlar yerel modda da akisini surdurebilir.
    // =====================================================================

    /**
     * Yerel mal kabul. Mikro adaptoruyle ayni WMS adimlarini calistirir: kabul edilen
     * urunler gecici adrese yerlestirilir, WMS siparisi irsaliye bilgileriyle kapatilir.
     * ERP'ye gonderim yerine yerel siparis kaynagi {@code aur_erp_data}'daki teslim
     * miktari artirilir; boylece tamamlanan satirlar listelerden duser. Adimlardan biri
     * basarisiz olursa hepsi geri alinir.
     * TODO rezerve listesi ve siparis maili (Mikro'da pushReserveList / OrderMailEvent) yok.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object receiveOrder(String token, String apiPath, MalKabulRequestDto malKabulRequestDto, Long addressId) {
        receivingAddressService.completeReceivingAddressOperation(malKabulRequestDto, addressId);
        aurOrderMasterService.completeReceiving(malKabulRequestDto);

        List<OrderLineItemDto> lines = malKabulRequestDto.getOrderDetailList() == null
            ? Collections.emptyList() : malKabulRequestDto.getOrderDetailList();
        for (OrderLineItemDto line : lines) {
            if (line.getSipUid() == null || line.getKabulMiktar() == null || line.getKabulMiktar() <= 0) {
                continue;
            }
            int updated = em.createNativeQuery(RECEIVE_LINE_SQL)
                .setParameter("amount", line.getKabulMiktar())
                .setParameter("sipUid", line.getSipUid())
                .executeUpdate();
            if (updated == 0) {
                log.warn("Local ERP: {} sip_guid'li mal kabul satiri bulunamadi", line.getSipUid());
            }
        }
        log.debug("Local ERP: {} siparisi icin mal kabul tamamlandi ({} satir)", malKabulRequestDto.getOrderId(), lines.size());
        return new ErpOperationResult(true, "Yerel mod: mal kabul ERP'ye gonderilmedi", malKabulRequestDto.getOrderNo());
    }

    /**
     * TODO yerel sevkiyat is kurallari.
     *
     * <p>Mikro tarafinda sevkiyat sonrasi siparis satirlari kapatiliyor; yerel modda
     * ayni etki {@code aur_erp_data.sip_teslim_miktar} artirilarak saglanabilir.
     */
    @Override
    public Object dispatchOrder(String token, String apiPath, SevkiyatRequestDto sevkiyatRequestDto) {
        log.warn("Yerel sevkiyat heniz uygulanmadi");
        return ErpOperationResult.notImplemented("Sevkiyat");
    }

    /**
     * TODO yerel barkod uretimi.
     *
     * <p>Yerel modda barkod {@code product} tablosundan okunabilir ya da stok koduna
     * gore uretilebilir.
     */
    @Override
    public Object generateBarcode(String token, String apiPath, String stokKod) {
        log.warn("Yerel barkod uretimi heniz uygulanmadi (stokKod={})", stokKod);
        return ErpOperationResult.notImplemented("Barkod uretimi");
    }

    /**
     * Yerel modda bildirilecek bir ERP yok; stok hareketi WMS tarafinda yapildigi icin
     * transfer basarili sayilir, ERP belge numarasi olusmaz.
     */
    @Override
    public ErpOperationResult interWarehouseTransfer(String token, String apiPath, DepolarArasiTransferErpDto dto) {
        return new ErpOperationResult(true, "Yerel mod: transfer ERP'ye gonderilmedi", null);
    }

    /**
     * TODO genis siparis detayi.
     *
     * <p>Kaynak {@code aur_erp_data}; {@link #getOrderDetail} ile ayni tablodan,
     * {@link OrderParamsDTO} icindeki filtrelere gore beslenecek.
     */
    @Override
    public Object getOrderComprehensiveDetails(String token, String apiPath, OrderParamsDTO orderParamsDTO) {
        log.warn("Yerel genis siparis detayi heniz uygulanmadi");
        return Collections.emptyList();
    }

    /** TODO yerel irsaliye sorgusu; kaynak zaten hep "DYS" olacagi icin bu ekranlarin yerel modda anlami sinirli. */
    @Override
    public List<AurWaybillDto> getWaybillList(String token, String apiPath, WaybillQueryRequestDto request) {
        return Collections.emptyList();
    }

    /** TODO yerel FMK siparis kalemi sorgusu. */
    @Override
    public List<AurCariOrderDetailListDto> getFirmStockOrderList(String token, String apiPath, FirmStockOrderListRequestDto request) {
        return Collections.emptyList();
    }

    private static String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private static double asDouble(Object value) {
        return value instanceof Number ? ((Number) value).doubleValue() : 0d;
    }
}
