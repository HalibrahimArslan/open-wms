package com.hisarresearch.wms.repository;


import com.hisarresearch.wms.domain.AurGrupMailAdres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AurGrupMailAdresRepository extends JpaRepository<AurGrupMailAdres, Long> {
    Optional<AurGrupMailAdres> findByGrupKodu(Integer grupKodu);
}
