package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {})
public interface ProductWithoutAddressMapper extends EntityMapper<ProductWithoutAddressDTO, Product> {

    @Mapping(target = "id.barkod",source = "barcode")
    @Mapping(target = "id.companyCode",source = "companyCode")
    Product toEntity(ProductWithoutAddressDTO productWithoutAddressDTO);

    @Mapping(target = "barcode",source = "id.barkod")
    @Mapping(target = "companyCode",source = "id.companyCode")
    ProductWithoutAddressDTO toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stokKodu", ignore = true)
    @Mapping(target = "anaGrup", ignore = true)
    @Mapping(target = "kategoriAdi", ignore = true)
    @Mapping(target = "stokBirimi", ignore = true)
    @Mapping(target = "miktar", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "physicalAttributes", ignore = true)
    @Mapping(target = "sktFlag", ignore = true)
    @Mapping(target = "productAddresses", ignore = true)
    void partialUpdateAllowedFields(@MappingTarget Product entity, ProductWithoutAddressDTO dto);
}
