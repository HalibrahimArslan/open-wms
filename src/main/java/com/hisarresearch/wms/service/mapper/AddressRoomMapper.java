package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AddressRoom;
import com.hisarresearch.wms.service.dto.address.components.AddressRoomDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AddressRoomMapper  extends EntityMapper<AddressRoomDTO, AddressRoom>{
}

