package com.hisarresearch.wms.service.event;

import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.service.AurOrderMasterService;
import com.hisarresearch.wms.service.dto.event.AurOrderMasterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderMasterListener {
    private final Logger log = LoggerFactory.getLogger(OrderMasterListener.class);
    private final AurOrderMasterService orderMasterService;

    public OrderMasterListener(AurOrderMasterService orderMasterService) {
        this.orderMasterService = orderMasterService;
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void handleOrderWhenOutProgressEvent(AurOrderMasterEvent aurOrderMasterEvent) {
        log.info("Order Master received transfer to control event : {}",aurOrderMasterEvent);
        AurOrderMaster orderMaster = orderMasterService.isExistOrderById(aurOrderMasterEvent.getAurOrderMaster().getId());
        orderMasterService.transferOrderControlArea(orderMaster);
        log.info("Order transferred to control area");

    }
}
