package com.hisarresearch.wms.service.event;

//import com.hisarresearch.wms.domain.ProductAddressv2;
//import com.hisarresearch.wms.service.dto.productaddress.ProductUpdateMessage;
//import com.hisarresearch.wms.exception.validation.InsufficientStockException;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hisarresearch.wms.repository.ProductAddressv2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductUpdateListener {

    private static final Logger logger = LoggerFactory.getLogger(ProductUpdateListener.class);


    @Autowired
    private ProductAddressv2Repository productAddressRepository;

    private ConcurrentHashMap<String, String> resultStorage = new ConcurrentHashMap<>();


//    @RabbitListener(queues = "product-update-queue")
//    public void handleProductUpdate(ProductUpdateMessage message) {
//        String requestId = message.getRequestId();
//        try {
//            logger.info("Received message: {}", message);
//
//            Optional<ProductAddressv2> productAddressOptional = productAddressRepository.findByProduct_Id_BarkodAndDepoCode(
//                message.getBarcode(), message.getDepoCode());
//
//            if (productAddressOptional.isPresent()) {
//                ProductAddressv2 productAddress = productAddressOptional.get();
//                double finalAmount = productAddress.getMiktar() - message.getAmount();
//                if (finalAmount < 0) {
//                    resultStorage.put(requestId, "Insufficient stock");
//                    throw new InsufficientStockException("Insufficient stock for product: " + message.getBarcode() + " at depot: " + message.getDepoCode());
//                }
//                productAddress.setMiktar(finalAmount);
//                productAddressRepository.save(productAddress);
//                resultStorage.put(requestId, "Update successful");
//                logger.info("Updated stock for product: {} at depot: {}. New amount: {}", message.getBarcode(), message.getDepoCode(), finalAmount);
//
//            } else {
//                resultStorage.put(requestId, "Product or address not found");
//                logger.warn("Product not found for barcode: {} and depot: {}", message.getBarcode(), message.getDepoCode());
//            }
//        } catch (Exception e) {
//            logger.error("Error processing message: {}", message, e);
//            resultStorage.put(requestId, "Error: " + e.getMessage());
//        }
//
//    }
//
//    public String getResult(String requestId) {
//        return resultStorage.get(requestId);
//    }
}
