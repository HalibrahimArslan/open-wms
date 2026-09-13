package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.ProductAddressv2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductAddressv2Repository extends JpaRepository<ProductAddressv2,Long>, JpaSpecificationExecutor<ProductAddressv2> {
    List<ProductAddressv2> findByDepoCodeAndCompanyCodeAndProduct_Id_BarkodAndUrunAdres_UrunAdresIdAndStatusTrue(String depoCode, String companyCode, String barkod, Long addressId);
    List<ProductAddressv2> findByDepoCodeAndCompanyCodeAndProduct_Id_BarkodAndStatus(String depoCode, String companyCode, String barkod, boolean status);
    List<ProductAddressv2> findDistinctByStatusTrueAndDepoCodeAndCompanyCode(String depoCode, String companyCode);
    List<ProductAddressv2> findDistinctByStatusTrueAndDepoCodeAndCompanyCodeAndUrunAdres_Reyon(String depoCode, String companyCode,String rayon);
    List<ProductAddressv2> findByDepoCodeAndCompanyCodeAndStatusTrue(String depoCode, String companyCode);
    List<ProductAddressv2> findByDepoCodeAndStatusTrueAndStokKodAndUrunAdres_ToplamaGozu(String depoCode, String stockCode,Boolean pickingAddress);
    List<ProductAddressv2> findByDepoCodeAndCompanyCodeAndProduct_Id_BarkodAndStatusAndUrunAdres_ToplamaGozu(String depoCode, String companyCode, String barkod, boolean status,boolean pickingAddress);
    Optional<ProductAddressv2> findByProduct_Id_BarkodAndDepoCode(String barcode,String depoCode);
    List<ProductAddressv2> findByDepoCodeAndStatusTrueAndStokKodAndUrunAdres_ToplamaGozuAndUrunAdres_AdresStartingWith(String depoCode, String stockCode,Boolean pickingAddress,String address );
}
