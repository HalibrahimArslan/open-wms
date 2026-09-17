package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurMenuRoleRel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the AurMenuRoleRel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurMenuRoleRelRepository extends JpaRepository<AurMenuRoleRel, Long> {
    Optional<AurMenuRoleRel> findByMenu_IdAndRole_Id(Long menuId, Long roleId);
}
