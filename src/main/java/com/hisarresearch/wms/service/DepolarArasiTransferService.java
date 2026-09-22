package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.service.dto.*;
import com.hisarresearch.wms.service.dto.erp.ErpOperationResult;
import com.hisarresearch.wms.service.erp.ErpGatewayRouter;
import com.hisarresearch.wms.utility.AurHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class DepolarArasiTransferService {
    private final Logger log = LoggerFactory.getLogger(DepolarArasiTransferService.class);

    private final static String depolarArasiTransfer = "DEPOLAR_ARASI_TRANSFER";

    private final AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    private final OrderPickingTransactionService orderPickingTransactionService;

    private final ErpGatewayRouter erpGatewayRouter;

    private final UserService userService;

    private final WarehouseService warehouseService;

    private final AddressService addressService;

    private final TranslationService translationService;

    public DepolarArasiTransferService(AurDepoUrunAdresStokService aurDepoUrunAdresStokService, OrderPickingTransactionService orderPickingTransactionService, ErpGatewayRouter erpGatewayRouter, UserService userService, WarehouseService warehouseService, AddressService addressService, TranslationService translationService) {
        this.aurDepoUrunAdresStokService = aurDepoUrunAdresStokService;
        this.orderPickingTransactionService = orderPickingTransactionService;
        this.erpGatewayRouter = erpGatewayRouter;
        this.userService = userService;
        this.warehouseService = warehouseService;
        this.addressService = addressService;
        this.translationService = translationService;
    }

    public void depolarArasiTransfer(DepolarArasiTransferDto params) throws Exception {
        log.debug("Depolar arasi transfer Service is working params : {}",params);

        if(params.getGirisDepo() == params.getCikisDepo()){
            String errorMessage = translationService.getErrorMessage("depolarArasiTransfer.invalidParam");
            log.error(errorMessage);
            throw new BusinessException(errorMessage,depolarArasiTransfer,"invalidParam");
        }

        String companyCode = String.valueOf(userService.getUserCompanyCode());
        Warehouse transferWarehouse = warehouseService.findByDepoCodeAndCompanyCode(String.valueOf(params.getCikisDepo()),companyCode);
        Warehouse entranceWarehouse = warehouseService.findByDepoCodeAndCompanyCode(String.valueOf(params.getGirisDepo()),companyCode);
        AurDepoUrunAdres temporaryAddress = addressService.checkTemporaryAddress(entranceWarehouse.getReceivingCode());

        if(!transferWarehouse.getTransferCode().equals(entranceWarehouse.getReceivingCode())){
            aurDepoUrunAdresStokService.decreaseProductAmount(params.getBarcode(),transferWarehouse.getTransferCode(),params.getMiktar(),params.getUrunAdresId());
            ProductAddressSaveDTO productAddressSaveDTO = new ProductAddressSaveDTO();
            productAddressSaveDTO.setStokKod(params.getStokKodu());
            productAddressSaveDTO.setBarcode(params.getBarcode());
            productAddressSaveDTO.setCompanyCode(companyCode);
            productAddressSaveDTO.setDepoCode(entranceWarehouse.getReceivingCode());
            productAddressSaveDTO.setUrunAdres(temporaryAddress.getAdres());
            productAddressSaveDTO.setMiktar(params.getMiktar());
            productAddressSaveDTO.setStatus(true);
            aurDepoUrunAdresStokService.assignProductToAddress(productAddressSaveDTO);
        }

        String erpDocumentNo = sendErpTransfer(params);
        orderPickingTransactionService.saveByInterWarehouseTransferTransactions(params,erpDocumentNo);
        log.debug("Depolar arasi transfer is completed succesfully");
    }


    String sendErpTransfer(DepolarArasiTransferDto dto) throws Exception {
        DepolarArasiTransferErpDto erpDto = new DepolarArasiTransferErpDto();
        DepolarArasiTransferDetailDto erpDetailDto = new DepolarArasiTransferDetailDto();
        List<DepolarArasiTransferDetailDto> erpDetailList = new ArrayList<>();

        erpDto.setGirDepoNo(dto.getGirisDepo());
        erpDto.setCikDepoNo(dto.getCikisDepo());
        erpDto.setAciklama(dto.getDescription());
        erpDto.setTarih(AurHelper.getDateForMicro());
        erpDto.setErpUserCode("1");

        erpDetailDto.setStokKodu(dto.getStokKodu());
        erpDetailDto.setTransferMiktar(dto.getMiktar());
        erpDetailDto.setReferansSiparisNo(dto.getDescription());
        erpDetailList.add(erpDetailDto);
        erpDto.setDetailList(erpDetailList);

        ErpGatewayRouter.ErpCallContext ctx = erpGatewayRouter.context();
        ErpOperationResult result = erpGatewayRouter.interWarehouseTransfer(ctx.getToken(), ctx.getApiPath(), erpDto);
        if (!result.isSuccess()) {
            // Servis rollbackOn = Exception ile isaretli; yerel stok hareketi de geri alinir.
            throw new BusinessException(result.getMessage(), depolarArasiTransfer, "erpTransferFailed");
        }
        return result.getReference();

    }


}
