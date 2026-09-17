package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.service.dto.OrderRowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderRow} and its DTO {@link OrderRowDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface OrderRowMapper extends EntityMapper<OrderRowDTO, OrderRow> {
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    OrderRowDTO toDto(OrderRow s);
}
