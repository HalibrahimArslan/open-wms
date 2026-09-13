package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.Customer;
import com.hisarresearch.wms.service.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
}
