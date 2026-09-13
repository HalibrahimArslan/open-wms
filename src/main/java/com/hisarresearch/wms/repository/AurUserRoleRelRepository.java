package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurUserRoleRel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AurUserRoleRel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurUserRoleRelRepository extends JpaRepository<AurUserRoleRel, Long> {
    List<AurUserRoleRel> findByRole_IdAndUser_Activated(Long roleId,Boolean activated);
    Optional<AurUserRoleRel> findByRole_IdAndUser_Id(Long roleId,Long userId);
}
