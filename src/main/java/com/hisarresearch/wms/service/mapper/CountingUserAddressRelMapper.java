package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.CountingUserAddressRel;
import com.hisarresearch.wms.service.dto.counting.CountingUserAddressRelDTO;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring",uses = {})
public interface CountingUserAddressRelMapper extends EntityMapper<CountingUserAddressRelDTO, CountingUserAddressRel>{

    @Mapping(source = "addressId", target = "address.urunAdresId" )
    @Mapping(source = "countingDefinitionId", target = "countingDefinition.id" )
    @Mapping(source = "userId", target = "user.id" )
    CountingUserAddressRel toEntity(CountingUserAddressRelDTO countingUserAddressRelDTO);

    @Mapping(source = "address.urunAdresId", target = "addressId" )
    @Mapping(source = "countingDefinition.id", target = "countingDefinitionId" )
    @Mapping(source = "user.id", target = "userId" )
    @Mapping(source = "user.login", target = "login" )
    @Mapping(source = "address.adres", target = "countingAddress" )
    CountingUserAddressRelDTO toDto(CountingUserAddressRel countingUserAddressRelDTO);

    @Mapping(source = "address.urunAdresId", target = "addressId")
    @Mapping(source = "countingDefinition.id", target = "countingDefinitionId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "address.adres", target = "countingAddress")
    @Mapping(target = "counted", ignore = true)
    CountingUserAddressRelDTO toDto(CountingUserAddressRel entity, @Context Set<Long> countedIds);

    @AfterMapping
    default void setCounted(
        @MappingTarget CountingUserAddressRelDTO dto,
        CountingUserAddressRel entity,
        @Context Set<Long> countedIds
    ) {
        if (countedIds != null && entity.getAddress() != null) {
            dto.setCounted(
                countedIds.contains(entity.getAddress().getUrunAdresId())
            );
        }
    }

}
