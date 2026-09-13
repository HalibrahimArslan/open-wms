package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.ErpJwtData;
import com.hisarresearch.wms.repository.ErpJwtDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ErpJwtDataService {

    private final Logger log = LoggerFactory.getLogger(ErpJwtDataService.class);

    private final ErpJwtDataRepository erpJwtDataRepository;

    private final CacheManager cacheManager;

    private final AurLogService aurLogService;

    public ErpJwtDataService(ErpJwtDataRepository erpJwtDataRepository, CacheManager cacheManager, AurLogService aurLogService) {
        this.erpJwtDataRepository = erpJwtDataRepository;
        this.cacheManager = cacheManager;
        this.aurLogService = aurLogService;
    }

    @Cacheable(cacheNames ="erpDataByErpType",key = "#erpType")
    public Optional<ErpJwtData> findByErpType(String erpType){
        return erpJwtDataRepository.findByErpTipi(erpType);
    }


    public ErpJwtData save(ErpJwtData erpJwtData){
        log.debug("Request to save ErpJwtData : {}", erpJwtData);
        return erpJwtDataRepository.save(erpJwtData);
    }

    public void clearAllCaches() {
        Objects.requireNonNull(cacheManager.getCache("erpDataByErpType")).clear();

    }

    public void deleteToken(ErpJwtData erpJwtData){
        Long logId = aurLogService.logRequest("ErpJwtDataService","deleteToken", erpJwtData.toString());
        erpJwtDataRepository
            .findById(erpJwtData.getId())
            .ifPresent(
                token -> {
                    log.debug("Deleted erp_jwt_token: {}",token);
                    aurLogService.logResponse(logId,"Deleted erp_jwt_token: " + token);
                    this.clearAllCaches();
                    erpJwtDataRepository.delete(token);
                }
            );
    }

}
