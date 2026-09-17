package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.UserFirmRel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the UserFirmRel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserFirmRelRepository extends JpaRepository<UserFirmRel, Long>,JpaSpecificationExecutor<UserFirmRel> {
    List<UserFirmRel> findByUser_Id(Long userId);
    Optional<UserFirmRel> findByUser_IdAndFirmCode(Long userId,String firmCode);
}
