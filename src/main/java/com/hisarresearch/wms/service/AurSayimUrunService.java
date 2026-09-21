package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.hisarresearch.wms.domain.enumeration.TransactionType;
import com.hisarresearch.wms.repository.AurSayimUrunRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hisarresearch.wms.service.dto.counting.AurSayimDetailDto;
import com.hisarresearch.wms.service.dto.ComparativeCountingResultDTO;
import com.hisarresearch.wms.service.dto.CountingReportMicroDto;
import com.hisarresearch.wms.service.dto.CountingReportDto;
import com.hisarresearch.wms.service.dto.barcode.PalletInfoDTO;
import com.hisarresearch.wms.service.dto.counting.CountingDetailDTO;
import com.hisarresearch.wms.service.dto.counting.CountingSummaryDTO;
import com.hisarresearch.wms.service.dto.counting.CountingSummaryKeyDTO;
import com.hisarresearch.wms.service.mapper.CountingDetailMapper;
import com.hisarresearch.wms.utility.AurHelper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Service Implementation for managing {@link AurSayimUrun}.
 */
@Service
@Transactional
public class AurSayimUrunService {

    private final Logger log = LoggerFactory.getLogger(AurSayimUrunService.class);

    private final PalletBarcodeOrderRelService palletBarcodeOrderRelService;

    private final UserService userService;

    private final EntityManager em;

    private final AurSayimUrunRepository aurSayimUrunRepository;

    private final OrderPickingTransactionService orderPickingTransactionService;

    private final AurSayimUrunQueryService aurSayimUrunQueryService;

    private final CountingDetailMapper countingDetailMapper;

    private final ProductService productService;

    public AurSayimUrunService(AurSayimUrunRepository aurSayimUrunRepository, OrderPickingTransactionService orderPickingTransactionService,
                               EntityManager em, AurSayimUrunQueryService aurSayimUrunQueryService,
                               UserService userService, PalletBarcodeOrderRelService palletBarcodeOrderRelService,
                               CountingDetailMapper countingDetailMapper, ProductService productService) {
        this.aurSayimUrunRepository = aurSayimUrunRepository;
        this.orderPickingTransactionService = orderPickingTransactionService;
        this.em = em;
        this.aurSayimUrunQueryService = aurSayimUrunQueryService;
        this.userService = userService;
        this.palletBarcodeOrderRelService = palletBarcodeOrderRelService;
        this.countingDetailMapper = countingDetailMapper;
        this.productService = productService;
    }

    /**
     * Save a aurSayimUrun.
     *
     * @param countingDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public AurSayimUrun save(CountingDetailDTO countingDetailDTO) {
        log.debug("Request to save AurSayimUrun : {}", countingDetailDTO);
        AurSayimUrun createEntity = countingDetailMapper.toEntity(countingDetailDTO);
        Optional<Product> productOpt = productService.findById(countingDetailDTO.getProduct().getBarcode(),countingDetailDTO.getProduct().getCompanyCode());
        if(productOpt.isPresent()){
            Product product = productOpt.get();
            product.setMiktar(countingDetailDTO.getProduct().getMiktar());
            product.setAnaGrup(countingDetailDTO.getProduct().getAnaGrup());
            product.setKategoriAdi(countingDetailDTO.getProduct().getKategoriAdi());
            product.setStokAdi(countingDetailDTO.getProduct().getStokAdi());
            product.setStokBirimi(countingDetailDTO.getProduct().getStokBirimi());
            product.setSktFlag(countingDetailDTO.getProduct().getSktFlag());
            product.setDescription(countingDetailDTO.getProduct().getDescription());
            product.setStokKodu(countingDetailDTO.getStokKod());
            createEntity.setProduct(product);
        } else {
            Product product = new Product();
            ProductId productId = new ProductId();
            productId.setBarkod(countingDetailDTO.getProduct().getBarcode());
            productId.setCompanyCode(countingDetailDTO.getProduct().getCompanyCode());
            product.setId(productId);
            product.setMiktar(countingDetailDTO.getProduct().getMiktar());
            product.setAnaGrup(countingDetailDTO.getProduct().getAnaGrup());
            product.setKategoriAdi(countingDetailDTO.getProduct().getKategoriAdi());
            product.setStokAdi(countingDetailDTO.getProduct().getStokAdi());
            product.setStokBirimi(countingDetailDTO.getProduct().getStokBirimi());
            product.setSktFlag(countingDetailDTO.getProduct().getSktFlag());
            product.setDescription(countingDetailDTO.getProduct().getDescription());
            product.setStokKodu(countingDetailDTO.getStokKod());
            createEntity.setProduct(product);
        }
        AurSayimUrun newEntity = aurSayimUrunRepository.save(createEntity);
        orderPickingTransactionService.saveByAurSayimUrun(newEntity.getAurSayimTanim().getId(), newEntity.getAddress().getUrunAdresId(), newEntity.getMiktar(), newEntity.getStokKod());
        return newEntity;
    }

    /**
     * Partially update a aurSayimUrun.
     *
     * @param aurSayimUrun the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AurSayimUrun> partialUpdate(AurSayimUrun aurSayimUrun) {
        log.debug("Request to partially update AurSayimUrun : {}", aurSayimUrun);
        return aurSayimUrunRepository
            .findById(aurSayimUrun.getId())
            .map(
                existingAurSayimUrun -> {
                    if (aurSayimUrun.getAddress() != null) {
                        AurDepoUrunAdres updatedAddress = new AurDepoUrunAdres();
                        updatedAddress.setUrunAdresId(aurSayimUrun.getAddress().getUrunAdresId());
                        existingAurSayimUrun.setAddress(updatedAddress);
                    }
                    if (aurSayimUrun.getStokKod() != null) {
                        existingAurSayimUrun.setStokKod(aurSayimUrun.getStokKod());
                    }
                    if (aurSayimUrun.getStatus() != null) {
                        existingAurSayimUrun.setStatus(aurSayimUrun.getStatus());
                    }
                    if (aurSayimUrun.getMiktar() != null) {
                        existingAurSayimUrun.setMiktar(aurSayimUrun.getMiktar());
                    }

                    orderPickingTransactionService.saveByAurSayimUrun(existingAurSayimUrun.getAurSayimTanim().getId(), existingAurSayimUrun.getAddress().getUrunAdresId(), aurSayimUrun.getMiktar(), existingAurSayimUrun.getStokKod());


                    return existingAurSayimUrun;
                }
            )
            .map(aurSayimUrunRepository::save);
    }

    /**
     * Get all the aurSayimUruns.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AurSayimUrun> findAll() {
        log.debug("Request to get all AurSayimUruns");
        return aurSayimUrunRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AurSayimResult> findSayimResult(Long aurSayimTanimId) {
        Query q = em.createNamedQuery("getAurSayimResult");
        q.setParameter("aurSayimTanimId", aurSayimTanimId);

        List<AurSayimResult> dbResultList = q.getResultList();
        em.close();
        return dbResultList;

    }

    /**
     * Get one aurSayimUrun by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AurSayimUrun> findOne(Long id) {
        log.debug("Request to get AurSayimUrun : {}", id);
        return aurSayimUrunRepository.findById(id);
    }

    /**
     * Delete the aurSayimUrun by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AurSayimUrun : {}", id);
        aurSayimUrunRepository.deleteById(id);
    }

    public List<CountingDetailDTO> findProcessedAddress(Long countingDefinitionId) {
        AurHelper aurHelper = new AurHelper();
        List<AurSayimUrun> sayimUrunList =  aurSayimUrunRepository.findByAurSayimTanim_Id(countingDefinitionId)
            .stream()
            .filter(aurHelper.distinctByKey(AurSayimUrun::getAddress))
            .collect(Collectors.toList());
        return countingDetailMapper.toDto(sayimUrunList);
    }

    public List<AurSayimUrun> findByTanimId(Long id) {
        log.debug("Request to get AurSayimUrun By AurSayimTanimId : {}", id);
        return aurSayimUrunRepository.findByAurSayimTanim_Id(id);

    }

    public AurSayimDetailDto getCountingAmountOfProductByCountingDefinitionId(Long countingDefinitionId, String barcode) {
        AurSayimDetailDto sayimDetailDto = new AurSayimDetailDto();

        List<AurSayimUrun> sayimUrunList = aurSayimUrunRepository.findByAurSayimTanim_IdAndStatusAndProduct_Id_Barkod(countingDefinitionId, SayimDurumu.ACTIVE, barcode);
        double totalQuantity = sayimUrunList
            .stream()
            .mapToDouble(AurSayimUrun::getMiktar)
            .sum();
        sayimDetailDto.setCountingStatus(!sayimUrunList.isEmpty());
        sayimDetailDto.setCountingAmount(totalQuantity);
        return sayimDetailDto;
    }

    public void saveByPalletBarcode(Long aurSayimTanimId, String palletBarcode, Long urunAdresId) {
        log.debug("Request to save AurSayimUrun by pallet-barcode: {}", palletBarcode);
        String companyCode = String.valueOf(userService.getUserCompanyCode());

        List<PalletInfoDTO> palletInfoDTOS = palletBarcodeOrderRelService.getPalletInfo(palletBarcode);

        Optional<OrderPickingTransaction> opt = orderPickingTransactionService.findByTransactionTypeAndDescriptionAndStockCodeAndAurTmpDetailId(TransactionType.PALLET_BARCODE_COUNTING, "Pallet-barcode-save", palletBarcode, aurSayimTanimId);

        if (opt.isPresent()) {
            throw new BadRequestAlertException("Pallet barkodu okutulması yapıldı", "Pallet", "");
        } else {
            palletInfoDTOS.forEach(item -> {

                Optional<AurSayimUrun> desiredItem = aurSayimUrunRepository.findByAddress_UrunAdresIdAndStokKodAndAurSayimTanim_Id(urunAdresId, item.getStockCode(), aurSayimTanimId);

                if (desiredItem.isPresent()) {
                    desiredItem.get().setMiktar(desiredItem.get().getMiktar() + item.getAmount());
                    aurSayimUrunRepository.save(desiredItem.get());

                } else {
                    AurSayimUrun aurSayimUrun = new AurSayimUrun();
                    AurSayimTanim sayimTanim = new AurSayimTanim();

                    sayimTanim.setId(aurSayimTanimId);
                    aurSayimUrun.setAurSayimTanim(sayimTanim);
                    AurDepoUrunAdres aurDepoUrunAdres = new AurDepoUrunAdres();
                    aurDepoUrunAdres.setUrunAdresId(urunAdresId);
                    aurSayimUrun.setAddress(aurDepoUrunAdres);
                    aurSayimUrun.setStatus(SayimDurumu.ACTIVE);
                    aurSayimUrun.setStokKod(item.getStockCode());
                    Product product = new Product();
                    ProductId productId = new ProductId(item.getBarcode(), companyCode);
                    product.setId(productId);

                    aurSayimUrun.setProduct(product);
                    aurSayimUrun.setMiktar(item.getAmount());

                    AurSayimUrun savedOne = aurSayimUrunRepository.save(aurSayimUrun);
                    orderPickingTransactionService.saveByAurSayimUrun(savedOne.getAurSayimTanim().getId(), savedOne.getAddress().getUrunAdresId(), aurSayimUrun.getMiktar(), savedOne.getStokKod());

                }


            });

            savePalletTransaction(aurSayimTanimId, palletBarcode, "Pallet-barcode-save");


        }

    }

    void savePalletTransaction(Long aurSayimTanimId, String palletBarcode, String description) {
        orderPickingTransactionService.saveByPalletBarcodeCounting(aurSayimTanimId, palletBarcode, description);
    }

    public List<CountingReportDto> getCountingReportList(Long countingDefinitionId) {
        log.debug("Service that return the list of counting results by id {}", countingDefinitionId);
        return aurSayimUrunRepository.getCountingReport(countingDefinitionId);
    }

    public List<CountingReportMicroDto> getCountingReportListMicro(Long countingDefinitionId) {
        log.debug("Service that return the list of counting results wrt micro quantity by id {}", countingDefinitionId);
        return aurSayimUrunRepository.getCountingReportMicro(countingDefinitionId);
    }

    public List<ComparativeCountingResultDTO> getComparativeCountingReport(Long countingId, Long controlCountingId) {
        log.debug("Service that return the list of comparative counting results wrt countingId {} and controlCountingId {} ", countingId, controlCountingId);
        return aurSayimUrunRepository.getComparativeCountingReport(countingId, controlCountingId);
    }

    public List<AurSayimUrun> getCountedProductDistinct(long countingId){
        log.debug("Get counted product distinct by barcode");
        AurHelper aurHelper = new AurHelper();
        return aurSayimUrunRepository.findByAurSayimTanim_Id(countingId)
            .stream()
            .filter(aurHelper.distinctByKey(aurSayimUrun -> aurSayimUrun.getProduct().getId().getBarkod()))
            .collect(Collectors.toList());
    }


    public List<CountingSummaryDTO> findGroupedByBarcodeStokKodAndAddressWithMiktarAndSktDates(Long countingDefinitionId) {
        List<AurSayimUrun> aurSayimUruns = aurSayimUrunRepository.findByAurSayimTanim_Id(countingDefinitionId);
        return aurSayimUruns.stream()
            .filter(aurSayimUrun -> aurSayimUrun.getProduct() != null && aurSayimUrun.getAddress() != null)
            .collect(Collectors.groupingBy(
                aurSayimUrun -> new CountingSummaryKeyDTO(
                    aurSayimUrun.getProduct().getId().getBarkod(),
                    aurSayimUrun.getStokKod(),
                    aurSayimUrun.getAddress()
                )
            ))
            .entrySet()
            .stream()
            .map(entry -> {
                CountingSummaryKeyDTO key = entry.getKey();
                String barcode = key.getBarcode();
                String stokKod = key.getStokKod();
                AurDepoUrunAdres address = key.getAddress(); // Address object
                List<AurSayimUrun> groupedUruns = entry.getValue();

                double totalMiktar = groupedUruns.stream()
                    .mapToDouble(AurSayimUrun::getMiktar)
                    .sum();

                // Handle null sktDate
                Map<Instant, Double> sktDateMiktarMap = groupedUruns.stream()
                    .filter(aurSayimUrun -> aurSayimUrun.getSktDate() != null)
                    .collect(Collectors.groupingBy(
                        AurSayimUrun::getSktDate,
                        Collectors.summingDouble(AurSayimUrun::getMiktar)
                    ));

                return new CountingSummaryDTO(barcode, stokKod, address, totalMiktar, sktDateMiktarMap);
            })
            .collect(Collectors.toList());

    }

}
