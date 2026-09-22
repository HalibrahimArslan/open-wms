package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.Order;
import com.hisarresearch.wms.service.dto.OrderDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { OrderStatusMapper.class, LazyOrderRowMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoId(Order order);
}
