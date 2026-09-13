package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurReserve;
import com.hisarresearch.wms.service.dto.AurReserveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AurReserveMapper extends EntityMapper<AurReserveDTO, AurReserve> {
    @Mapping(source = "product.stokAdi", target = "stokAdi")
    @Mapping(source = "product.stokKodu", target = "stokKodu")
    @Mapping(source = "product.id.barkod",target = "barcode" )
    @Mapping(source = "product.id.companyCode", target = "companyCode")
    AurReserveDTO toDto(AurReserve aurReserve);

    @Mapping(source = "stokAdi", target = "product.stokAdi")
    @Mapping(source = "stokKodu",target = "product.stokKodu")
    @Mapping(source = "barcode", target = "product.id.barkod")
    @Mapping(source = "companyCode",target = "product.id.companyCode")
    AurReserve toEntity(AurReserveDTO aurReserveDTO);



}
