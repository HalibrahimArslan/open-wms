package com.hisarresearch.wms.service.picking;

import com.hisarresearch.wms.domain.ProductAddressv2;
import com.hisarresearch.wms.domain.enumeration.WarehousePickingRuleType;
import com.hisarresearch.wms.service.dto.address.AurDepoStokUrunAdresDto;

import java.util.List;

public interface ProductAddressStrategy {
    List<ProductAddressv2> findPickingProductAddresses(AurDepoStokUrunAdresDto dto, String stockCode);
    WarehousePickingRuleType getType();

}

