package com.hisarresearch.wms.service;


import com.hisarresearch.wms.domain.AddressMovementHistory;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import com.hisarresearch.wms.domain.enumeration.AddressMovementType;
import com.hisarresearch.wms.repository.AddressMovementHistoryRepository;
import com.hisarresearch.wms.service.dto.ProductAddressSaveDTO;
import com.hisarresearch.wms.service.dto.productaddress.ProductAddressDefinitionDTO;
import com.hisarresearch.wms.service.dto.address.AurProductAddressReplacementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

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




    public List<AddressMovementHistory> getAllPlacementHistory(){
        return addressMovementHistoryRepository.findAll();
    }


    /**
     * Gecici adresten rafa yerlestirmeyi, stok hareketiyle ayni islem icinde yazar. Iki stok
     * kaydi da cagrildiginda guncellenmistir; bosalan gecici adres kaydi pasife cekildigi
     * icin kalan miktar 0 yazilir.
     */
    public void saveTemporaryPlacementMovement(ProductAddressSaveDTO dto,
                                               AurDepoUrunAdresStok temporaryStock,
                                               AurDepoUrunAdresStok placementStock) {
        AddressMovementHistory amh = new AddressMovementHistory();
        amh.setAurUser(userService.getUser());
        amh.setStokKodu(dto.getStokKod());
        amh.setBarcode(dto.getBarcode());
        amh.setProcessAmount(dto.getMiktar());
        amh.setOriginAddress(generateAddress(temporaryStock.getUrunAdresId()));
        amh.setOriginUpdatedAmount(Boolean.TRUE.equals(temporaryStock.getStatus()) ? temporaryStock.getMiktar() : 0.0);
        amh.setPlacementAddress(generateAddress(placementStock.getUrunAdresId()));
        amh.setPlacementUpdatedAmount(placementStock.getMiktar());
        amh.setMovementType(AddressMovementType.PLACEMENT_FROM_TMP);

        addressMovementHistoryRepository.save(amh);
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

    /**
     * Stok kayitlari ayni islemde yonetilen entity'ler oldugu icin, cagrildiginda ikisi de
     * zaten guncellenmistir; guncel miktarlar oldugu gibi yazilir.
     */
    public void saveAddressReplacementMovement(AurProductAddressReplacementDto dto,
                                               AurDepoUrunAdresStok targetAddress,
                                               AurDepoUrunAdresStok transferAddress
                                              ){
        Long targetAddressId = dto.getNewUrunAdresId();
        Long transferAddressId = dto.getOldUrunAdresId();

        AddressMovementHistory addressMovementHistory = new AddressMovementHistory();
        addressMovementHistory.setAurUser(userService.getUser());
        addressMovementHistory.setStokKodu(dto.getStokKodu());
        addressMovementHistory.setBarcode(dto.getBarcode());
        addressMovementHistory.setProcessAmount(dto.getMiktar());
        addressMovementHistory.setPlacementAddress(generateAddress(targetAddressId));
        addressMovementHistory.setPlacementUpdatedAmount(targetAddress.getMiktar());
        addressMovementHistory.setOriginAddress(generateAddress(transferAddressId));
        addressMovementHistory.setOriginUpdatedAmount(transferAddress.getMiktar());
        addressMovementHistory.setMovementType(AddressMovementType.REPLACEMENT);

        addressMovementHistoryRepository.save(addressMovementHistory);
    }

    public AurDepoUrunAdres generateAddress(Long addressId){
        AurDepoUrunAdres aurDepoUrunAdres = new AurDepoUrunAdres();
        aurDepoUrunAdres.setUrunAdresId(addressId);
        return aurDepoUrunAdres;
    }

}
