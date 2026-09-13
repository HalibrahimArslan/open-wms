package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.UserDepoRel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the UserDepoRel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDepoRelRepository extends JpaRepository<UserDepoRel, Long>, JpaSpecificationExecutor<UserDepoRel> {
    Optional<UserDepoRel> findByUser_IdAndWarehouse_Code(Long userId, String code);
    List<UserDepoRel> findByUser_Id(Long userId);
    List<UserDepoRel> findByWarehouse_Id(Long warehouseId);
}
