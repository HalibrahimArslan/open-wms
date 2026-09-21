package com.hisarresearch.wms.web.rest;


import com.hisarresearch.wms.domain.AurVwDepoStokAdres;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import com.hisarresearch.wms.repository.AurOrderMasterRepository;
import com.hisarresearch.wms.repository.AurVwDepoStokAdresRepository;
import com.hisarresearch.wms.repository.address.AurDepoAdresRepository;
import com.hisarresearch.wms.repository.address.AurDepoUrunAdresStokRepository;
import com.hisarresearch.wms.service.*;
import com.hisarresearch.wms.service.criteria.AurDepoUrunAdresStokCriteria;
import com.hisarresearch.wms.service.dto.ProductAddressSaveDTO;
import com.hisarresearch.wms.service.dto.productaddress.ProductAddressDefinitionDTO;
import com.hisarresearch.wms.service.dto.address.*;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.exception.validation.InvalidAddressException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Transactional
public class ProductAddressResource {
    private static final String ENTITY_NAME = "productAddress";
    private final Logger log = LoggerFactory.getLogger(ProductAddressResource.class);

    @Autowired
    private EntityManager em;

    @Autowired
    private AurDepoUrunAdresStokRepository aurDepoUrunAdresStokRepository;

    @Autowired
    private AurOrderMasterRepository aurOrderMasterRepository;

    @Autowired
    private AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Autowired
    private AurDepoAdresRepository aurDepoAdresRepository;

    @Autowired
    private AurDepoUrunAdresStokQueryService aurDepoUrunAdresStokQueryService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AurVwDepoStokAdresRepository aurVwDepoStokAdresRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AurLogService logService;


    @GetMapping("/aur-depo-urun-adres-stok")
    public List<AurDepoStokAdresDto> getProductAddressList() throws RuntimeException {
        log.debug("REST request to get all product address list");
        try {
            Query q = em.createNativeQuery("SELECT * FROM aur_vw_depo_stok_adres where companycode = :companyCode", AurVwDepoStokAdres.class);
            ModelMapper mm = new ModelMapper();
            String companyCode = String.valueOf(userService.getUserCompanyCode());
            q.setParameter("companyCode", companyCode);

            List<AurVwDepoStokAdres> dbResultList = q.getResultList();
            return dbResultList
                .stream()
                .map(domain -> mm.map(domain, AurDepoStokAdresDto.class))
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    //TODO product-address-definition ve replacement kullanılarak kaldırılacak
    @PostMapping("/aur-depo-stok-adres")
    public void saveProductAddress(@Valid @RequestBody ProductAddressSaveDTO productAddressSaveDTO) throws Exception {
        long logId = logService.logRequest("saveProductAddress", "aur-depo-stok-adres", productAddressSaveDTO.toString());
        AurDepoUrunAdres aurDepoUrunAdres = aurDepoAdresRepository.findByAdresAndDepoNo(productAddressSaveDTO.getUrunAdres(),
            productAddressSaveDTO.getDepoCode()).orElseThrow(InvalidAddressException::new);

        if (productAddressSaveDTO.getMiktar() < 0) {
            logService.logResponse(logId, "Girilen Miktar Sıfırdan Küçük Olamaz");
            throw new BadRequestAlertException("Girilen Miktar Sıfırdan Küçük Olamaz", ENTITY_NAME, "zeroCheck");
        }

        if (aurDepoUrunAdres.getGeciciAdres() || aurDepoUrunAdres.getKontrolAdres()) {
            aurDepoUrunAdresStokService.assignProductToAddress(productAddressSaveDTO);
        } else {
            aurDepoUrunAdresStokService.transferFromTemporaryAreaToAddress(productAddressSaveDTO);
        }
        logService.logResponse(logId, "success");
    }


    @GetMapping("/check-temporary-area/{depoNo}/{barcode}")
    public AurDepoUrunAdresStok getAddressByBarcode(@PathVariable String depoNo, @PathVariable String barcode) {
        AurDepoUrunAdres temporaryAddress = addressService.checkTemporaryAddress(depoNo);
        Optional<AurDepoUrunAdresStok> aurDepoUrunAdresStok = aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(barcode, temporaryAddress.getUrunAdresId(), true, depoNo);

        if (aurDepoUrunAdresStok.isPresent()) {
            return aurDepoUrunAdresStok.get();
        } else {
            String message = String.format("%s barkodlu ürün için geçici adreste kayıt bulunamadı", barcode);
            throw new BusinessException(message, ENTITY_NAME, "invalidAddress");
        }
    }


    @GetMapping("/miktar/{id}/{miktar}")
    public void updateStockAmount(@PathVariable Long id, @PathVariable Double miktar) throws InvalidAddressException {
        Optional<AurDepoUrunAdresStok> relatedStock = aurDepoUrunAdresStokRepository.findById(id);

        if (relatedStock.isPresent()) {
            double finalAmount = relatedStock.get().getMiktar() - miktar;
            if (finalAmount < 0) {
                throw new BadRequestAlertException("Sıfır hatası", ENTITY_NAME, "zeroCheck");
            }
            relatedStock.get().setMiktar(relatedStock.get().getMiktar() - miktar);
            if (finalAmount == 0) {
                relatedStock.get().setStatus(false);
            }

        } else {
            throw new InvalidAddressException();
        }
    }

    @GetMapping("/total-amount/{barcode}/{depoCode}")
    public Double getProductTotalAmount(@PathVariable String barcode, @PathVariable String depoCode) {
        List<AurDepoUrunAdresStok> aduas = aurDepoUrunAdresStokRepository.findByBarcodeAndStatusAndDepoCode(barcode, true, depoCode);
        if (!aduas.isEmpty()) {
            double totalAmount = 0.0;
            for (AurDepoUrunAdresStok item : aduas) {
                totalAmount = totalAmount + item.getMiktar();
            }
            return totalAmount;
        } else {
            String defaultMessage = String.format("Product %s did not found", barcode);
            throw new BadRequestAlertException(defaultMessage, "AurAddressResource", "productAmountError");
        }

    }

    @PostMapping("/address-amount/{depoCode}")
    public ResponseEntity<List<AurDepoStockCodeDto>> getAmountProductByAddress(@PathVariable String depoCode, @RequestBody List<AurDepoStockCodeDto> aurDepoStockCodeDtoList) {
        return ResponseEntity.ok(aurDepoUrunAdresStokService.getAmountAddressByBarcodeAndAddress(depoCode, aurDepoStockCodeDtoList));
    }

    @GetMapping("/product-category-distribution")
    public ResponseEntity<List<ProductCategoryDistributionDto>> getProductCategoryDistribution() {
        log.debug("Get request product category distribution");
        return ResponseEntity.ok(aurDepoUrunAdresStokService.getProductAddressDistribution());
    }

    @PostMapping("/product-address-definition")
    public ResponseEntity<String> productAddressDefinition(@Valid @RequestBody ProductAddressDefinitionDTO productAddressDefinitionDTO) {
        aurDepoUrunAdresStokService.productAddressDefinition(productAddressDefinitionDTO);
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/product-address-replacement")
    public ResponseEntity<String> productAddressReplacement(@Valid @RequestBody AurProductAddressReplacementDto aurProductAddressReplacementDto) {
        aurDepoUrunAdresStokService.productAddressReplacement(aurProductAddressReplacementDto);
        return ResponseEntity.ok().body("success");
    }

}
