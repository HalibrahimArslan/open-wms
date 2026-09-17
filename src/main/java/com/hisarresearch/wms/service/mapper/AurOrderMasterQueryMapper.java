package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.service.dto.AurOrderMasterQueryDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { AurOrderDetailMapper.class })
public interface AurOrderMasterQueryMapper extends EntityMapper<AurOrderMasterQueryDTO, AurOrderMaster>{
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AurOrderMasterQueryDTO toDtoId(AurOrderMaster orderMaster);

}
