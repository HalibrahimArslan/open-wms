package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.ProductAddressSkt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAddressSktRepository extends JpaRepository<ProductAddressSkt, Long> {
    Optional<ProductAddressSkt> findByProductAddress_IdAndStatusAndSktDate(Long productAddressId, Boolean status, Instant sktDate);
    List<ProductAddressSkt> findByProductAddress_Id(long productAddressId);
}
