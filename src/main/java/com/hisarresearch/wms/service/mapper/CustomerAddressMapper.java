package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.CustomerAddress;
import com.hisarresearch.wms.service.dto.CustomerAddressDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomerAddressMapper.class})
public interface CustomerAddressMapper {

    CustomerAddressDTO toDto(CustomerAddress customerAddress);


}
