package com.hisarresearch.wms.service.event;

import com.hisarresearch.wms.service.AurReserveService;
import com.hisarresearch.wms.service.dto.event.AurReserveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AurReserveListener {
    private final Logger log = LoggerFactory.getLogger(AurReserveListener.class);
    private final AurReserveService aurReserveService;

    public AurReserveListener(AurReserveService aurReserveService) {
        this.aurReserveService = aurReserveService;
    }

    @Async
    @EventListener
    public void handleUserRegisteredEvent(AurReserveEvent aurReserveEvent) {
        log.info("AurReserveListener received user registered event");
        aurReserveService.saveAurReserves(aurReserveEvent.getAurReserveDTOList());
        log.info("AurReserveListener saved");

    }
}
