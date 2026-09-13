package com.hisarresearch.wms.service.event;

import com.hisarresearch.wms.service.MailService;
import com.hisarresearch.wms.service.dto.event.OrderMailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MailListener {
    private final Logger log = LoggerFactory.getLogger(MailListener.class);

    private final MailService mailService;

    public MailListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Async
    @TransactionalEventListener(
        phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleDispatcherMail(OrderMailEvent orderMailEvent) {
        log.info("MailListener handleDispatcherMail event is started");
        mailService.sendMailVersion2(orderMailEvent.getOrderId());
        log.info("MailListener handleDispatcherMail event is finished");

    }
}
