package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurMailAddTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AurMailAddToRepository extends JpaRepository<AurMailAddTo, Long> {
    List<AurMailAddTo> findByCompanyCode(int companyCode);
    Optional<AurMailAddTo> findByCompanyCodeAndMailAdres(int companyCode, String mailAdres);
}
