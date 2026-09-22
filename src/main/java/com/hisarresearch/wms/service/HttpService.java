package com.hisarresearch.wms.service;

import com.hisarresearch.wms.service.dto.mikro.v15.MikroV15QueryDTO;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.dto.base.ResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Map;

@Service
@Transactional
public class HttpService {
    private final Logger log = LoggerFactory.getLogger(HttpService.class);

    private final AurLogService aurLog;

    public HttpService(AurLogService aurLog) {
        this.aurLog = aurLog;

    }

    public Object httpPost(String token, String endPoint, Object dto) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = mapper.writeValueAsString(dto);

        long logId = aurLog.logRequest("httpPost", endPoint, requestBody);

        HttpPost httpPost = generateHttpPost(token,endPoint,requestBody);
        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Http Post servisinde hata alındı : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new Exception(errorMessage);
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

    public Object httpGet(String token, String endPoint, MikroV15QueryDTO dto) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String queryParams = mapper.writeValueAsString(dto);

        long logId = aurLog.logRequest("httpGet", endPoint, queryParams);

        URI uri;
        if (dto != null) {
            URIBuilder uriBuilder = new URIBuilder(endPoint);
            Map<String, String> paramMap = dto.toMap();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
            uri = uriBuilder.build();
        } else {
            uri = new URI(endPoint);
        }

        HttpGet httpGet = new HttpGet(uri);

        if (token != null && !token.isBlank()) {
            httpGet.addHeader("Authorization", "Bearer " + token);
        }
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.setHeader("Accept-Encoding", "UTF-8");

        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Http Get servisinde hata alındı : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new Exception(errorMessage);
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

    public Object executeService(String token, String apiPath, RequestDto dto) throws Exception {
        String genericPath = apiPath.concat("/executeService");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = mapper.writeValueAsString(dto);

        long logId = aurLog.logRequest("executeService", genericPath, requestBody);
        HttpPost httpPost = generateHttpPost(token, genericPath, requestBody);

        String result;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Execute Servicede Hata Alındı : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new Exception(errorMessage);
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

    public HttpPost generateHttpPost(String token,String path,String requestBody){
        HttpPost httpPost = new HttpPost(path);

        if (token != null && !token.isBlank()) {
            httpPost.addHeader("Authorization", "Bearer " + token);
        }
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept-Encoding", "UTF-8");
        StringEntity entity = new StringEntity(requestBody, "UTF-8");
        httpPost.setEntity(entity);

        return httpPost;
    }
}
