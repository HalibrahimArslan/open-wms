package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.OrderRow;
import com.hisarresearch.wms.service.dto.OrderRowDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * OrderMapper'in siparis satirlari icin kullandigi OrderRowMapper'a gecikmeli erisim.
 * <p>
 * OrderRowMapper siparis alani icin OrderMapper'i kullandigindan ikisi dogrudan
 * birbirini {@code uses} ile alirsa dongusel bean bagimliligi olusur (Spring Boot 2.6+
 * buna izin vermez). OrderMapper bu sinifi kullanir; OrderRowMapper ilk cagrida cozulur.
 */
@Component
public class LazyOrderRowMapper {

    private final OrderRowMapper orderRowMapper;

    public LazyOrderRowMapper(@Lazy OrderRowMapper orderRowMapper) {
        this.orderRowMapper = orderRowMapper;
    }

    public OrderRow toEntity(OrderRowDTO orderRowDTO) {
        return orderRowMapper.toEntity(orderRowDTO);
    }

    public OrderRowDTO toDto(OrderRow orderRow) {
        return orderRowMapper.toDto(orderRow);
    }
}
