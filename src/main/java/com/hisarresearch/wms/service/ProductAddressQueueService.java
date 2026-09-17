package com.hisarresearch.wms.service;

//import com.hisarresearch.wms.service.dto.productaddress.ProductUpdateMessage;
//import com.hisarresearch.wms.service.event.ProductUpdateListener;
//import org.springframework.amqp.core.AmqpAdmin;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductAddressQueueService {

  /*  @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductUpdateListener productUpdateListener;

    @Autowired
    private AmqpAdmin amqpAdmin;

    public String createQueue(String queueName) {
        Queue queue = new Queue(queueName, false);
        amqpAdmin.declareQueue(queue);
        return queueName;
    }

    public String updateProductAmount(String barcode, Long addressId, int amount,String depoCode) {
        ProductUpdateMessage message = new ProductUpdateMessage();
        String requestId = UUID.randomUUID().toString();
        String queueName = createQueue("product-update-queue");

        message.setBarcode(barcode);
        message.setAddressId(addressId);
        message.setAmount(amount);
        message.setDepoCode(depoCode);
        message.setRequestId(requestId);

        rabbitTemplate.convertAndSend(queueName, message);
        return requestId;

    }

    public String getUpdateResult(String requestId) {
        return productUpdateListener.getResult(requestId);
    }*/
}
