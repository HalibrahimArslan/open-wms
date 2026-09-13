package com.hisarresearch.wms.service;

import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.AurPdfRequestDto;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.erp.ErpTokenService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BarcodeService {

    private final ErpTokenService erpTokenService;

    private final UserService userService;

    private final HttpService httpService;

    public BarcodeService(ErpTokenService erpTokenService, UserService userService, HttpService httpService) {
        this.erpTokenService = erpTokenService;
        this.userService = userService;
        this.httpService = httpService;
    }

    public Object generateBarcodePdf(String stokKod) throws Exception {
        AurPdfRequestDto aurPdfRequestDto = new AurPdfRequestDto();
        aurPdfRequestDto.setStokKodu(stokKod);
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        String token = erpTokenService.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        RequestDto dto = new RequestDto();
        dto.setServiceName("stokService.stokSorgula");
        dto.setData(aurPdfRequestDto);

        return httpService.executeService(token,aurCompanyDto.getApiEndPoint(),dto);
    }
}
