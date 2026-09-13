package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetailSkt;
import com.hisarresearch.wms.repository.AurOrderDetailSktRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AurOrderDetailSktService {
    private final Logger log = LoggerFactory.getLogger(AurOrderDetailSktService.class);

    private final AurOrderDetailSktRepository aurTmpDetailSktRepository;

    public AurOrderDetailSktService(AurOrderDetailSktRepository aurTmpDetailSktRepository) {
        this.aurTmpDetailSktRepository = aurTmpDetailSktRepository;
    }

    /**
     *  Get all tmp detail skt list
     */
    @Transactional
    public List<AurOrderDetailSkt> findAll() {
        log.debug("Request to get all order detail skt list");
        return aurTmpDetailSktRepository.findAll();
    }


}
