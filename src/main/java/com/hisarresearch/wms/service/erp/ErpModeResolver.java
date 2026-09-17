package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import org.springframework.stereotype.Service;

/**
 * Oturumdaki kullanicinin sirketi icin ERP entegrasyonunun acik olup olmadigini belirler.
 *
 * <p>Iki durumda "local mod" kabul edilir:
 * <ul>
 *     <li>sirketin {@code erpType} degeri {@link ErpConnectionType#LOCAL}</li>
 *     <li>ya da {@code apiParameters.erpApiActive} degeri {@code true} degil</li>
 * </ul>
 *
 * <p>{@code apiParameters} ya da {@code erpApiActive} bos ise local mod kabul edilir.
 */
@Service
public class ErpModeResolver {

    private final UserService userService;

    public ErpModeResolver(UserService userService) {
        this.userService = userService;
    }

    public boolean isLocalMode() {
        return isLocalMode(userService.getUserCompanyInfo());
    }

    public static boolean isLocalMode(AurCompanyDTO company) {
        if (company == null) {
            return true;
        }
        return isLocalErpType(company) || !isErpApiActive(company);
    }

    public static boolean isLocalErpType(AurCompanyDTO company) {
        return company == null || company.getErpType() == null || company.getErpType() == ErpConnectionType.LOCAL;
    }

    public static boolean isErpApiActive(AurCompanyDTO company) {
        if (company == null || company.getApiParameters() == null) {
            return false;
        }
        return Boolean.TRUE.equals(company.getApiParameters().getErpApiActive());
    }
}
