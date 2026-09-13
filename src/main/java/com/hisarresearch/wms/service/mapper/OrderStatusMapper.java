package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.service.dto.OrderStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderStatus} and its DTO {@link OrderStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderStatusMapper extends EntityMapper<OrderStatusDTO, OrderStatus> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderStatusDTO toDtoId(OrderStatus orderStatus);
}
