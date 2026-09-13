package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.domain.enumeration.MicroWaybillTypeV15;
import com.hisarresearch.wms.repository.AurOrderMasterRepository;
import com.hisarresearch.wms.service.*;
import com.hisarresearch.wms.service.dto.*;
import com.hisarresearch.wms.service.dto.barcode.PackageCodeList;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.dto.base.ResponseDto;
import com.hisarresearch.wms.service.dto.event.AurReserveEvent;
import com.hisarresearch.wms.service.dto.event.OrderMailEvent;
import com.hisarresearch.wms.service.dto.mikro.StockDetailResponseDto;
import com.hisarresearch.wms.service.dto.mikro.v15.MikroV15QueryDTO;
import com.hisarresearch.wms.service.dto.mikro.v16.OrderParamsDTO;
import com.hisarresearch.wms.service.dto.sms.ShippingSmsDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MikroServices implements ErpOrderGateway {

    private final String cariOrderListPath = "/firmOrderList";

    @Autowired
    private AurLogService aurLog;

    @Autowired
    private UserService userService;

    @Autowired
    private AurOrderMasterRepository aurOrderMasterRepository;

    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpService httpService;

    @Autowired
    private AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Autowired
    private ReceivingAddressService receivingAddressService;

    @Autowired
    private ErpTokenService erpTokenService;

    @Autowired
    private ApplicationEventPublisher publisher;


    @Override
    public java.util.Set<ErpConnectionType> erpTypes() {
        // Ayni servis hem v16 hem v15 icin kullaniliyor; ayrim metotlarin icinde yapiliyor.
        return java.util.Set.of(ErpConnectionType.MIKRO_V16, ErpConnectionType.MIKRO_V15);
    }

    @Override
    public String getToken(String apiPath, String apiParameters) throws Exception {
        return erpTokenService.getToken(apiPath, apiParameters);
    }

    @Override
    public Object getDepoList(String token, String apiPath, String companyCode) throws Exception {
        // Mikro'da depo listesi ERP'den gelir; companyCode yalnizca yerel adaptorde anlamlidir.
        return getDepoList(token, apiPath);
    }

    public Object getDepoList(String token, String apiPath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        JSONObject apiParameters = JSONObject.fromObject(aurCompanyDto.getApiParameters());
        JSONArray jsonArray = apiParameters.getJSONArray("depoNo");
        List<Integer> depoNoList = Arrays.asList(mapper.readValue(jsonArray.toString(), Integer[].class));

        RequestDto dto = new RequestDto();
        dto.setData(depoNoList);
        dto.setServiceName("depoService.getDepoList");

        return httpService.executeService(token, apiPath, dto);
    }

    @Override
    @Cacheable(cacheNames = "microFirmList", key = "{#depoNo,#sipTip}")
    public List<AurCariDto> getFirmList(String token, String apiPath, int depoNo, int sipTip) throws Exception {
        String endPoint = apiPath.concat("/firmListOrderExists/")
            .concat(String.valueOf(depoNo))
            .concat("/")
            .concat(String.valueOf(sipTip));

        Object response = httpService.httpGet(token, endPoint, null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, new TypeReference<>() {});
    }

    @Override
    public List<AurCariOrderDto> getCariOrderList(String token, String apiPath, AurFirmListDto aurFirmListDto)
        throws Exception {
        String endPoint = apiPath.concat(cariOrderListPath);

        ObjectMapper mapper = new ObjectMapper();
        Object response = httpService.httpPost(token, endPoint, aurFirmListDto);
        List<AurCariOrderDto> aurCariOrderDtoList = mapper.convertValue(response, new TypeReference<>() {
        });

        for (AurCariOrderDto dto : aurCariOrderDtoList) {
            List<AurCariOrderDetailDto> cariDto = dto.getOrderDetail().stream().filter(x -> x.getDurum().equals("0") && x.getTeslimMiktar() < x.getSiparisMiktar()).collect(Collectors.toList());
            dto.setOrderDetail(cariDto);
        }
        return aurCariOrderDtoList;
    }

    @Override
    public List<AurCariOrderDetailListDto> getCariOrderDetailList(String token, String apiPath, AurFirmListDto aurFirmListDto)
        throws Exception {
        String endPoint = apiPath.concat(cariOrderListPath);
        Object response = httpService.httpPost(token, endPoint, aurFirmListDto);
        return aurOrderMasterService.getFilteredDepoOrderDetails(response);

    }

    @Override
    public List<AurCariOrderDetailDto> getOrderDetail(String token, String apiPath, String orderNo, Integer sipTip, Integer depoNo) throws Exception {
        String endPoint = apiPath.concat("/orderDetail/").concat(orderNo).concat("/").concat(sipTip.toString()).concat("/").concat(String.valueOf(depoNo));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Object response = httpService.httpGet(token, endPoint, null);
        return mapper.convertValue(response, new TypeReference<>() {});
    }

    @Override
    public Map<String, StockDetailResponseDto> getStockDetails(String token, String apiPath,
                                                               List<String> barcodes, Integer depoNo) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("barkodList", barcodes);
        data.put("depoNo", String.valueOf(depoNo));

        RequestDto requestDto = new RequestDto();
        requestDto.setServiceName("stokService.stokDetaySorgula");
        requestDto.setData(data);

        Object response = httpService.executeService(token, apiPath, requestDto);

        ObjectMapper mapper = new ObjectMapper();
        List<StockDetailResponseDto> stockDetailList = mapper.convertValue(response, new TypeReference<>() {});
        return stockDetailList.stream()
            .collect(Collectors.toMap(StockDetailResponseDto::getBarkod, stockDetail -> stockDetail, (a, b) -> a));
    }

    @Override
    public Object receiveOrder(String token, String apiPath, MalKabulRequestDto malKabulRequestDto, Long addressId) throws Exception {
        String receivingPath = apiPath.concat("/malKabulYap");
        receivingAddressService.completeReceivingAddressOperation(malKabulRequestDto, addressId);
        aurOrderMasterService.completeReceiving(malKabulRequestDto);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = mapper.writeValueAsString(malKabulRequestDto);

        long logId = aurLog.logRequest("firmadanMalKabul", receivingPath, requestBody);

        HttpPost httpPost = httpService.generateHttpPost(token, receivingPath, requestBody);
        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Giris Irsaliye Servisinde Hata Meydana Geldi : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new RuntimeException(errorMessage);
            }
            result = EntityUtils.toString(response.getEntity());
            aurLog.logResponse(logId, result);
        }

        JSONObject jsnObject = JSONObject.fromObject(result);

        ResponseDto responseDto = mapper.readValue(jsnObject.toString(), ResponseDto.class);
        if (responseDto.getSuccess().equals("true")) {
            pushReserveList(malKabulRequestDto.getOrderDetailList());
            publisher.publishEvent(new OrderMailEvent(malKabulRequestDto.getOrderId()));
            return responseDto.getData();
        } else {
            throw new RuntimeException(responseDto.getMessage());
        }
    }

    @Override
    public Object dispatchOrder(String token, String apiPath, SevkiyatRequestDto sevkiyatRequestDto) throws Exception {
        // Mikro tarafinda token/apiPath metodun kendi icinde cozuluyor.
        return dispatchOrder(sevkiyatRequestDto);
    }

    public Object dispatchOrder(SevkiyatRequestDto sevkiyatRequestDto) throws Exception {
        AurCompanyDTO aurCompanyDto = userService.checkErpType(ErpConnectionType.MIKRO_V16);
        String apiPath = aurCompanyDto.getApiEndPoint();
        String token = getToken(apiPath, aurCompanyDto.getApiParameters());
        String dispatcherPath = apiPath.concat("/sevkiyatYap");
        aurDepoUrunAdresStokService.deleteProductsFromControlAreaByOrder(sevkiyatRequestDto.getOrderId());
        aurOrderMasterService.completeDispatcher(sevkiyatRequestDto);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = mapper.writeValueAsString(sevkiyatRequestDto);

        long logId = aurLog.logRequest("sevkiyatProcess", dispatcherPath, requestBody);
        HttpPost httpPost = httpService.generateHttpPost(token, dispatcherPath, requestBody);

        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Cikis Irsaliye Servisinde Hata Alındı : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new RuntimeException(errorMessage);
            }
            result = EntityUtils.toString(response.getEntity());
            aurLog.logResponse(logId, result);
        }

        JSONObject jsnObject = JSONObject.fromObject(result);

        ResponseDto responseDto = mapper.readValue(jsnObject.toString(), ResponseDto.class);

        if (responseDto.getSuccess().equals("true")) {
            Optional<AurOrderMaster> aom = aurOrderMasterRepository.findByOrderInfo(sevkiyatRequestDto.getOrderNo());
            if (aom.isPresent()) {
                String belgeNo = responseDto.getData().toString();
                aom.get().setBelgeNo(belgeNo);
            }
            publisher.publishEvent(new OrderMailEvent(sevkiyatRequestDto.getOrderId()));
            return responseDto.getData();
        } else {
            throw new RuntimeException(responseDto.getMessage());
        }
    }

    public Object depolarArasiTransfer(String token, String apiPath, DepolarArasiTransferErpDto depolarArasiTransferDto, String orderInfo, String orderNo, String operationType) throws Exception {
        String depolarArasiEndpoint = apiPath.concat("/depolarArasiTransferYap");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = mapper.writeValueAsString(depolarArasiTransferDto);

        long logId = aurLog.logRequest("depolarArasiTransfer", depolarArasiEndpoint, requestBody);

        HttpPost httpPost = httpService.generateHttpPost(token, depolarArasiEndpoint, requestBody);
        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Depolar Arası Transfer Servisinde Hata Meydana Geldi : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new RuntimeException(errorMessage);
            }
            result = EntityUtils.toString(response.getEntity());
            aurLog.logResponse(logId, result);
        }

        JSONObject jsnObject = JSONObject.fromObject(result);

        ResponseDto responseDto = mapper.readValue(jsnObject.toString(), ResponseDto.class);

        if (responseDto.getSuccess().equals("true")) {
            int userErpCode = userService.getUserCompanyCode();
            if (ErpConnectionType.MIKRO_V15.getErpCode() == userErpCode) {
                aurOrderMasterService.completeOrder(orderInfo, orderNo);
                orderService.changeStatus(orderNo, depolarArasiTransferDto.getGirDepoNo(), depolarArasiTransferDto.getCikDepoNo(), operationType, (String) responseDto.getData());
            }
            return responseDto.getData();
        } else {
            throw new RuntimeException(responseDto.getMessage());
        }
    }

    @Override
    public Object generateBarcode(String token, String apiPath, String stokKod) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String barcodePath = apiPath.concat("/produceBarkod/").concat(stokKod);

        long logId = aurLog.logRequest("generateBarcode", barcodePath, "GET REQUEST");

        HttpGet httpGet = new HttpGet(barcodePath);

        httpGet.addHeader("Authorization", "Bearer " + token);
        httpGet.addHeader("Content-Type", "application/json");

        String result;


        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Barkod Oluşturma Servisinde Hata Meydana Geldi  : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new RuntimeException(errorMessage);
            }
            result = EntityUtils.toString(response.getEntity());
            aurLog.logResponse(logId, result);
        }

        JSONObject jsnObject = JSONObject.fromObject(result);

        ResponseDto responseDto = mapper.readValue(jsnObject.toString(), ResponseDto.class);

        if (responseDto.getSuccess().equals("true")) {

            return responseDto.getData();
        } else {
            throw new RuntimeException(responseDto.getMessage());
        }
    }

    public MicroSipParameterDetailDTO getOrderParameterDetail(String token, String apiPath, String sipUid) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String barcodePath = apiPath.concat("/sipParameterDetail/").concat(sipUid);

        long logId = aurLog.logRequest("generateBarcode", barcodePath, "GET REQUEST");

        HttpGet httpGet = new HttpGet(barcodePath);
        httpGet.addHeader("Authorization", "Bearer " + token);
        httpGet.addHeader("Content-Type", "application/json");

        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Sipariş detayları getirme servisinde hata  : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new RuntimeException(errorMessage);
            }
            result = EntityUtils.toString(response.getEntity());
            aurLog.logResponse(logId, result);
        }

        JSONObject jsnObject = JSONObject.fromObject(result);
        ResponseDto responseDto = mapper.readValue(jsnObject.toString(), ResponseDto.class);
        if (responseDto.getSuccess().equals("true")) {
            return mapper.convertValue(responseDto.getData(), new TypeReference<>() {
            });
        } else {
            throw new RuntimeException(responseDto.getMessage());
        }
    }

    public Object depolarArasiUrunKabul(String token, String apiPath, DepolarArasiTransferErpDto depolarArasiTransferDto, String orderInfo, String orderNo, String operationType) throws Exception {
        String depolarArasiEndpoint = apiPath.concat("/depolarArasiSevkKabulYap");
        int girDepoNo = depolarArasiTransferDto.getGirDepoNo();
        int cikDepoNo = depolarArasiTransferDto.getCikDepoNo();
        depolarArasiTransferDto.setGirDepoNo(cikDepoNo);
        depolarArasiTransferDto.setCikDepoNo(girDepoNo);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = mapper.writeValueAsString(depolarArasiTransferDto);

        long logId = aurLog.logRequest("depolarArasiUrunKabul", depolarArasiEndpoint, requestBody);
        HttpPost httpPost = httpService.generateHttpPost(token, depolarArasiEndpoint, requestBody);
        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Depolar Arası Urun Kabul Servisinde Hata Meydana Geldi : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new RuntimeException(errorMessage);
            }
            result = EntityUtils.toString(response.getEntity());
            aurLog.logResponse(logId, result);
        }

        JSONObject jsnObject = JSONObject.fromObject(result);
        ResponseDto responseDto = mapper.readValue(jsnObject.toString(), ResponseDto.class);
        if (responseDto.getSuccess().equals("true")) {
            int userErpCode = userService.getUserCompanyCode();
            if (ErpConnectionType.MIKRO_V15.getErpCode() == userErpCode) {
                aurOrderMasterService.completeOrder(orderInfo, orderNo);
                orderService.changeStatus(orderNo, depolarArasiTransferDto.getCikDepoNo(), depolarArasiTransferDto.getGirDepoNo(), operationType, orderNo);
            }
            return responseDto.getData();
        } else {
            throw new RuntimeException(responseDto.getMessage());
        }
    }

    public Object mikroV15Query(String token, String apiPath, MikroV15QueryDTO queryParams) throws Exception {
        String queryPath = apiPath.concat("/query");
        return httpService.httpGet(token, queryPath, queryParams);
    }

    public Object countOfMikroV15Query(String token, String apiPath, MikroV15QueryDTO queryParams) throws Exception {
        String queryPath = apiPath.concat("/query/count");
        return httpService.httpGet(token, queryPath, queryParams);
    }

    public LinkedHashMap<String, String> getPartialItem(String token, String apiPath, String stockCode) throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setServiceName("stokService.packageList");

        Object microPackageList = httpService.executeService(token, apiPath, requestDto);
        List<LinkedHashMap<String, String>> packageList = (List<LinkedHashMap<String, String>>) microPackageList;
        Optional<LinkedHashMap<String, String>> partialItem = packageList.stream().filter(item -> item.get("packageCode").equals(stockCode)).findAny();
        if (partialItem.isPresent()) {
            return partialItem.get();
        }
        throw new RuntimeException(stockCode + "stok kodu için tanım bulunamadı.");
    }


    public List<AurPartialDetails> getPartialItemDetails(AurPartialItem partialItem, String token, String apiPath) throws Exception {
        RequestDto requestDto = new RequestDto();
        PackageCodeList packageCodeList = new PackageCodeList();
        List<String> packageList = new ArrayList<>();
        List<AurPartialDetails> partialDetailList = new ArrayList<>();

        packageList.add(partialItem.getPackageCode());
        packageCodeList.setPackageCode(packageList);
        requestDto.setData(packageCodeList);
        requestDto.setServiceName("stokService.packageDetail");


        Object microPackageDetail = httpService.executeService(token, apiPath, requestDto);
        List<LinkedHashMap<String, Object>> packageDetailList = (List<LinkedHashMap<String, Object>>) microPackageDetail;

        packageDetailList.forEach(detail -> {
            AurPartialDetails aurPartialDetails = new AurPartialDetails();
            aurPartialDetails.setStockCode((String) detail.get("stockCode"));
            aurPartialDetails.setQuantity((double) (int) detail.get("quantity"));
            aurPartialDetails.setBarcode((String) detail.get("barcode"));
            aurPartialDetails.setStockName((String) detail.get("stockName"));
            aurPartialDetails.setAurPartialItem(partialItem);

            partialDetailList.add(aurPartialDetails);
        });

        return partialDetailList;

    }

    public ResponseDto getCustomerMailInfo(String token, String apiPath, String orderNo) {
        ResponseDto responseDto = new ResponseDto();
        try {
            RequestDto requestDto = new RequestDto();
            requestDto.setServiceName("siparislerService.getOrderMailInfo");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderNo", orderNo);
            requestDto.setData(jsonObject);
            Object mailAddress = httpService.executeService(token, apiPath, requestDto);
            responseDto.setData(mailAddress);
            responseDto.setSuccess("true");
            return responseDto;

        } catch (Exception e) {
            responseDto.setSuccess("false");
            return responseDto;
        }
    }

    @Override
    public Object getOrderComprehensiveDetails(String token, String apiPath, OrderParamsDTO orderParamsDTO) throws Exception {
        return httpService.httpPost(token, apiPath + "/searchOrderList", orderParamsDTO);
    }

    public Object receivingWithoutOrder(String token, String apiPath, MalKabulRequestDto dto) throws Exception {
        return httpService.httpPost(token, apiPath + "/siparisSizGiris", dto);
    }

    public Object returnWaybill(String token, String apiPath, MalKabulRequestDto dto) throws Exception {
        return httpService.httpPost(token, apiPath + "/iadeIrsaliyesi", dto);
    }

    public Object sarfWaybill(String token, String apiPath, MalKabulRequestDto dto) throws Exception {
        String endPoint = apiPath + "/sarf" +
            "?type=" +
            MicroWaybillTypeV15.SARF;
        return httpService.httpPost(token, endPoint, dto);
    }

    public void getCustomerSmsInfo(String token, String apiPath, ShippingSmsDTO shippingSmsDTO) {
        ResponseDto responseDto = new ResponseDto();
        try {
            Object sendShippingSms = httpService.httpPost(token, apiPath + "/sendShippingSms", shippingSmsDTO);
            responseDto.setData(sendShippingSms);
            responseDto.setSuccess("true");

        } catch (Exception e) {
            responseDto.setSuccess("false");
        }
    }

    public Object sendSiparisNoList(List<String> siparisNoList) throws Exception {
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        int erpCode = Integer.parseInt(aurCompanyDto.getErpTipi());
        String token = "";

        if (ErpConnectionType.MIKRO_V16.getErpCode() == erpCode) {
            token = getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        }
        String endpoint = aurCompanyDto.getApiEndPoint() + "/inquery-order-adres";

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("siparisNoList", siparisNoList);

        String requestBody = mapper.writeValueAsString(requestBodyMap);
        long logId = aurLog.logRequest("sendSiparisNoList", endpoint, requestBody);

        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.addHeader("Authorization", "Bearer " + token);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));

        String result;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "POST an " + endpoint + " HATA cıktı: " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new Exception(errorMessage);
            }

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            aurLog.logResponse(logId, result);
        }

        JSONObject jsnObject = JSONObject.fromObject(result);
        ResponseDto responseDto = new ObjectMapper().readValue(jsnObject.toString(), ResponseDto.class);

        if ("true".equals(responseDto.getSuccess())) {
            return responseDto.getData();
        } else {
            throw new RuntimeException(responseDto.getMessage());
        }
    }



    public void pushReserveList(List<OrderLineItemDto> orderDetailList){
        String companyCode = userService.getUserCompanyCode().toString();
        List<AurReserveDTO> aurReserveDTOList = new ArrayList<>();
        orderDetailList.forEach(orderDetail -> {
            if (orderDetail.getIsReserve() == null) {
                return;
            }
            if (Objects.equals(orderDetail.getIsReserve(), "H")) {
                return;
            }
            AurReserveDTO aurReserveDTO = new AurReserveDTO();
            aurReserveDTO.setBarcode(orderDetail.getBarkod());
            aurReserveDTO.setStatus(AurReserveStatus.RESERVED);
            aurReserveDTO.setOrderNo(orderDetail.getReserveNo());
            aurReserveDTO.setCompanyCode(companyCode);
            aurReserveDTO.setDescription(orderDetail.getDescription());
            aurReserveDTOList.add(aurReserveDTO);
        });

        publisher.publishEvent(new AurReserveEvent(aurReserveDTOList));
    }

}
