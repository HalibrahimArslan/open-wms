package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.domain.ProductCountingTypePairing;
import com.hisarresearch.wms.domain.enumeration.CountingType;
import com.hisarresearch.wms.repository.ProductCountingTypePairingRepository;
import com.hisarresearch.wms.service.dto.ProductCountingTypePairingBulkDTO;
import com.hisarresearch.wms.service.dto.ProductCountingTypePairingDTO;
import com.hisarresearch.wms.service.mapper.ProductCountingTypePairingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductCountingTypePairingService {
    private final Logger log = LoggerFactory.getLogger(ProductCountingTypePairingService.class);

    private final ProductCountingTypePairingRepository productCountingTypePairingRepository;

    private final ProductCountingTypePairingMapper productCountingTypePairingMapper;

    private final ProductService productService;

    public ProductCountingTypePairingService(ProductCountingTypePairingRepository productCountingTypePairingRepository,
                                             ProductCountingTypePairingMapper productCountingTypePairingMapper,
                                             ProductService productService) {
        this.productCountingTypePairingRepository = productCountingTypePairingRepository;
        this.productCountingTypePairingMapper = productCountingTypePairingMapper;
        this.productService = productService;
    }


    /**
     * Get all the product counting-type pairings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductCountingTypePairingDTO> findAll() {
        log.debug("Request to get all ProductCountingTypePairings");
        return productCountingTypePairingMapper.toDto(productCountingTypePairingRepository.findAll());
    }

    public ProductCountingTypePairingDTO createProductCountingTypePairing(ProductCountingTypePairingDTO productCountingTypePairingDTO) {
        log.debug("Request to create ProductCountingTypePairing : {}", productCountingTypePairingDTO);
        ProductCountingTypePairing createOne = productCountingTypePairingMapper.toEntity(productCountingTypePairingDTO);
        Optional<Product> product = productService.findById(productCountingTypePairingDTO.getProduct().getBarcode(),productCountingTypePairingDTO.getProduct().getCompanyCode());
        if(product.isPresent()){
            createOne.setProduct(product.get());
        }
        return productCountingTypePairingMapper.toDto(productCountingTypePairingRepository.save(createOne));
    }

    public List<ProductCountingTypePairingDTO> createBulkProductCountingTypePairing(ProductCountingTypePairingBulkDTO productCountingTypePairingBulkDTO) {
        log.debug("Request to create bulk ProductCountingTypePairing : {}", productCountingTypePairingBulkDTO);
        List<ProductCountingTypePairingDTO> createdList = new ArrayList<>();
        productCountingTypePairingBulkDTO.getProductList().stream().forEach(pairingItem -> {
            ProductCountingTypePairingDTO productCountingTypePairingDTO = new ProductCountingTypePairingDTO();
            productCountingTypePairingDTO.setProduct(pairingItem);
            productCountingTypePairingDTO.setWarehouseCode(productCountingTypePairingBulkDTO.getWarehouseCode());
            productCountingTypePairingDTO.setCountingType(productCountingTypePairingBulkDTO.getCountingType());
            ProductCountingTypePairingDTO createdOne = createProductCountingTypePairing(productCountingTypePairingDTO);
            createdList.add(createdOne);
        });

      return createdList;
    }

    public void deleteOne(Long id){
        log.debug("Request to delete ProductCountingTypePairing : {}", id);
        productCountingTypePairingRepository.findById(id).get().setProduct(null);
        productCountingTypePairingRepository.deleteById(id);
    }

    @Transactional
    public List<ProductCountingTypePairing> findByCountingTypeAndWarehouseCodeAndCompanyCode(CountingType countingType, int warehouseCode, String companyCode) {
        return productCountingTypePairingRepository.findByCountingTypeAndWarehouseCodeAndProduct_Id_CompanyCode(countingType,warehouseCode,companyCode);
    }


}
