package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.barcode.UniqueBarcode;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeCreateDTO;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {ProductWithoutAddressMapper.class})
public abstract class UniqueBarcodeMapper {

    @Mapping(source = "erpOrderNo", target = "erpOrderInfo")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "barcode", ignore = true)
    @Mapping(target = "partiCode", ignore = true)
    @Mapping(target = "lotNumber", ignore = true)
    @Mapping(target = "receivingDate", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    public abstract UniqueBarcode toEntity(UniqueBarcodeCreateDTO dto);

    @Mapping(source = "product.stokKodu", target = "stokKodu")
    @Mapping(source = "product.stokAdi", target = "stokAdi")
    @Mapping(source = "product.anaGrup", target = "anaGrup")
    @Mapping(source = "product.kategoriAdi", target = "kategoriAdi")
    @Mapping(source = "product.stokBirimi", target = "stokBirimi")
    @Mapping(source = "product.id.barkod", target = "barkod")
    @Mapping(source = "product.lotBasedTracking", target = "lotBasedTracking")
    public abstract UniqueBarcodeResponseDTO toDTO(UniqueBarcode entity);
}
