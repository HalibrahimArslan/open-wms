package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AddressMovementHistory;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import com.hisarresearch.wms.domain.enumeration.AddressMovementType;
import com.hisarresearch.wms.repository.AddressMovementHistoryRepository;
import com.hisarresearch.wms.service.dto.AddressPlacementDto;
import com.hisarresearch.wms.service.dto.productaddress.ProductAddressDefinitionDTO;
import com.hisarresearch.wms.service.dto.address.AurProductAddressReplacementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service class for address movements.
 */


@Service
@Transactional
public class AddressMovementHistoryService {

    @Autowired
    private AddressMovementHistoryRepository addressMovementHistoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Autowired
    private AddressService addressService;


    public List<AddressMovementHistory> getAllPlacementHistory(){
        return addressMovementHistoryRepository.findAll();
    }


    public AddressMovementHistory saveAddressPlacementFromTmp(AddressPlacementDto dto) {
        Optional<AurDepoUrunAdresStok> originAddress = aurDepoUrunAdresStokService.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(dto.getBarcode(),dto.getOriginAddressId(),true,dto.getDepoCode());
        Optional<AurDepoUrunAdresStok> placementAddress = aurDepoUrunAdresStokService.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(dto.getBarcode(),dto.getPlacementAddressId(),true,dto.getDepoCode());
        AddressMovementHistory adph = new AddressMovementHistory();

        adph.setProcessAmount(dto.getChangeAmount());
        adph.setStokKodu(dto.getStokKodu());
        adph.setBarcode(dto.getBarcode());
        adph.setAurUser(userService.getUser());

       if(originAddress.isPresent()){
           adph.setOriginAddress(generateAddress(originAddress.get().getUrunAdresId()));
           adph.setOriginUpdatedAmount(originAddress.get().getMiktar() - dto.getChangeAmount());
       }
       else{
           AurDepoUrunAdres temporaryAddress = addressService.checkTemporaryAddress(dto.getDepoCode());
           adph.setOriginAddress(temporaryAddress);
           adph.setOriginUpdatedAmount(0.0);
       }

       if(placementAddress.isPresent()){
           adph.setPlacementAddress(generateAddress(placementAddress.get().getUrunAdresId()));
           adph.setPlacementUpdatedAmount(placementAddress.get().getMiktar());
       }
       else{
           adph.setPlacementAddress(generateAddress(dto.getPlacementAddressId()));
           adph.setPlacementUpdatedAmount(dto.getChangeAmount());
       }

        adph.setMovementType(AddressMovementType.PLACEMENT_FROM_TMP);
        addressMovementHistoryRepository.save(adph);
        return adph;
    }

    public void saveAddressDefinitionMovement(ProductAddressDefinitionDTO dto, Double previousAmount){
        Long addressId = dto.getUrunAdresId();
        AddressMovementHistory amh = new AddressMovementHistory();
        amh.setAurUser(userService.getUser());
        amh.setStokKodu(dto.getStokKodu());
        amh.setBarcode(dto.getBarcode());
        amh.setProcessAmount(dto.getMiktar());

        amh.setPlacementAddress(generateAddress(addressId));
        amh.setPlacementUpdatedAmount(dto.getMiktar());
        amh.setOriginAddress(generateAddress(addressId));
        amh.setOriginUpdatedAmount(previousAmount);
        amh.setMovementType(AddressMovementType.DEFINITION);

        addressMovementHistoryRepository.save(amh);

    }

    public void saveAddressReplacementMovement(AurProductAddressReplacementDto dto,
                                               AurDepoUrunAdresStok targetAddress,
                                               Double previousTransferAddressAmount
                                              ){
        Long targetAddressId = dto.getNewUrunAdresId();
        Long transferAddressId = dto.getOldUrunAdresId();
        Double previousTargetAddressAmount = targetAddress.getMiktar();

        AddressMovementHistory addressMovementHistory = new AddressMovementHistory();
        addressMovementHistory.setAurUser(userService.getUser());
        addressMovementHistory.setStokKodu(dto.getStokKodu());
        addressMovementHistory.setBarcode(dto.getBarcode());
        addressMovementHistory.setProcessAmount(dto.getMiktar());
        addressMovementHistory.setPlacementAddress(generateAddress(targetAddressId));
        addressMovementHistory.setPlacementUpdatedAmount(previousTargetAddressAmount + dto.getMiktar());
        addressMovementHistory.setOriginAddress(generateAddress(transferAddressId));
        addressMovementHistory.setOriginUpdatedAmount(previousTransferAddressAmount - dto.getMiktar());
        addressMovementHistory.setMovementType(AddressMovementType.REPLACEMENT);

        addressMovementHistoryRepository.save(addressMovementHistory);
    }

    public AurDepoUrunAdres generateAddress(Long addressId){
        AurDepoUrunAdres aurDepoUrunAdres = new AurDepoUrunAdres();
        aurDepoUrunAdres.setUrunAdresId(addressId);
        return aurDepoUrunAdres;
    }

}
