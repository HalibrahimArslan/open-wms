package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurMenu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AurMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurMenuRepository extends JpaRepository<AurMenu, Long> {
    Optional<AurMenu> findByMenuName(String menuName);
    List<AurMenu> findByParentMenuId(Integer parentMenuId);
}
