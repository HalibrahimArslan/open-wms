package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AddressHall;
import com.hisarresearch.wms.service.dto.address.components.AddressHallDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AddressHallMapper  extends EntityMapper<AddressHallDTO, AddressHall>{
}
