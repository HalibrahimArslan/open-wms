package com.hisarresearch.wms.service;


import com.hisarresearch.wms.domain.AurGrupMailAdres;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.repository.AurGrupMailAdresRepository;
import com.hisarresearch.wms.service.dto.AurGrupMailAdresDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AurGroupMailAddressService {

    private static final String ENTITY_NAME = "AurGroupMailAddress";

    private final AurGrupMailAdresRepository aurGrupMailAdresRepository;

    public AurGroupMailAddressService(AurGrupMailAdresRepository aurGrupMailAdresRepository) {
        this.aurGrupMailAdresRepository = aurGrupMailAdresRepository;
    }


    @Transactional(readOnly = true)
    public Optional<AurGrupMailAdres> findByGrupKodu(Integer grupKodu){
        return aurGrupMailAdresRepository.findByGrupKodu(grupKodu);
    }

    public List<AurGrupMailAdres> getAllGrupMailAdres(){
        return aurGrupMailAdresRepository.findAll();
    }

    public AurGrupMailAdres saveGrupMailAdres(AurGrupMailAdresDto aurGrupMailAdresDto){
        AurGrupMailAdres agma = new AurGrupMailAdres();

        Optional<AurGrupMailAdres> oagma = aurGrupMailAdresRepository.findByGrupKodu(aurGrupMailAdresDto.getGrupKodu());
        if (oagma.isEmpty()) {
            agma.setMailAdres(aurGrupMailAdresDto.getMailAdres());
            agma.setGrupKodu(aurGrupMailAdresDto.getGrupKodu());
            return aurGrupMailAdresRepository.save(agma);
        }
        throw new BusinessException("Bu Mail adresine Kayitli bir GrupKodu Var",ENTITY_NAME);
    }

    public AurGrupMailAdres updateGroupMailAdres(AurGrupMailAdresDto aurGrupMailAdresDto){

        Optional<AurGrupMailAdres> oagma = aurGrupMailAdresRepository.findById(aurGrupMailAdresDto.getId());
        if (oagma.isPresent()) {
            oagma.get().setMailAdres(aurGrupMailAdresDto.getMailAdres());
            oagma.get().setGrupKodu(aurGrupMailAdresDto.getGrupKodu());
            return oagma.get();
        }
        throw new BusinessException("Güncellenecek bir Mail bulunamadi",ENTITY_NAME);
    }

    public void deleteById(Long id){
        aurGrupMailAdresRepository.deleteById(id);
    }
}
