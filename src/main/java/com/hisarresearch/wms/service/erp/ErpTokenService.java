package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.ErpJwtData;
import com.hisarresearch.wms.service.AurLogService;
import com.hisarresearch.wms.service.ErpJwtDataService;
import com.hisarresearch.wms.service.HttpService;
import com.hisarresearch.wms.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ErpTokenService {

    @Autowired
    private UserService userService;

    @Autowired
    private ErpJwtDataService erpJwtDataService;

    @Autowired
    private HttpService httpService;

    @Autowired
    private AurLogService aurLog;

    public String getToken(String apiPath, String apiParameters) throws Exception {
        String erpType = userService.getUserErpType();
        Optional<ErpJwtData> erpJwtData = erpJwtDataService.findByErpType(erpType);
        if (erpJwtData.isPresent()) {
            String[] chunks = erpJwtData.get().getToken().split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            JSONObject jsonObject = JSONObject.fromObject(payload);

            if (jsonObject.getLong("exp") < (System.currentTimeMillis() / 1000)) {
                erpJwtDataService.deleteToken(erpJwtData.get());
                String token = fetchToken(apiPath, apiParameters);
                ErpJwtData jwt = new ErpJwtData();
                jwt.setToken(token);
                jwt.setErpTipi(erpType);
                erpJwtDataService.save(jwt);
                return token;
            }
            return erpJwtData.get().getToken();
        } else {
            String token = fetchToken(apiPath, apiParameters);
            erpJwtDataService.clearAllCaches();
            ErpJwtData jwt = new ErpJwtData();
            jwt.setToken(token);
            jwt.setErpTipi(erpType);
            erpJwtDataService.save(jwt);
            return token;
        }
    }

    private String fetchToken(String apiPath, String apiParameters) throws Exception {
        String authEndpoint = apiPath + "/authenticate";

        JSONObject parameters = JSONObject.fromObject(apiParameters);
        String password = parameters.getString("password");
        String username = parameters.getString("username");

        Map<String, String> request = new HashMap<>();
        request.put("password", password);
        request.put("username", username);
        request.put("rememberMe", "true");

        String requestBody = convertToString(request);
        HttpPost httpPost = httpService.generateHttpPost(null, authEndpoint, requestBody);
        String result;
        long logId = aurLog.logRequest("fetchToken", authEndpoint, requestBody);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String errorMessage = "Authenticate Servisinde Hata Alındı : " + response.getStatusLine().getReasonPhrase();
                aurLog.logResponse(logId, errorMessage);
                throw new Exception(errorMessage);
            }
            result = EntityUtils.toString(response.getEntity());
            aurLog.logResponse(logId, result);
        }

        JSONObject response = JSONObject.fromObject(result);
        return response.getString("id_token");
    }

    private String convertToString(Object requestBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(requestBody);
    }
}
