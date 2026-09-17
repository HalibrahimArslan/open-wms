package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AddressType;
import com.hisarresearch.wms.service.dto.address.components.AddressTypeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {})
public interface AddressTypeMapper extends EntityMapper<AddressTypeDTO, AddressType> {
}
