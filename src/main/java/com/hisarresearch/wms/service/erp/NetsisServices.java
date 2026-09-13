package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.AurLogService;
import com.hisarresearch.wms.service.dto.*;
import com.hisarresearch.wms.service.dto.netsis.AurNetsisFatUstInfoDto;
import com.hisarresearch.wms.service.dto.netsis.AurNetsisItemSlipsDto;
import com.hisarresearch.wms.service.dto.netsis.AurNetsisKalemDto;
import com.hisarresearch.wms.service.dto.netsis.AurSaveOrderDetailsRequestDto;
import com.hisarresearch.wms.service.dto.erp.ErpOperationResult;
import com.hisarresearch.wms.service.dto.mikro.StockDetailResponseDto;
import com.hisarresearch.wms.service.dto.mikro.v16.OrderParamsDTO;
import com.hisarresearch.wms.service.AurOrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import com.hisarresearch.wms.utility.AurHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class NetsisServices implements ErpOrderGateway {

    //@Value("${netsis.tokenPath}")
    private final String netsisTokenPath = "/token";

    //@Value("${netsis.queryPath}")
    private final String netsisQueryPath = "/Queries";

    //@Value("${netsis.slipPath}")
    private String netsisSlipPath = "/ItemSlips";

    //@Value("${netsis.dbName}")
    //private String dbName;


    private final AurLogService aurLog;

    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    public NetsisServices(AurLogService aurLog) {
        this.aurLog = aurLog;
    }

    @Override
    public java.util.Set<ErpConnectionType> erpTypes() {
        return java.util.Set.of(ErpConnectionType.NETSIS);
    }

    @Override
    public String getToken(String apiPath, String apiParameters) {
        String tokenResponse;
        String tokenPath = apiPath.concat(netsisTokenPath);

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        JSONArray api_params = JSONArray.fromObject("[" + apiParameters + "]");
        String dbName = api_params.getJSONObject(0).getString("dbName");

        MultiValueMap<String, String> tokenMap = new LinkedMultiValueMap<String, String>();

        tokenMap.add("grant_type", "password");
        tokenMap.add("branchcode", "2");
        tokenMap.add("password", "NET1");
        tokenMap.add("username", "netsis");
        //tokenMap.add("dbname", "ATOROSTOPTAN");
        tokenMap.add("dbname", dbName);
        tokenMap.add("dbuser", "TEMELSET");
        tokenMap.add("dbpassword", "");
        tokenMap.add("dbtype", "0");
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<MultiValueMap<String, String>>(tokenMap, tokenHeaders);
        RestTemplate restTemplate = new RestTemplate();

        tokenResponse = restTemplate.postForObject(tokenPath, tokenRequest, String.class);
        JSONArray jsonToken = JSONArray.fromObject("[" + tokenResponse + "]");
        String token = jsonToken.getJSONObject(0).getString("access_token");
        return token;
    }

    public String getQuery(String query, String token, String apiPath) throws Exception {
        String requestBody = "{\"TSql\":\"" + query + "\"}";

        String queryPath = apiPath.concat(netsisQueryPath);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            queryPath,
            HttpMethod.POST,
            entity,
            String.class
        );

        return response.getBody();
    }

    public List<WarehouseDTO> getDepoList(String token, String apiPath) throws Exception {
        String query = "SELECT * FROM TBLSTOKDP WITH (NOLOCK)";

        long logId = aurLog.logRequest("getDepoList", apiPath, query);

        String queryResult = getQuery(query, token, apiPath);
        aurLog.logResponse(logId, queryResult);

        JSONArray jsnResponse;
        jsnResponse = JSONArray.fromObject("[" + queryResult + "]");

        String result = "success";
        if (jsnResponse.getJSONObject(0).getString("IsSuccessful").equals("false")) {
            result = "Hata:" + jsnResponse.getJSONObject(0).getString("ErrorDesc");
        }

        String data = jsnResponse.getJSONObject(0).getString("Data");

        JSONArray jsnData = JSONArray.fromObject("[" + data + "]").getJSONArray(0);
        List<WarehouseDTO> depoDtoList = new ArrayList<>();
        for (int i = 0; i < jsnData.size(); i++) {
            JSONObject item = jsnData.getJSONObject(i);

            WarehouseDTO dto = new WarehouseDTO();
            dto.setCode(String.valueOf(item.getInt("DEPO_KODU")));
            dto.setName(item.getString("DEPO_ISMI"));
            depoDtoList.add(dto);
        }

        return depoDtoList;
    }

    public List<AurCariDto> getFirmList(String token, String apiPath) throws Exception {
        String FTIRSIP = "7";

        String query =
            "SELECT CARI_KOD, CARI_ISIM FROM TBLCASABIT AS CARI WITH(NOLOCK) WHERE EXISTS(SELECT 1 FROM TBLSIPAMAS AS SIP WITH (NOLOCK) WHERE SIP.CARI_KODU=CARI.CARI_KOD AND SIP.FTIRSIP=" +
            FTIRSIP +
            " AND SIP.KAPATILMIS='S')";

        long logId = aurLog.logRequest("getFirmList", apiPath, query);

        String queryResult = getQuery(query, token, apiPath);

        aurLog.logResponse(logId, queryResult);
        JSONArray jsnResponse;
        jsnResponse = JSONArray.fromObject("[" + queryResult + "]");

        String result = "success";

        if (jsnResponse.getJSONObject(0).getString("IsSuccessful").equals("false")) {
            result = "Hata:" + jsnResponse.getJSONObject(0).getString("ErrorDesc");
        }

        String data = jsnResponse.getJSONObject(0).getString("Data");

        System.out.println(data);

        JSONArray jsnData = JSONArray.fromObject("[" + data + "]").getJSONArray(0);
        List<AurCariDto> orderOkFirmDtoList = new ArrayList<AurCariDto>();
        for (int i = 0; i < jsnData.size(); i++) {
            JSONObject item = jsnData.getJSONObject(i);
            String cari_Kodu = item.getString("CARI_KOD");
            String cari_Isim = item.getString("CARI_ISIM");
            AurCariDto dto = new AurCariDto();
            dto.setCariKod(cari_Kodu);
            dto.setCariUnvan(cari_Isim);
            orderOkFirmDtoList.add(dto);
        }

        return orderOkFirmDtoList;
    }

    public List<AurCariDto> getSevkiyatList(String token, String apiPath) throws Exception {
        String FTIRSIP = "6";

        String query =
            "SELECT CARI_KOD, CARI_ISIM FROM TBLCASABIT AS CARI WITH(NOLOCK) WHERE EXISTS(SELECT 1 FROM TBLSIPAMAS AS SIP WITH (NOLOCK) WHERE SIP.CARI_KODU=CARI.CARI_KOD AND SIP.FTIRSIP=" +
            FTIRSIP +
            " AND SIP.KAPATILMIS='S')";

        long logId = aurLog.logRequest("getSevkiyatList", apiPath, query);

        String queryResult = getQuery(query, token, apiPath);

        aurLog.logResponse(logId, queryResult);
        JSONArray jsnResponse;
        jsnResponse = JSONArray.fromObject("[" + queryResult + "]");

        String result = "success";

        if (jsnResponse.getJSONObject(0).getString("IsSuccessful").equals("false")) {
            result = "Hata:" + jsnResponse.getJSONObject(0).getString("ErrorDesc");
        }

        String data = jsnResponse.getJSONObject(0).getString("Data");
        JSONArray jsnData = JSONArray.fromObject("[" + data + "]").getJSONArray(0);
        List<AurCariDto> orderOkFirmDtoList = new ArrayList<AurCariDto>();
        for (int i = 0; i < jsnData.size(); i++) {
            JSONObject item = jsnData.getJSONObject(i);
            String cari_Kodu = item.getString("CARI_KOD");
            String cari_Isim = item.getString("CARI_ISIM");
            AurCariDto dto = new AurCariDto();
            dto.setCariKod(cari_Kodu);
            dto.setCariUnvan(cari_Isim);
            orderOkFirmDtoList.add(dto);
        }

        return orderOkFirmDtoList;
    }

    public List<AurCariOrderDto> getOrderListByCariKod(String cariKodu, String token, String apiPath) throws Exception {
        String FTIRSIP = "7";
        String query =
            "SELECT FATIRS_NO, TARIH FROM TBLSIPAMAS AS SIP WITH (NOLOCK) WHERE SIP.CARI_KODU='" +
            cariKodu +
            "' AND SIP.FTIRSIP = " +
            FTIRSIP +
            " AND SIP.KAPATILMIS ='S'";

        long logId = aurLog.logRequest("getOrderListByCariKod", apiPath, query);

        String queryResult = getQuery(query, token, apiPath);
        aurLog.logResponse(logId, queryResult);

        JSONArray jsnResponse;
        jsnResponse = JSONArray.fromObject("[" + queryResult + "]");

        String result = "success";

        if (jsnResponse.getJSONObject(0).getString("IsSuccessful").equals("false")) {
            result = "Hata:" + jsnResponse.getJSONObject(0).getString("ErrorDesc");
        }

        String data = jsnResponse.getJSONObject(0).getString("Data");

        JSONArray jsnData = JSONArray.fromObject("[" + data + "]").getJSONArray(0);
        List<AurCariOrderDto> cariOrderDtoList = new ArrayList<AurCariOrderDto>();
        for (int i = 0; i < jsnData.size(); i++) {
            JSONObject item = jsnData.getJSONObject(i);

            AurCariOrderDto dto = new AurCariOrderDto();
            dto.setOrderNo(item.getString("FATIRS_NO"));
            dto.setOrderDate(item.getString("TARIH"));
            //dto.setOrderLineItemCount(0);
            cariOrderDtoList.add(dto);
        }

        return cariOrderDtoList;
    }

    public List<AurCariOrderDetailDto> getOrderDetailById(String id, String token, String apiPath) throws Exception {
        String query =
            "SELECT SIP.STOK_KODU, STOK.STOK_ADI, COALESCE(STOK.BARKOD1, STOK.BARKOD2, STOK.BARKOD3) AS BARKOD, SIP.STHAR_GCMIK AS MIKTAR FROM TBLSIPATRA AS SIP WITH (NOLOCK) INNER JOIN TBLSTSABIT AS STOK WITH (NOLOCK) ON SIP.STOK_KODU = STOK.STOK_KODU WHERE SIP.FISNO = '" +
            id +
            "'";

        long logId = aurLog.logRequest("getOrderDetailById", apiPath, query);

        String queryResult = getQuery(query, token, apiPath);

        aurLog.logResponse(logId, queryResult);

        JSONArray jsnResponse;
        jsnResponse = JSONArray.fromObject("[" + queryResult + "]");

        String result = "success";

        if (jsnResponse.getJSONObject(0).getString("IsSuccessful").equals("false")) {
            result = "Hata:" + jsnResponse.getJSONObject(0).getString("ErrorDesc");
        }

        String data = jsnResponse.getJSONObject(0).getString("Data");

        System.out.println(data);

        JSONArray jsnData = JSONArray.fromObject("[" + data + "]").getJSONArray(0);
        List<AurCariOrderDetailDto> cariOrderDetailDtoList = new ArrayList<AurCariOrderDetailDto>();
        for (int i = 0; i < jsnData.size(); i++) {
            JSONObject item = jsnData.getJSONObject(i);

            AurCariOrderDetailDto dto = new AurCariOrderDetailDto();
            dto.setStokKodu(item.getString("STOK_KODU"));
            dto.setStokAdi(item.getString("STOK_ADI"));
            dto.setBarkod(item.getString("BARKOD"));
            dto.setTeslimMiktar(item.getDouble("MIKTAR"));
            cariOrderDetailDtoList.add(dto);
        }

        return cariOrderDetailDtoList;
    }

    public String saveOrderDetails(AurSaveOrderDetailsRequestDto aurSaveOrderDetailsRequestDto, String token, String apiPath)
        throws Exception {
        AurNetsisItemSlipsDto mainDto = new AurNetsisItemSlipsDto();
        AurNetsisFatUstInfoDto fuDto = new AurNetsisFatUstInfoDto();
        List<AurNetsisKalemDto> kalemDtoList = new ArrayList<AurNetsisKalemDto>();

        //Token Alma
        //String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        String formattedDate = AurHelper.getDate();
        String irsaliyeNo = aurSaveOrderDetailsRequestDto.getFatirsNumber();
        String cariKod = aurSaveOrderDetailsRequestDto.getFirmCode();

        fuDto.setSube_Kodu(Integer.parseInt("-1"));
        fuDto.setCariKod(cariKod);
        fuDto.setFATIRS_NO(irsaliyeNo);
        fuDto.setTarih(formattedDate);
        fuDto.setTip(3);
        fuDto.setTIPI(2);
        fuDto.setProje_Kodu("FR");
        fuDto.setFiiliTarih(formattedDate);
        fuDto.setODEMETARIHI(formattedDate);
        fuDto.setENTEGRE_TRH(formattedDate);
        fuDto.setSIPARIS_TEST(formattedDate);
        fuDto.setFIYATTARIHI(formattedDate);
        fuDto.setKOSULTARIHI(formattedDate);
        fuDto.setKDV_DAHILMI(true);
        fuDto.setPLA_KODU("12");

        mainDto.setFatUst(fuDto);

        mainDto.setTransactSupport(false);
        mainDto.setMuhasebelesmisBelge(false);
        mainDto.setKalemAdedi(aurSaveOrderDetailsRequestDto.getOrderDetailList().size());
        mainDto.setFaturaTip(7);
        mainDto.setSonNumaraYazilsin(false);
        mainDto.setOtoIskontoGetir(false);
        mainDto.setKosulluHesapla(false);
        mainDto.setInternalObjectAddress(218197952);
        mainDto.setSeriliHesapla(false);
        mainDto.setFiyatSistemineGoreHesapla(false);
        mainDto.setStokKartinaGoreHesapla(false);
        mainDto.setOtoVadeGunGetir(true);
        mainDto.setOtomatikIslemTipiGetir(false);
        mainDto.setOtomatikOdemeKoduGetir(false);
        mainDto.setMaliyetTipineGoreHesapla(false);
        mainDto.setOtomatikCevrimYapilsin(false);
        mainDto.setKayitliNumaraOtomatikGuncellensin(false);
        mainDto.setSiralama("\\u0000");
        mainDto.setEPostaGonderilsin(false);
        mainDto.setOtoNakliyeKatSayisiGetir(false);
        mainDto.setOtoBolgeFarkIskGetir(false);
        mainDto.setRiskKontrol(false);
        mainDto.setTahsilatKalemAdedi(0);
        mainDto.setTahsilatKayitKullan(false);
        mainDto.setAcikBelgeTahsilat(false);
        mainDto.setBaglantiKontrol(true);
        int siraNo = 1;
        for (AurCariOrderDetailDto orderDetail : aurSaveOrderDetailsRequestDto.getOrderDetailList()) {
            AurNetsisKalemDto kalemDto = new AurNetsisKalemDto();
            kalemDto.setDovizAdi("TL");
            kalemDto.setSTra_DovizAdi("TL");
            kalemDto.setKalemSeri(null);
            kalemDto.setStokKodu(orderDetail.getStokKodu());
            kalemDto.setSira(siraNo);
            kalemDto.setSTra_FATIRSNO(irsaliyeNo);
            kalemDto.setSTra_GCMIK(orderDetail.getTeslimMiktar());
            kalemDto.setSTra_TAR(formattedDate);
            /*
             kalemDto.setSTra_NF(Double.parseDouble("50.0".replace(",",".")));
             kalemDto.setSTra_BF(Double.parseDouble("50.0".replace(",",".")));
             kalemDto.setSTra_KDV(Double.parseDouble("18.0".replace(",",".")));*/

            kalemDto.setSTra_SatIsk(Double.parseDouble("0.0".replace(",", ".")));

            kalemDto.setProjeKodu("FR");
            kalemDto.setDEPO_KODU(aurSaveOrderDetailsRequestDto.getDepoCode());
            kalemDto.setStra_FiiliTar(formattedDate);
            kalemDto.setReferansKodu("0");
            kalemDtoList.add(kalemDto);
            siraNo++;
        }

        mainDto.setKalems(kalemDtoList);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String request = mapper.writeValueAsString(mainDto);
        String slipPath = apiPath.concat(netsisSlipPath);

        long logId = aurLog.logRequest("saveOrderDetails", slipPath, request);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<AurNetsisItemSlipsDto> aktarimRequest = new HttpEntity<AurNetsisItemSlipsDto>(mainDto, headers);
        String aktarimResponse = restTemplate.postForObject(slipPath, aktarimRequest, String.class);

        aurLog.logResponse(logId, aktarimResponse);

        System.out.println("-----------------Response-----------------------------------");
        System.out.println(aktarimResponse);
        String result = "success";
        JSONArray jsnAktarimResponse = JSONArray.fromObject("[" + aktarimResponse + "]");

        if (jsnAktarimResponse.getJSONObject(0).getString("IsSuccessful").equals("false")) {
            result = "Hata:" + jsnAktarimResponse.getJSONObject(0).getString("ErrorDesc");
        }
        return result;
    }

    // =====================================================================
    // ErpOrderGateway koprusu.
    //
    // Netsis'in kendi metotlari zaten kanonik DTO'lari donduruyor; asagidakiler
    // yalnizca ortak imzayi karsilar. Boylece Netsis de digerleri gibi
    // ErpGatewayRouter uzerinden secilir ve resource katmaninda Netsis'e ozel
    // dallanma kalmaz.
    // =====================================================================

    @Override
    public List<AurCariDto> getFirmList(String token, String apiPath, int depoNo, int sipTip) throws Exception {
        // Netsis sorgusu depo/siparis tipine gore daraltilmiyor.
        return getFirmList(token, apiPath);
    }

    @Override
    public List<AurCariOrderDto> getCariOrderList(String token, String apiPath, AurFirmListDto aurFirmListDto) throws Exception {
        return getOrderListByCariKod(aurFirmListDto.getFirmCode(), token, apiPath);
    }

    @Override
    public List<AurCariOrderDetailListDto> getCariOrderDetailList(String token, String apiPath, AurFirmListDto aurFirmListDto) throws Exception {
        List<AurCariOrderDto> orders = getOrderListByCariKod(aurFirmListDto.getFirmCode(), token, apiPath);
        List<AurCariOrderDetailListDto> flat = new ArrayList<>();
        int index = 0;
        for (AurCariOrderDto order : orders) {
            List<AurCariOrderDetailDto> details = order.getOrderDetail() == null
                ? Collections.emptyList() : order.getOrderDetail();
            for (AurCariOrderDetailDto detail : details) {
                if (detail.getTeslimMiktar() >= detail.getSiparisMiktar()) {
                    continue;
                }
                AurCariOrderDetailListDto dto = new AurCariOrderDetailListDto();
                dto.setId(index++);
                dto.setOrderNo(order.getOrderNo());
                dto.setOrderDate(order.getOrderDate());
                dto.setOrderLineItemCount(details.size());
                dto.setBarkod(detail.getBarkod());
                dto.setStokKodu(detail.getStokKodu());
                dto.setStokAdi(detail.getStokAdi());
                dto.setStokBirimi(detail.getStokBirimi());
                dto.setSiparisMiktar(detail.getSiparisMiktar());
                dto.setTeslimMiktar(detail.getTeslimMiktar());
                dto.setSipUid(detail.getSipUid());
                dto.setDurum("0");
                flat.add(dto);
            }
        }
        return aurOrderMasterService.enrichDepoOrderDetails(flat);
    }

    @Override
    public List<AurCariOrderDetailDto> getOrderDetail(String token, String apiPath, String orderNo,
                                                      Integer sipTip, Integer depoNo) throws Exception {
        return getOrderDetailById(orderNo, token, apiPath);
    }

    @Override
    public Object getDepoList(String token, String apiPath, String companyCode) throws Exception {
        return getDepoList(token, apiPath);
    }

    // --- Netsis adaptorunde heniz karsiligi olmayan islemler ---------------

    @Override
    public Map<String, StockDetailResponseDto> getStockDetails(String token, String apiPath,
                                                               List<String> barcodes, Integer depoNo) {
        return Collections.emptyMap(); // TODO Netsis stok detay sorgusu
    }

    @Override
    public Object getOrderComprehensiveDetails(String token, String apiPath, OrderParamsDTO orderParamsDTO) {
        return Collections.emptyList(); // TODO Netsis genis siparis detayi
    }

    @Override
    public Object receiveOrder(String token, String apiPath, MalKabulRequestDto malKabulRequestDto, Long addressId) {
        return ErpOperationResult.notImplemented("Netsis mal kabul");
    }

    @Override
    public Object dispatchOrder(String token, String apiPath, SevkiyatRequestDto sevkiyatRequestDto) {
        return ErpOperationResult.notImplemented("Netsis sevkiyat");
    }

    @Override
    public Object generateBarcode(String token, String apiPath, String stokKod) {
        return ErpOperationResult.notImplemented("Netsis barkod uretimi");
    }
}
