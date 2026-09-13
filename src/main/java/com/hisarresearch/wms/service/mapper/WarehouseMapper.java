package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.service.dto.WarehouseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface WarehouseMapper  extends EntityMapper<WarehouseDTO, Warehouse>{
}
