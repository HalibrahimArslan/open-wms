package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AurDepoUrunAdresStokRepository extends JpaRepository<AurDepoUrunAdresStok, Long>,JpaSpecificationExecutor<AurDepoUrunAdresStok> {
    List<AurDepoUrunAdresStok> findByUrunAdresIdAndDepoCode(Long urunAdresId,String depoCode);
    Optional<AurDepoUrunAdresStok> findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(String barcode, Long urunAdresId,Boolean status,String depoCode);
    Optional<AurDepoUrunAdresStok> findByPaletBarkodIdAndStokKod(Long paletBarkodId,String stokKod);
    List<AurDepoUrunAdresStok> findByBarcodeAndStatusAndDepoCode(String barcode, Boolean status,String depoCode);
    List<AurDepoUrunAdresStok> findByStokKodAndStatusAndDepoCode(String stockCode, Boolean status,String depoCode);
    List<AurDepoUrunAdresStok> findByUrunAdresIdAndDepoCodeAndStatus(Long urunAdresId,String depoCode,Boolean status);
    List<AurDepoUrunAdresStok> findByStatusAndMiktar(Boolean status,Double miktar);
    List<AurDepoUrunAdresStok> findByDepoCodeAndCompanyCodeAndStatusTrue(String depoCode,String companyCode);
    List<AurDepoUrunAdresStok> findByStokKodStartingWithAndStatus(String stockCode,Boolean status);

}
