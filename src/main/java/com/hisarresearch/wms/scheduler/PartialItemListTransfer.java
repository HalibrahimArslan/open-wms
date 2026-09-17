package com.hisarresearch.wms.scheduler;


import com.hisarresearch.wms.domain.AurPartialDetails;
import com.hisarresearch.wms.domain.AurPartialItem;
import com.hisarresearch.wms.domain.ErpJwtData;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.repository.AurPartialDetailsRepository;
import com.hisarresearch.wms.repository.AurPartialItemRepository;
import com.hisarresearch.wms.service.*;
import com.hisarresearch.wms.service.dto.AurPartialItemDTO;
import com.hisarresearch.wms.service.dto.barcode.PackageCodeList;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.erp.MikroServices;
import com.hisarresearch.wms.exception.validation.InvalidErpTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PartialItemListTransfer {
    private final Logger log = LoggerFactory.getLogger(PartialItemListTransfer.class);

    public static final String CRON_DAILY_GUN_ICI = "0 */1 8-20 * * *"; //Her gün saat 08.00-20.00 arasında (20.00 haric) 30 dakikada bir çalışır.

    private final MikroServices mikroServices;

    private final ErpJwtDataService erpJwtDataService;

    private final AurPartialItemRepository aurPartialItemRepository;

    private final AurPartialItemService aurPartialItemService;

    private final AurPartialDetailsRepository aurPartialDetailsRepository;

    private final HttpService httpService;

    /** Mikro API kok adresi; dagitima ozgu oldugu icin config uzerinden verilir. */
    @Value("${wms.erp.mikro.base-path:}")
    private String basePath;


    public PartialItemListTransfer(MikroServices mikroServices, ErpJwtDataService erpJwtDataService,
                                   AurPartialItemRepository aurPartialItemRepository, AurPartialItemService aurPartialItemService,
                                   AurPartialDetailsRepository aurPartialDetailsRepository,HttpService httpService) {
        this.mikroServices = mikroServices;
        this.erpJwtDataService = erpJwtDataService;
        this.aurPartialItemRepository = aurPartialItemRepository;
        this.aurPartialItemService = aurPartialItemService;
        this.aurPartialDetailsRepository = aurPartialDetailsRepository;
        this.httpService = httpService;
    }

//    @Scheduled(cron = CRON_DAILY_GUN_ICI)
    public void getPartialItemList() throws Exception{
        log.info("Scheduler partial item list from MICRO-API");
        RequestDto requestDto = new RequestDto();
        requestDto.setServiceName("stokService.packageList");

        //Kullanıcı erpType'ına göre token alınır
        ErpJwtData erpJwtData = erpJwtDataService.findByErpType(ErpConnectionType.MIKRO_V16).orElseThrow(InvalidErpTypeException::new);
        String token = erpJwtData.getToken();

        Object microPackageList  = httpService.executeService(token,basePath, requestDto);
        List<LinkedHashMap<String,String>> packageList = (List<LinkedHashMap<String,String>>)microPackageList;
        List<AurPartialItemDTO> partialItemList = aurPartialItemService.findAllList();

        packageList.forEach(packageItem ->{
            String packageCode = packageItem.get("packageCode");
            if(partialItemList.stream().noneMatch(item -> item.getPackageCode().equals(packageCode))){
                AurPartialItem partialItem = new AurPartialItem();
                partialItem.setPackageCode(packageItem.get("packageCode"));
                partialItem.setPackageName(packageItem.get("packageName"));
                partialItem.setStatus(true);
                partialItem.setPackageBarcode(packageItem.get("packageBarcode"));

                AurPartialItem savedPartialItem = aurPartialItemRepository.save(partialItem);
                try {
                    savePartialDetailList(savedPartialItem,token,packageCode);
                } catch (Exception e) {
                    log.error("Parçalı ürün transfer sürecinde hata meydana geldi");
                    throw new RuntimeException(e);
                }
            }
        });
    };

    //İlgili parçalı ürünün parça detaylarının sorgulanıp kaydedildiği yer
    private void savePartialDetailList(AurPartialItem partialItem,String token,String packageCode) throws Exception {
        RequestDto requestDto = new RequestDto();
        PackageCodeList packageCodeList = new PackageCodeList();
        List<String> packageList = new ArrayList<>();

        packageList.add(packageCode);
        packageCodeList.setPackageCode(packageList);

        requestDto.setData(packageCode);
        requestDto.setServiceName("stokService.packageDetail");


        Object microPackageDetail  = httpService.executeService(token,basePath, requestDto);
        List<LinkedHashMap<String,Object>> packageDetailList = (List<LinkedHashMap<String,Object>>)microPackageDetail;

        packageDetailList.forEach(detail -> {
            AurPartialDetails aurPartialDetails = new AurPartialDetails();
            aurPartialDetails.setStockCode((String)detail.get("stockCode"));
            aurPartialDetails.setQuantity((double) (int) detail.get("quantity"));
            aurPartialDetails.setBarcode((String)detail.get("barcode"));
            aurPartialDetails.setStockName((String)detail.get("stockName"));
            aurPartialDetails.setAurPartialItem(partialItem);

            aurPartialDetailsRepository.save(aurPartialDetails);
        });

    }
}



