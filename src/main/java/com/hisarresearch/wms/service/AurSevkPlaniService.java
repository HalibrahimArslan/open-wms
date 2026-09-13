package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.sevkplani.AurSevkPlani;
import com.hisarresearch.wms.repository.AurSevkPlaniRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AurSevkPlaniService {

    private final AurSevkPlaniRepository repository;

    public AurSevkPlaniService(AurSevkPlaniRepository repository) {
        this.repository = repository;
    }

    public List<AurSevkPlani> findAll() {
        return repository.findAll();
    }

    public Optional<AurSevkPlani> findById(Long id) {
        return repository.findById(id);
    }

    public AurSevkPlani save(AurSevkPlani plan) {
        return repository.save(plan);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
