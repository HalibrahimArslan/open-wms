package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.CountingAddressException;
import com.hisarresearch.wms.service.dto.CountingAddressExceptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface CountingAddressExceptionMapper extends EntityMapper<CountingAddressExceptionDto, CountingAddressException> {
    @Mapping(source = "addressId", target = "address.urunAdresId" )
    @Mapping(source = "countingDefinitionId", target = "countingDefinition.id" )
    CountingAddressException toEntity(CountingAddressExceptionDto countingAddressExceptionDto);

    default CountingAddressException fromId(Long id){
        if (id == null) return null;
        CountingAddressException countingAddressException = new CountingAddressException();
        countingAddressException.setId(id);
        return countingAddressException;
    }
}
