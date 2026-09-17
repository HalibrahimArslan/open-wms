package com.hisarresearch.wms.service.picking;

import com.hisarresearch.wms.domain.ProductAddressv2;
import com.hisarresearch.wms.domain.enumeration.WarehousePickingRuleType;
import com.hisarresearch.wms.repository.ProductAddressv2Repository;
import com.hisarresearch.wms.service.dto.address.AurDepoStokUrunAdresDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressScopeStrategy implements ProductAddressStrategy {

    private final ProductAddressv2Repository repository;

    public AddressScopeStrategy(ProductAddressv2Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductAddressv2> findPickingProductAddresses(AurDepoStokUrunAdresDto dto, String stockCode) {
        return repository.findByDepoCodeAndStatusTrueAndStokKodAndUrunAdres_ToplamaGozuAndUrunAdres_AdresStartingWith(
            dto.getDepoCode(), stockCode, true, "A"
        );
    }

    @Override
    public WarehousePickingRuleType getType() {
        return WarehousePickingRuleType.ADDRESS_SCOPE;
    }


}
