package com.hisarresearch.wms.service;


import com.hisarresearch.wms.domain.enumeration.AurTmpSevkPlaniStatus;
import com.hisarresearch.wms.domain.sevkplani.AurTmpSevkPlani;
import com.hisarresearch.wms.repository.AurTmpSevkPlaniRepository;
import com.hisarresearch.wms.service.dto.AurTmpSevkPlaniDTO;
import com.hisarresearch.wms.service.mapper.AurTmpSevkPlaniMapper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class AurTmpSevkPlaniService {

    private final AurTmpSevkPlaniRepository repository;

    private AurTmpSevkPlaniMapper mapper;

    public AurTmpSevkPlaniService(AurTmpSevkPlaniRepository repository,  AurTmpSevkPlaniMapper map) {
        this.repository = repository;
        this.mapper = map;
    }

    public AurTmpSevkPlaniDTO findById(Long id) {
        AurTmpSevkPlani sevkPlani = repository.findById(id).orElseThrow(()->new RuntimeException("Plan bulunamadı"));
        return mapper.toDto(sevkPlani);
    }

    public List<AurTmpSevkPlani> findAll() {
        return repository.findAll();
    }

    public AurTmpSevkPlani save(AurTmpSevkPlaniDTO sevkPlaniDTO) {
        // Berechnung der Woche (ISO-Standard)
        int currentWeek = LocalDate.now().get(WeekFields.ISO.weekOfYear());
        sevkPlaniDTO.setWeek(currentWeek);
        sevkPlaniDTO.setStatus(AurTmpSevkPlaniStatus.CREATED);

        // Überprüfen der Einzigartigkeit von sipUid + week
        boolean exists = repository.existsBySipUidAndWeek(sevkPlaniDTO.getSipUid(), sevkPlaniDTO.getWeek());
        if (exists) {
            throw new BadRequestAlertException("Ein Eintrag mit dieser sipUid und Woche existiert bereits.", "AurTmpSevkPlanı", "uniqueconstraintviolation");
        }

        // Neue Version verwalten
        sevkPlaniDTO.setVersion(sevkPlaniDTO.getVersion() + 1);

        AurTmpSevkPlani sevkPlani = mapper.toEntity(sevkPlaniDTO);
        return repository.save(sevkPlani);
    }
    @Transactional
    public List<AurTmpSevkPlani> saveAll(List<AurTmpSevkPlaniDTO> dtoList) {
        // Berechnung der Woche (ISO-Standard) und Zuordnung der Werte
        List<AurTmpSevkPlani> sevkPlani = dtoList.stream()
            .map(dto -> {
                int currentWeek = LocalDate.now().get(WeekFields.ISO.weekOfYear());
                dto.setWeek(currentWeek);
                dto.setStatus(AurTmpSevkPlaniStatus.CREATED);
                AurTmpSevkPlani entity = mapper.toEntity(dto);

                // Einzigartigkeit prüfen
                if (repository.existsBySipUidAndWeek(dto.getSipUid(), dto.getWeek())) {
                    throw new BadRequestAlertException(
                        "Yoladiniz dosyanin icindeki sipUid ve Hafta DB var bile.",
                        "AurTmpSevkPlani",
                        "uniqueconstraintviolation"
                    );
                }

                return entity;
            })
            .collect(Collectors.toList());

        // Batch-Speicherung
        return repository.saveAll(sevkPlani);
    }
}
