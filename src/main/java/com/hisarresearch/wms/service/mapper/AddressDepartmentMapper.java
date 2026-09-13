package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AddressDepartment;
import com.hisarresearch.wms.service.dto.address.components.AddressDepartmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AddressDepartmentMapper  extends EntityMapper<AddressDepartmentDTO, AddressDepartment>{
}
