package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurMailAddTo;
import com.hisarresearch.wms.repository.AurMailAddToRepository;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AurMailAddToService {
    private final Logger log = LoggerFactory.getLogger(AurMailAddToService.class);

    private final AurMailAddToRepository aurMailAddToRepository;

    private final TranslationService translationService;

    public AurMailAddToService(AurMailAddToRepository aurMailAddToRepository,TranslationService translationService) {
        this.aurMailAddToRepository = aurMailAddToRepository;
        this.translationService = translationService;
    }

    @Transactional(readOnly = true)
    public List<AurMailAddTo> findAll() {
        return aurMailAddToRepository.findAll();
    }

    public List<AurMailAddTo> findByCompanyCode(int companyCode) {
        log.debug("Get mail list by company code: {}", companyCode);
        return aurMailAddToRepository.findByCompanyCode(companyCode);
    }

    public AurMailAddTo save(AurMailAddTo aurMailAddTo) {
        log.debug("Save mail : {}", aurMailAddTo);
        Optional<AurMailAddTo> searchMailAddress = aurMailAddToRepository.findByCompanyCodeAndMailAdres(aurMailAddTo.getCompanyCode(), aurMailAddTo.getMailAdres());
        if (searchMailAddress.isPresent()) {
            throw new BadRequestAlertException(translationService.getErrorMessage("mailAddTo.invalidMail"),"AurMailAddTo","mailExists");
        }
        return aurMailAddToRepository.save(aurMailAddTo);
    }

    public void deleteById(long id){
        log.debug("Delete mail : {}", id);
        aurMailAddToRepository.deleteById(id);
    }

}
