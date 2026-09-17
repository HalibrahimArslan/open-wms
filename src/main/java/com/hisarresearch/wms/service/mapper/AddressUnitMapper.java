package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AddressUnit;
import com.hisarresearch.wms.service.dto.address.components.AddressUnitDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AddressUnitMapper extends EntityMapper<AddressUnitDTO, AddressUnit> {
}
