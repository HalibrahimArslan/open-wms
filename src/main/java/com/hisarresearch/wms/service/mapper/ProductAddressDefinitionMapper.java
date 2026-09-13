package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.ProductAddressv2;
import com.hisarresearch.wms.service.dto.productaddress.ProductAddressDefinitionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface ProductAddressDefinitionMapper extends EntityMapper<ProductAddressDefinitionDTO, ProductAddressv2> {

    @Mapping(target = "product.id.barkod",source = "barcode")
    @Mapping(target = "depoCode", source = "depoNo" )
    @Mapping(target = "urunAdres.urunAdresId" ,source = "urunAdresId")
    @Mapping(target = "product.stokKodu",source = "stokKodu")
    @Mapping(target = "productAddressSktList" ,source = "sktDateList")
    @Mapping(target = "product.stokAdi" ,source = "stokAdi")
    ProductAddressv2 toEntity(ProductAddressDefinitionDTO productAddressDefinitionDTO);

}
