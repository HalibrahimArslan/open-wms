package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurVwDepoStokAdres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AurVwDepoStokAdresRepository extends JpaRepository<AurVwDepoStokAdres, Long> {
    List<AurVwDepoStokAdres> findByUrunAdresIdAndDepoCodeAndStatus(Long urunAdresId, String depoCode, Boolean status);
    List<AurVwDepoStokAdres> findByBarkodAndStatusAndDepoCode(String barcode, Boolean status, String depoCode);
}
