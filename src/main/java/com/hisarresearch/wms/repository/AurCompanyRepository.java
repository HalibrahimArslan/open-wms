package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AurCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurCompanyRepository extends JpaRepository<AurCompany, Long> {
    //String USER_COMPANY_INFO = "usersCompanyInfo";

    //@Cacheable(cacheNames = USER_COMPANY_INFO)
    AurCompany findByCompanyCode(Integer companyCode);
}
