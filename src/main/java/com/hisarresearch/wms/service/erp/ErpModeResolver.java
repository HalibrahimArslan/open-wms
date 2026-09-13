package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Oturumdaki kullanicinin sirketi icin ERP entegrasyonunun acik olup olmadigini belirler.
 *
 * <p>Iki durumda "local mod" kabul edilir:
 * <ul>
 *     <li>sirketin {@code erpTipi} degeri {@link ErpConnectionType#LOCAL}</li>
 *     <li>ya da {@code apiParameters} icindeki {@code erpApiActive} degeri "1" degil</li>
 * </ul>
 *
 * <p>{@code apiParameters} bos veya {@code erpApiActive} anahtari yoksa local mod kabul
 * edilir; onceki kod bu durumda {@code JSONObject.getString} uzerinden hata firlatiyordu.
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
        Integer erpCode = erpCode(company);
        return erpCode == null || ErpConnectionType.LOCAL.getErpCode() == erpCode;
    }

    /** {@code erpTipi} sayiya cevrilemiyorsa {@code null} doner. */
    public static Integer erpCode(AurCompanyDTO company) {
        if (company == null || company.getErpTipi() == null) {
            return null;
        }
        try {
            return Integer.valueOf(company.getErpTipi().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean isErpApiActive(AurCompanyDTO company) {
        if (company == null || company.getApiParameters() == null) {
            return false;
        }
        try {
            JSONObject params = JSONObject.fromObject(company.getApiParameters());
            return params.has("erpApiActive") && "1".equals(params.getString("erpApiActive"));
        } catch (RuntimeException e) {
            return false;
        }
    }
}
