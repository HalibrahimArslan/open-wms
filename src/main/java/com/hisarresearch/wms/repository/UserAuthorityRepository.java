package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.UserAuthority;
import com.hisarresearch.wms.domain.UserAuthorityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, UserAuthorityId> {
}
