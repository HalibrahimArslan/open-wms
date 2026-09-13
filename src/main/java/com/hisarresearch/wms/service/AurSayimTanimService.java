package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.CountingType;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.hisarresearch.wms.repository.AurSayimTanimRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.hisarresearch.wms.service.criteria.ProductCountingTypePairingCriteria;
import com.hisarresearch.wms.service.dto.address.AddressCountingResponseDto;
import com.hisarresearch.wms.service.dto.counting.AurSayimDetailDto;
import com.hisarresearch.wms.service.dto.productaddress.ProductAddressDefinitionDTO;
import com.hisarresearch.wms.service.dto.counting.CountingDefinitionDTO;
import com.hisarresearch.wms.service.dto.counting.CountingSummaryDTO;
import com.hisarresearch.wms.service.mapper.CountingDefinitionMapper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.exception.validation.InvalidAddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service Implementation for managing {@link AurSayimTanim}.
 */
@Service
@Transactional
public class AurSayimTanimService {

    private final Logger log = LoggerFactory.getLogger(AurSayimTanimService.class);

    private static final String ENTITY_NAME = "aurSayimTanim";

    @Autowired
    private AurSayimTanimRepository aurSayimTanimRepository;

    @Autowired
    private CountingAddressExceptionService countingAddressExceptionService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AurSayimUrunService aurSayimUrunService;

    @Autowired
    private ProductCountingTypePairingQueryService productCountingTypePairingQueryService;

    @Autowired
    private ProductCountingTypePairingService productCountingTypePairingService;

    @Autowired
    private CountingDefinitionMapper countingDefinitionMapper;

    @Autowired
    private AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Autowired
    private ProductAddressService productAddressService;

    @Autowired
    private WebSocketClientService webSocketClientService;

    @Autowired
    private CountingUserAddressRelService countingUserAddressRelService;

    @Autowired
    private UserService userService;

    @Autowired
    private TranslationService translationService;

    /**
     * Save a aurSayimTanim.
     *
     * @param countingDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    public AurSayimTanim save(CountingDefinitionDTO countingDefinitionDTO) {
        log.debug("Request to save AurSayimTanim : {}", countingDefinitionDTO);
        String desiredAuthority = countingDefinitionDTO.getVisibilityAuthorities()[0];
        List<String> activeAuthorities = aurSayimTanimRepository.findByDepoNoAndStatusAndSayimDurumu(countingDefinitionDTO.getDepoNo(), true, SayimDurumu.ACTIVE)
            .stream()
            .flatMap(counting -> Arrays.stream(counting.getVisibilityAuthorities()))
            .collect(Collectors.toList());

        for(String authority : activeAuthorities) {
            if(authority.equals(desiredAuthority)) {
                throw new BadRequestAlertException("There is already counting with related authority", ENTITY_NAME, "existscounting");
            }
        }
        String countingSerial = countingDefinitionDTO.getSayimAdi().concat("-").concat(String.valueOf(getLastCountingDefinitionSerial(countingDefinitionDTO)));

        countingDefinitionDTO.setSayimTarihi(Instant.now());
        countingDefinitionDTO.setSayimAdi(countingSerial);
        AurSayimTanim savedOne = aurSayimTanimRepository.save(countingDefinitionMapper.toEntity(countingDefinitionDTO));
        countingAddressExceptionService.saveCountingDefinition(savedOne, savedOne.getDepoNo());
        return savedOne;
    }

    /**
     * Partially update a aurSayimTanim.
     *
     * @param aurSayimTanim the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AurSayimTanim> partialUpdate(AurSayimTanim aurSayimTanim) {
        log.debug("Request to partially update AurSayimTanim : {}", aurSayimTanim);
        final boolean[] isChangedCountingStatus = {false};
        final String[] sayimAdi = {null};

        return aurSayimTanimRepository
            .findById(aurSayimTanim.getId())
            .map(
                existingAurSayimTanim -> {
                    if (aurSayimTanim.getStatus() != null) {
                        existingAurSayimTanim.setStatus(aurSayimTanim.getStatus());
                    }
                    if (aurSayimTanim.getSayimAdi() != null) {
                        existingAurSayimTanim.setSayimAdi(aurSayimTanim.getSayimAdi());
                    }
                    if (aurSayimTanim.getDepoNo() != null) {
                        existingAurSayimTanim.setDepoNo(aurSayimTanim.getDepoNo());
                    }
                    if (aurSayimTanim.getSayimTarihi() != null) {
                        existingAurSayimTanim.setSayimTarihi(aurSayimTanim.getSayimTarihi());
                    }
                    if (aurSayimTanim.getSayimDurumu() != null) {
                        existingAurSayimTanim.setSayimDurumu(aurSayimTanim.getSayimDurumu());
                        isChangedCountingStatus[0] = true;
                        sayimAdi[0] = existingAurSayimTanim.getSayimAdi();
                    }
                    if (aurSayimTanim.getAciklama() != null) {
                        existingAurSayimTanim.setAciklama(aurSayimTanim.getAciklama());
                    }
                    if (aurSayimTanim.getSayimiBitirenKullanici() != null) {
                        existingAurSayimTanim.setSayimiBitirenKullanici(aurSayimTanim.getSayimiBitirenKullanici());
                    }
                    if (aurSayimTanim.getSayimiOnaylayanKullanici() != null) {
                        existingAurSayimTanim.setSayimiOnaylayanKullanici(aurSayimTanim.getSayimiOnaylayanKullanici());
                    }
                    if (aurSayimTanim.getCreatedDate() != null) {
                        existingAurSayimTanim.setCreatedDate(aurSayimTanim.getCreatedDate());
                    }
                    if (aurSayimTanim.getCreatedBy() != null) {
                        existingAurSayimTanim.setCreatedBy(aurSayimTanim.getCreatedBy());
                    }
                    if (aurSayimTanim.getLastModifiedDate() != null) {
                        existingAurSayimTanim.setLastModifiedDate(aurSayimTanim.getLastModifiedDate());
                    }
                    if (aurSayimTanim.getLastModifiedBy() != null) {
                        existingAurSayimTanim.setLastModifiedBy(aurSayimTanim.getLastModifiedBy());
                    }

                    return existingAurSayimTanim;
                }
            )
            .map(entity -> {
                if(isChangedCountingStatus[0]){
                    webSocketClientService.send(sayimAdi[0] + " adlı sayımın durumu " + aurSayimTanim.getSayimDurumu().getLabel() + " olarak güncellendi.");
                }
                return aurSayimTanimRepository.save(entity);
            });
    }

    /**
     * Get all the aurSayimTanims.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AurSayimTanim> findAll() {
        log.debug("Request to get all AurSayimTanims");
        return aurSayimTanimRepository.findAll();
    }

    /**
     * Get one aurSayimTanim by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AurSayimTanim> findOne(Long id) {
        log.debug("Request to get AurSayimTanim : {}", id);
        return aurSayimTanimRepository.findById(id);
    }

    /**
     * Delete the counting-definition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete counting definition by id : {}", id);
        aurSayimTanimRepository.deleteById(id);
    }

    public AurSayimTanim isExistCountingDefinition(Long id){
        log.debug("Request to exist counting definition by id : {}", id);
        return aurSayimTanimRepository.findById(id).orElseThrow(()-> new BusinessException("Invalid counting definition id",ENTITY_NAME,"invalidId"));
    }

    public List<AddressCountingResponseDto> getNonCountableAddressListByCountingDefinitionId(Long countingDefinitionId){
        log.debug("Request to get non-countable address list by counting definition id : {}", countingDefinitionId);
        isExistCountingDefinition(countingDefinitionId);
        return addressService.getNotCountedAddressList(countingDefinitionId);
    }

    public long getCountOfCountableAddressList(Long countingDefinitionId){
        log.debug("Request to get count of non-countable address list by counting definition id : {}", countingDefinitionId);
        AurSayimTanim existOne = isExistCountingDefinition(countingDefinitionId);
        return addressService.getCountOfCountableAddressList(countingDefinitionId, existOne.getDepoNo());
    }

    public List<AurSayimDetailDto> getCountingResultByCountingType(Long countingDefinitionId){
        List<AurSayimDetailDto> sayimDetailDtoList = new ArrayList<>();
        AurSayimTanim aurSayimTanim = isExistCountingDefinition(countingDefinitionId);
        CountingType countingType =  aurSayimTanim.getCountingType();
        int warehouse = Integer.parseInt(aurSayimTanim.getDepoNo());
        ProductCountingTypePairingCriteria criteria = new ProductCountingTypePairingCriteria();
        ProductCountingTypePairingCriteria.CountingTypeFilter countingTypeFilter = new ProductCountingTypePairingCriteria.CountingTypeFilter();
        IntegerFilter warehouseFilter = new IntegerFilter();
        StringFilter companyCodeFilter = new StringFilter();

        countingTypeFilter.setEquals(countingType);
        warehouseFilter.setEquals(warehouse);
        companyCodeFilter.setEquals("4");

        criteria.setCountingType(countingTypeFilter);
        criteria.setWarehouseCode(warehouseFilter);
        criteria.setCompanyCode(companyCodeFilter);

        List<ProductCountingTypePairing> productCountingTypePairingList = productCountingTypePairingQueryService.findByCriteria(criteria);

        if(productCountingTypePairingList.isEmpty()){
            List<AurSayimUrun> aurSayimUruns = aurSayimUrunService.getCountedProductDistinct(countingDefinitionId);
            aurSayimUruns.forEach(aurSayimUrun -> {
                AurSayimDetailDto sayimDetailDto = aurSayimUrunService.getCountingAmountOfProductByCountingDefinitionId(countingDefinitionId,aurSayimUrun.getProduct().getId().getBarkod());
                sayimDetailDto.setProduct(aurSayimUrun.getProduct());
                sayimDetailDtoList.add(sayimDetailDto);
            });
            return sayimDetailDtoList;
        }

        for (ProductCountingTypePairing pairing : productCountingTypePairingList) {
            AurSayimDetailDto sayimDetailDto = aurSayimUrunService.getCountingAmountOfProductByCountingDefinitionId(countingDefinitionId,pairing.getProduct().getId().getBarkod());
            sayimDetailDto.setProduct(pairing.getProduct());
            sayimDetailDtoList.add(sayimDetailDto);
        }

        return sayimDetailDtoList;

    }

    @Async
    public void completeCountingDefinition(Long countingDefinitionId, boolean clearHistory){
        try{
            AurSayimTanim currentCounting = isExistCountingDefinition(countingDefinitionId);
            List<CountingSummaryDTO> countingSummaryDTOS = aurSayimUrunService.findGroupedByBarcodeStokKodAndAddressWithMiktarAndSktDates(countingDefinitionId);
            deletePreviousProductAddresses(currentCounting,countingSummaryDTOS, clearHistory);
            countingSummaryDTOS.forEach(countingSummaryDTO -> {
                ProductAddressDefinitionDTO saveDto = toAurUrunAdresSaveDto(countingSummaryDTO,currentCounting);
                aurDepoUrunAdresStokService.productAddressDefinition(saveDto);
            });
            currentCounting.setStatus(false);
            currentCounting.setSayimDurumu(SayimDurumu.COMPLETED);
            webSocketClientService.send(currentCounting.getSayimAdi() + " adlı sayımın hareketleri adreslerine işlendi.");
        }
        catch(Exception e){
            webSocketClientService.send("Sayım Tamamlamada problem oldu");
        }
    }


    public void deletePreviousProductAddresses(AurSayimTanim existingCounting,List<CountingSummaryDTO> groupedProducts,boolean clearHistory){
        if(existingCounting.getCountingType() != null && existingCounting.getCountingType().equals(CountingType.DRILLING) && !clearHistory){
            groupedProducts.forEach(countingSummaryDTO -> productAddressService.deleteProductAddressAndSktList(
                existingCounting.getDepoNo(),
                existingCounting.getCompanyCode(),
                countingSummaryDTO.getBarcode(),
                countingSummaryDTO.getAddress()
            ));
            return;
        }
        productAddressService.deleteProductAddressAndSktList(existingCounting.getDepoNo(),existingCounting.getCompanyCode());

    }


    public ProductAddressDefinitionDTO toAurUrunAdresSaveDto(CountingSummaryDTO summary, AurSayimTanim aurSayimTanim) {
        ProductAddressDefinitionDTO dto = new ProductAddressDefinitionDTO();
        dto.setBarcode(summary.getBarcode());
        dto.setUrunAdresId(summary.getAddress().getUrunAdresId());
        dto.setMiktar(summary.getTotalMiktar());
        dto.setStokKodu(summary.getStokKod());
        dto.setDepoNo(aurSayimTanim.getDepoNo());

        List<ProductAddressSkt> sktDateList = summary.getSktDateMiktarMap().entrySet().stream()
            .map(entry -> {
                ProductAddressSkt skt = new ProductAddressSkt();
                skt.setSktDate(entry.getKey());
                skt.setQuantity(entry.getValue());
                return skt;
            })
            .collect(Collectors.toList());

        dto.setSktDateList(sktDateList);

        return dto;
    }

    public long getLastCountingDefinitionSerial(CountingDefinitionDTO countingDefinitionDTO){
       long countOfPastCountingList = aurSayimTanimRepository.findByDepoNoAndCompanyCodeOrderByIdDesc(countingDefinitionDTO.getDepoNo(), countingDefinitionDTO.getCompanyCode()).size();
       return countOfPastCountingList + 1;
    }

    public int getCountOfProductCountingTypePairingByCountingDefinitionId(long countingDefinitionId){
        AurSayimTanim aurSayimTanim = isExistCountingDefinition(countingDefinitionId);
        CountingType countingType =  aurSayimTanim.getCountingType();
        String companyCode = aurSayimTanim.getCompanyCode();
        int warehouseCode = Integer.parseInt(aurSayimTanim.getDepoNo());
        return productCountingTypePairingService.findByCountingTypeAndWarehouseCodeAndCompanyCode(countingType,warehouseCode,companyCode).size();
    }

    public void checkCountingAddress(Long countingDefinitionId,Long addressId){
        isExistCountingDefinition(countingDefinitionId);
        AurDepoUrunAdres address = addressService.findById(addressId).orElseThrow(InvalidAddressException::new);
        countingAddressExceptionService.findByAddressAndCountingDefinitionAndStatusTrue(addressId,countingDefinitionId)
            .ifPresent(exception -> {
                throw new BusinessException(translationService.getErrorMessage("countingException.invalidAddress",address.getAdres()),ENTITY_NAME,"countingException.invalidAddress");
            });
        Optional<CountingUserAddressRel> countingUserAddressRel = countingUserAddressRelService.findByCountingDefinitionIdAndAddressId(countingDefinitionId,addressId);
        if(countingUserAddressRel.isPresent()){
            String userName = userService.getUserName();
            String assignedUserName = countingUserAddressRel.get().getUser().getLogin();
            if(!userName.equals(assignedUserName)){
                throw new BusinessException(translationService.getErrorMessage("countingUserAddressRel.invalidUser",address.getAdres(),assignedUserName),ENTITY_NAME,"countingUserAddressRel.invalidUser");
            }
        }
    }

}
