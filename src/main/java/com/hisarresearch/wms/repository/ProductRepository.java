package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.domain.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findById(ProductId id);

    Product findByStokKodu(String StokKodu);

    List<Product> findByIdBarkodInAndIdCompanyCode(List<String> barkodlar, String companyCode);

}
