package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.service.dto.AurOrderDetailDTO;
import com.hisarresearch.wms.service.dto.AurOrderDetailResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AurOrderDetailSktMapper.class, UniqueBarcodeMapper.class})
public interface AurOrderDetailMapper extends EntityMapper<AurOrderDetailDTO, AurOrderDetail> {

    @Mapping(source = "order.id",target = "orderId")
    AurOrderDetailDTO toDto(AurOrderDetail aurOrderDetail);

    @Mapping(source = "orderId",target = "order.id")
    AurOrderDetail toEntity(AurOrderDetailDTO aurTmpDetailDTO);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.orderInfo", target = "orderInfo")
    @Mapping(source = "uniqueBarcodes", target = "uniqueBarcodeList")
    AurOrderDetailResponseDTO toResponseDto(AurOrderDetail aurOrderDetail);

    List<AurOrderDetailResponseDTO> toResponseDtoList(List<AurOrderDetail> aurOrderDetails);
}
