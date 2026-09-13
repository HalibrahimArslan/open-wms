package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurIntegrationLogs;
import com.hisarresearch.wms.repository.AurIntegrationLogsRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class AurLogService {

    @Autowired
    private AurIntegrationLogsRepository logRepo;

    public long logRequest(String serviceName, String serviceUrl, String requestMessage) {
        try {
            AurIntegrationLogs aurIntegrationLogs = new AurIntegrationLogs();
            aurIntegrationLogs.setServiceName(serviceName);
            aurIntegrationLogs.setServiceUrl(serviceUrl);
            aurIntegrationLogs.setRequestBody(requestMessage);
            aurIntegrationLogs = logRepo.save(aurIntegrationLogs);
            return aurIntegrationLogs.getLogId();
        } catch (Exception e) {
            System.out.println("LogRequest Error =>" + serviceUrl + "-" + serviceName + " : " + requestMessage);
            e.printStackTrace();
        }

        return -1;
    }

    public long logResponse(Long logRequestId, String responseMessage) {
        try {
            Optional<AurIntegrationLogs> aurIntegrationLogs = logRepo.findById(logRequestId);
            if (aurIntegrationLogs.isPresent()) {
                aurIntegrationLogs.get().setResponseBody(responseMessage);
                logRepo.save(aurIntegrationLogs.get());
            }
        } catch (Exception e) {
            System.out.println("LogResponse Error =>" + responseMessage + "!!!!!");
            e.printStackTrace();
        }

        return -1;
    }
}
