package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.CountingUserAddressRel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountingUserAddressRelRepository extends JpaRepository<CountingUserAddressRel, Long> {
    List<CountingUserAddressRel> findByCountingDefinition_IdAndAddress_UrunAdresIdIn(Long countingDefinitionId, List<Long> addresses);

    @EntityGraph(attributePaths = {"user", "address"})
    List<CountingUserAddressRel> findAllByIdIn(List<Long> ids);

    Optional<CountingUserAddressRel> findByCountingDefinition_IdAndAddress_UrunAdresId(Long countingDefinitionId, Long addressId);
}
