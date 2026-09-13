package com.hisarresearch.wms.service;

import com.hisarresearch.wms.service.dto.base.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpManagementService {

    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    @Autowired
    private HttpService httpService;


    public Object executeService(String token, String apiPath, RequestDto dto) throws Exception {
        Object data = httpService.executeService(token, apiPath, dto);
        if(dto.getServiceName().equals("depoService.getOrderDetailList")) {
            return aurOrderMasterService.getFilteredDepoOrderDetails(data);

        }
        return data;
    }
}
