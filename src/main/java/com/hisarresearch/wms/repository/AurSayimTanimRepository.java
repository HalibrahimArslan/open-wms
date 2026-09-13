package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurSayimTanim;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the AurSayimTanim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurSayimTanimRepository extends JpaRepository<AurSayimTanim, Long>, JpaSpecificationExecutor<AurSayimTanim> {
    List<AurSayimTanim> findByDepoNoAndStatusAndSayimDurumu(String depoNo, boolean status, SayimDurumu sayimDurumu);
    List<AurSayimTanim> findByDepoNoAndCompanyCodeOrderByIdDesc(String depoNo, String companyCode);
}
