package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AurRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurRoleRepository extends JpaRepository<AurRole, Long> {
    Optional<AurRole> findByRoleName(String role);

    List<AurRole> findAllByCompanyCode(Integer companyCode);

    Optional<AurRole> findByRoleNameAndCompanyCode(String roleName, Integer companyCode);
}
