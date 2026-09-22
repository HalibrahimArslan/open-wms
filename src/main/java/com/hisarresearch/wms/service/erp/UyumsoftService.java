package com.hisarresearch.wms.service.erp;

import org.springframework.context.annotation.Lazy;

import com.hisarresearch.wms.domain.AurLookupTable;
import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.domain.Customer;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.BatchRequestStatus;
import com.hisarresearch.wms.domain.enumeration.OrderStatus;
import com.hisarresearch.wms.service.*;
import com.hisarresearch.wms.service.dto.UyumsoftOrderTrackingListDTO;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.dto.base.ResponseDto;
import com.hisarresearch.wms.service.dto.uyumsoft.*;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.validation.InvalidOrderException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UyumsoftService {

    private final String urunKabulYap = "/urunKabulYap";
    private final String receivingForward = "/receiveOrders";
    private final String dispatchForward = "/dispatchOrders";
    private final String sevkiyatYap = "/sevkiyatYap";
    private final String siparisKapat = "/siparisKapat";
    private final String asyncSiparisKapat = "/asyncDispatchOrders";

    private final AurLogService aurLog;

    private final HttpService httpService;

    private final AurLookupService lookupService;

    private final CustomerService customerService;

    private final TranslationService translationService;

    private final RuleService ruleService;

    private final Environment environment;

    private final AurOrderMasterService aurOrderMasterService;

    private final AddressService addressService;

    private final WebSocketClientService webSocketClientService;

    private final AurOrderDetailService aurOrderDetailService;

    public UyumsoftService(AurLogService aurLog,
                           HttpService httpService, AurLookupService lookupService, Environment environment, CustomerService customerService,
                           TranslationService translationService, RuleService ruleService, @Lazy AurOrderMasterService aurOrderMasterService,
                           AddressService addressService, WebSocketClientService webSocketClientService, AurOrderDetailService aurOrderDetailService) {
        this.aurLog = aurLog;
        this.httpService = httpService;
        this.lookupService = lookupService;
        this.environment = environment;
        this.customerService = customerService;
        this.translationService = translationService;
        this.ruleService = ruleService;
        this.aurOrderMasterService = aurOrderMasterService;
        this.addressService = addressService;
        this.webSocketClientService = webSocketClientService;
        this.aurOrderDetailService = aurOrderDetailService;
    }

    public Object firmadanMalKabulUyumsoft(String token, String apiPath, UrunKabulIrsaliyeRequestDTO dto, String orderInfo) throws Exception {
        AurOrderMaster orderMaster = aurOrderMasterService.findByOrderInfo(orderInfo).orElseThrow(InvalidOrderException::new);
        AurDepoUrunAdres temporaryAddress = addressService.checkTemporaryAddress(String.valueOf(orderMaster.getDepoNo()));

        if (environment.acceptsProfiles("dev") && !getInvoiceStatus()) {
            completeIrsaliyeProcess(orderInfo, "Test-Kabul");
            aurOrderMasterService.sendProductToTemporaryAddress(orderMaster, temporaryAddress.getUrunAdresId());
            return "Mock Irsaliye oluşturuldu";
        }

        String endPoint = apiPath.concat(getReceivingPath());
        Object response = httpService.httpPost(token, endPoint, dto);
        completeIrsaliyeProcess(orderInfo, (String) response);
        aurOrderMasterService.sendProductToTemporaryAddress(orderMaster, temporaryAddress.getUrunAdresId());
        return response;

    }

    public Object sevkiyatProcessUyumsoft(String token, String apiPath, SevkiyatIrsaliyeRequestDTO dto, String orderInfo) throws Exception {
        fillErpUserCode(dto);

        if (environment.acceptsProfiles("dev") && !getInvoiceStatus()) {
            completeIrsaliyeProcess(orderInfo, "CIB2024000000651");
            return "CIB2024000000651";
        }

        String endPoint = apiPath.concat(getDispatcherPath());
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object response = httpService.httpPost(token, endPoint, dto);
        completeIrsaliyeProcess(orderInfo, (String) response);
        return response;

    }

    public String checkQueueStatus(String token, String apiPath, CheckQueueStatusDTO queueStatusDTO) {
        RequestDto dto = new RequestDto();
        String serviceName = "irsaliyeService.getQueueStatusByFilters";
        String status;
        QueueStatusSearchDto data = new QueueStatusSearchDto();
        data.setFirma_kod(queueStatusDTO.getFirmaKod());
        data.setBatch_request_id(queueStatusDTO.getBatchRequestId());
        data.setMsip_no(queueStatusDTO.getMsip_no());
        dto.setServiceName(serviceName);
        dto.setData(data);
        try {
            status = (String) httpService.executeService(token, apiPath, dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        aurOrderMasterService.updateOrderToStatus(queueStatusDTO.getOrderInfo(), getMatchedOrderStatus(status));
        return getMatchedOrderStatus(status);

    }

    public void completeOrder(String token, String apiPath, String orderInfo) throws Exception {
        CloseOrderDto dto = aurOrderMasterService.generateCloseOrderDto(orderInfo);
        String endpoint = apiPath.concat(siparisKapat);
        httpService.httpPost(token, endpoint, dto);
        aurOrderMasterService.updateOrderToStatus(orderInfo, "DONE");
    }

    public Object executeServiceWrapper(String token, String apiPath, RequestDto dto) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Object response = httpService.executeService(token, apiPath, dto);

        if (dto.getServiceName().equals("depoService.getOrderTrackingList")) {
            List<UyumsoftOrderTrackingListDTO> detailDtoList = mapper.convertValue(response, new TypeReference<>() {
            });
            List<String> dispatchAreaStatuses = new ArrayList<>();
            dispatchAreaStatuses.add("OUT_PROGRESS");
            dispatchAreaStatuses.add("DONE");
            detailDtoList.forEach(item -> {
                int totalItem = aurOrderDetailService.findByOrderNoAndStatus(item.getOrderMasterNo(), "IN_PROGRESS").size();
                List<AurOrderDetail> dispatchItemList = aurOrderDetailService.findByOrderNoAndStatusList(item.getOrderMasterNo(), dispatchAreaStatuses);
                if (totalItem > 0) {
                    item.setEnable(false);
                }
                if (!dispatchItemList.isEmpty()) {
                    item.setHasDone(true);
                }
            });

            return detailDtoList;
        }
        return response;
    }

    public String getMatchedOrderStatus(String status) {
        String responseStatus = "";
        if (status.equals(BatchRequestStatus.SUCCESS.toString())) {
            responseStatus = BatchRequestStatus.SUCCESS.getValue();
        }
        if (status.equals(BatchRequestStatus.FAILURE.toString())) {
            responseStatus = BatchRequestStatus.FAILURE.getValue();
        }
        if (status.equals(BatchRequestStatus.PENDING.toString())) {
            responseStatus = BatchRequestStatus.PENDING.getValue();
        }
        if (status.equals(BatchRequestStatus.RETRY.toString())) {
            responseStatus = BatchRequestStatus.RETRY.getValue();
        }
        if (status.equals(BatchRequestStatus.STARTED.toString())) {
            responseStatus = BatchRequestStatus.STARTED.getValue();
        }
        return responseStatus;
    }

    private String getDispatcherPath() {
        if (getQueueApiStatus()) {
            return sevkiyatYap;
        }
        return dispatchForward;
    }

    private String getReceivingPath() {
        if (getQueueApiStatus()) {
            return urunKabulYap;
        }
        return receivingForward;
    }

    private boolean getQueueApiStatus() {
        List<AurLookupTable> lookupList = lookupService.getByLookupName("UYUMSOFT_QUEUE_STATUS");
        if (lookupList.size() != 1) {
            return false;
        }
        return lookupList.get(0).getLookupDescription().equals("ACTIVE");
    }

    private void completeIrsaliyeProcess(String orderInfo, String erpResponse) {
        if (getQueueApiStatus()) {
            aurOrderMasterService.updateBatchRequestId(orderInfo, erpResponse);
        } else {
            aurOrderMasterService.updateBelgeNo(orderInfo, erpResponse, OrderStatus.DONE);
        }
    }

    private void fillErpUserCode(SevkiyatIrsaliyeRequestDTO dto) {
        List<Customer> desiredCustomer = customerService.getByCustomerCode(dto.getCariKod());
        dto.setErpUserCode("camapi");

        if (desiredCustomer.size() > 1) {
            throw new BadRequestAlertException(translationService.getErrorMessage("sevkiyatProcess.invalidCustomer"), "sevkiyatProcessUyumsoft", "sevkiyatProcess");
        }

        if (desiredCustomer.size() == 1) {
            dto.setProforma(true);
        }
        ruleService.executeRules(dto, "dispatcherVariant");

    }

    public boolean getInvoiceStatus() {
        String invoiceParam = "UYUMSOFT_INVOICE";
        List<AurLookupTable> invoiceStatus = lookupService.getByLookupName(invoiceParam);
        if (!invoiceStatus.isEmpty()) {
            return invoiceStatus.get(0).getLookupCode().equals("1");
        }
        return false;
    }

    public Object asyncDispatchProcess(String token, String apiPath, SevkiyatIrsaliyeRequestDTO dto, String orderInfo) throws Exception {
        fillErpUserCode(dto);

        if (environment.acceptsProfiles("dev") && !getInvoiceStatus()) {
            completeIrsaliyeProcess(orderInfo, "CIB2024000000651");
            return "CIB2024000000651";
        }

        String dispatcherPath = apiPath.concat(asyncSiparisKapat);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = mapper.writeValueAsString(dto);

        long logId = aurLog.logRequest("async-sevkiyatProcess-uyumsoft", dispatcherPath, requestBody);

        HttpPost httpPost = new HttpPost(dispatcherPath);

        httpPost.addHeader("Authorization", "Bearer " + token);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept-Encoding", "UTF-8");
        StringEntity entity = new StringEntity(requestBody, "UTF-8");
        httpPost.setEntity(entity);

        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Uyumsoft Async Cikis Irsaliye Servisinde Hata Alındı : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new Exception(errorMessage);
            }
            result = EntityUtils.toString(response.getEntity());
            aurLog.logResponse(logId, result);
        }

        JSONObject jsnObject = JSONObject.fromObject(result);
        ResponseDto responseDto = mapper.readValue(jsnObject.toString(), ResponseDto.class);
        if (responseDto.getSuccess().equals("true")) {
            aurOrderMasterService.updateBelgeNo(orderInfo, responseDto.getData().toString(), OrderStatus.OUT_PROGRESS);
            return responseDto.getData();
        } else {
            throw new Exception(responseDto.getMessage());
        }
    }

    public void fallbackDispatchProcess(ResponseDto responseDto) {
        String orderInfo = (String) JSONObject.fromObject(responseDto.getData()).get("orderInfo");
        if (responseDto.getSuccess().equals("true")) {
            aurOrderMasterService.updateOrderToStatus(orderInfo, OrderStatus.DONE.toString());
            String successMessage = "İrsaliye işlemi başarıyla gerçekleştirildi. Sipariş No: " + orderInfo;
            webSocketClientService.send(successMessage);
            return;
        }
        aurOrderMasterService.updateOrderToStatus(orderInfo, OrderStatus.IN_PROGRESS.toString());
        String errorMessage = "İrsaliye işlemi hatayla gerçekleştirildi. Sipariş No: " + orderInfo;
        webSocketClientService.send(errorMessage);

    }
}
