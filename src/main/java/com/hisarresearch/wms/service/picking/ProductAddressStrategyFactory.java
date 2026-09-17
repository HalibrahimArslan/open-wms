package com.hisarresearch.wms.service.picking;

import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.domain.enumeration.WarehousePickingRuleType;
import com.hisarresearch.wms.repository.WarehouseRepository;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductAddressStrategyFactory {
    private final Map<WarehousePickingRuleType, ProductAddressStrategy> strategyMap;
    private final WarehouseService warehouseService;
    private final UserService userService;

    public ProductAddressStrategyFactory(List<ProductAddressStrategy> strategies,  WarehouseService warehouseService, UserService userService) {
        this.warehouseService = warehouseService;
        this.userService = userService;

        this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(ProductAddressStrategy::getType, strategy -> strategy));

    }

    public ProductAddressStrategy getStrategy(String depoCode) {
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        String warehouseCode = warehouseService.transformWarehouseCode(depoCode);
        Warehouse warehouse = warehouseService.findByDepoCodeAndCompanyCode(warehouseCode,companyCode);
        WarehousePickingRuleType ruleType = warehouse.getPickingRuleType();

        if (ruleType == null) {
            ruleType = WarehousePickingRuleType.DEFAULT;
        }
        return strategyMap.getOrDefault(ruleType, strategyMap.get(WarehousePickingRuleType.DEFAULT));
    }
}

