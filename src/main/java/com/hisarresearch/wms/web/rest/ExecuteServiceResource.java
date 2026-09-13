package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.dto.ServiceRequestDto;
import com.hisarresearch.wms.service.dto.ServiceResponseDto;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@Transactional
public class ExecuteServiceResource {
    private final Logger log = LoggerFactory.getLogger(ExecuteServiceResource.class);

    private final ApplicationContext appContext;

    public ExecuteServiceResource(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @PostMapping("/executeService")
    ResponseEntity<ServiceResponseDto> executeService(@RequestBody ServiceRequestDto request) {
        log.debug("REST request to execute service : {}", request);
        ServiceResponseDto res = new ServiceResponseDto();
        try {
            String paramName = request.getServiceName();
            String serviceName = paramName.substring(0, paramName.indexOf("."));
            String methodName = paramName.substring(paramName.indexOf(".") + 1);

            Object service = appContext.getBean(serviceName);
            Object result = null;
            if (request.getData() == null) {
                Method method = service.getClass().getMethod(methodName);
                result = method.invoke(service);
                res.setData(result);
            } else {
                Object objData = request.getData();
                if (objData instanceof ArrayList) {
                    Method method = service.getClass().getMethod(methodName, JSONArray.class);
                    result = method.invoke(service, JSONArray.fromObject(objData));
                } else {
                    Method method = service.getClass().getMethod(methodName, JSONObject.class);
                    result = method.invoke(service, JSONObject.fromObject(objData));
                }
                res.setData(result);
            }
            res.setSuccess(true);
            res.setMessage("OK");
        } catch (Exception e) {
            res.setSuccess(false);
            res.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.ok(res);
    }

}
