package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AddressFlat;
import com.hisarresearch.wms.service.dto.address.components.AddressFlatDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AddressFlatMapper  extends EntityMapper<AddressFlatDTO, AddressFlat>{
}
