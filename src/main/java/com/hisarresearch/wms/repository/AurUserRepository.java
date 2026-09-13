package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AurUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurUserRepository extends JpaRepository<AurUser, Long> {
    //String USER_PROFILE_INFO = "usersProfileInfo";

    //@Cacheable(cacheNames = USER_PROFILE_INFO)
    AurUser findByLogin(String userName);

}
