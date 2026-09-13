package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.ProductAddressSkt;
import com.hisarresearch.wms.domain.ProductAddressv2;
import com.hisarresearch.wms.repository.ProductAddressSktRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class ProductAddressSktService {
    private final Logger log = LoggerFactory.getLogger(ProductAddressSktService.class);

    @Autowired
    private ProductAddressSktRepository productAddressSktRepository;


    public void updateStatusProductAddressSkt(long productAddressId) {
        productAddressSktRepository.findByProductAddress_Id(productAddressId).forEach(productAddressSkt -> productAddressSkt.setStatus(false));
    }



    public ProductAddressSkt createOrUpdateSktList(ProductAddressv2 productAddress, Instant sktDate, double quantity) {
        Optional<ProductAddressSkt> productAddressSkt = productAddressSktRepository.
            findByProductAddress_IdAndStatusAndSktDate(productAddress.getId(),true,sktDate);

        if(productAddressSkt.isPresent()){
            productAddressSkt.get().setQuantity(productAddressSkt.get().getQuantity() + quantity);
            return productAddressSkt.get();
        }
        else{
            ProductAddressSkt skt = new ProductAddressSkt();
            skt.setProductAddress(productAddress);
            skt.setSktDate(sktDate);
            skt.setStatus(true);
            skt.setQuantity(quantity);
            return productAddressSktRepository.save(skt);
        }

    }

    public ProductAddressSkt createOrReplaceSktList(ProductAddressv2 productAddress, Instant sktDate, double quantity) {
        Optional<ProductAddressSkt> productAddressSkt = productAddressSktRepository.
            findByProductAddress_IdAndStatusAndSktDate(productAddress.getId(),true,sktDate);

        if(productAddressSkt.isPresent()){
            productAddressSkt.get().setQuantity(quantity);
            return productAddressSkt.get();
        }
        else{
            ProductAddressSkt skt = new ProductAddressSkt();
            skt.setProductAddress(productAddress);
            skt.setSktDate(sktDate);
            skt.setStatus(true);
            skt.setQuantity(quantity);
            return productAddressSktRepository.save(skt);
        }

    }

}
