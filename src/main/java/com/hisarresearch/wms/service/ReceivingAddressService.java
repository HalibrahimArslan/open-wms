package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.repository.WarehouseRepository;
import com.hisarresearch.wms.service.barcode.UniqueBarcodeService;
import com.hisarresearch.wms.service.dto.MalKabulRequestDto;
import com.hisarresearch.wms.service.dto.ProductAddressSaveDTO;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeAddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReceivingAddressService {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Autowired
    private UniqueBarcodeService uniqueBarcodeService;

    @Autowired
    private TranslationService translationService;

    public void completeReceivingAddressOperation(MalKabulRequestDto malKabulRequestDto, Long addressId) {
        Long orderId = malKabulRequestDto.getOrderId();
        String depoNo = String.valueOf(malKabulRequestDto.getDepoNo());

        AurDepoUrunAdres aurDepoUrunAdres = addressService.isExistAddress(addressId);
        String companyCode = userService.getUserCompanyCode().toString();
        Warehouse warehouse = warehouseRepository.findByCode(depoNo).orElseThrow(() -> new BusinessException("Depo Bulunamadı", "Warehouse", "DEPO_YOK"));

        if (!Boolean.TRUE.equals(aurDepoUrunAdres.getStatus()) || !Boolean.TRUE.equals(aurDepoUrunAdres.getGeciciAdres())) {
            throw new BusinessException("Adres aktif değil veya geçici adrese ürün kabülü yapılamaz", "AurDepoUrunAdres", "ADRES_GECERSIZ");
        }

        List<AurOrderDetail> details = aurOrderMasterService.getActiveDetailById(orderId);

        for (AurOrderDetail detail : details) {
            Product product = productService.findById(detail.getBarkod(), companyCode)
                .orElseThrow(() -> new BusinessException(translationService.getErrorMessage("product.notFound"), "product", "productNotFound"));
            boolean lotBased = Boolean.TRUE.equals(product.getLotBasedTracking());
            if (!lotBased) {
                UniqueBarcodeAddressDTO ubAddressDto = new UniqueBarcodeAddressDTO();
                ubAddressDto.setErpBarkod(detail.getBarkod());
                ubAddressDto.setCompanyCode(companyCode);
                ubAddressDto.setAddressId(addressId);
                ubAddressDto.setDepoCode(warehouse.getReceivingCode());
                ubAddressDto.setStokKod(detail.getStokKodu());
                ubAddressDto.setStokAdi(detail.getStokAdi());
                uniqueBarcodeService.transferDetailBarcodesToTemporaryArea(detail.getId(), ubAddressDto);
            } else {
                ProductAddressSaveDTO saveDTO = new ProductAddressSaveDTO();
                saveDTO.setMiktar(detail.getTeslimMiktar());
                saveDTO.setBarcode(detail.getBarkod());
                saveDTO.setStokKod(detail.getStokKodu());
                saveDTO.setStokAdi(detail.getStokAdi());
                saveDTO.setCompanyCode(companyCode);
                saveDTO.setUrunAdres(aurDepoUrunAdres.getAdres());
                saveDTO.setDepoCode(warehouse.getReceivingCode());
                saveDTO.setCheckPartial(true);
                aurDepoUrunAdresStokService.assignProductToAddress(saveDTO);
            }
        }
    }
}
