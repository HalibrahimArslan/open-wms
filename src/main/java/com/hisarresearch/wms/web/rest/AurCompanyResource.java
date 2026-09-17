package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.ApiParameters;
import com.hisarresearch.wms.domain.AurCompany;
import com.hisarresearch.wms.repository.AurCompanyRepository;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurCompany}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AurCompanyResource {

    private final Logger log = LoggerFactory.getLogger(AurCompanyResource.class);

    private static final String ENTITY_NAME = "aurCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurCompanyRepository aurCompanyRepository;

    public AurCompanyResource(AurCompanyRepository aurCompanyRepository) {
        this.aurCompanyRepository = aurCompanyRepository;
    }

    /**
     * {@code POST  /aur-companies} : Create a new aurCompany.
     *
     * @param aurCompany the aurCompany to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurCompany, or with status {@code 400 (Bad Request)} if the aurCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-companies")
    public ResponseEntity<AurCompany> createAurCompany(@RequestBody AurCompany aurCompany) throws URISyntaxException {
        log.debug("REST request to save AurCompany : {}", aurCompany);
        if (aurCompany.getId() != null) {
            throw new BadRequestAlertException("A new aurCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AurCompany result = aurCompanyRepository.save(aurCompany);
        return ResponseEntity
            .created(new URI("/api/aur-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(withoutPassword(result));
    }

    /**
     * {@code PUT  /aur-companies/:id} : Updates an existing aurCompany.
     *
     * @param id the id of the aurCompany to save.
     * @param aurCompany the aurCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurCompany,
     * or with status {@code 400 (Bad Request)} if the aurCompany is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-companies/{id}")
    public ResponseEntity<AurCompany> updateAurCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurCompany aurCompany
    ) throws URISyntaxException {
        log.debug("REST request to update AurCompany : {}, {}", id, aurCompany);
        if (aurCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurCompany result = aurCompanyRepository.save(aurCompany);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurCompany.getId().toString()))
            .body(withoutPassword(result));
    }

    /**
     * {@code PATCH  /aur-companies/:id} : Partial updates given fields of an existing aurCompany, field will ignore if it is null
     *
     * @param id the id of the aurCompany to save.
     * @param aurCompany the aurCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurCompany,
     * or with status {@code 400 (Bad Request)} if the aurCompany is not valid,
     * or with status {@code 404 (Not Found)} if the aurCompany is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aur-companies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurCompany> partialUpdateAurCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurCompany aurCompany
    ) throws URISyntaxException {
        log.debug("REST request to partial update AurCompany partially : {}, {}", id, aurCompany);
        if (aurCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurCompany> result = aurCompanyRepository
            .findById(aurCompany.getId())
            .map(
                existingAurCompany -> {
                    if (aurCompany.getCompanyCode() != null) {
                        existingAurCompany.setCompanyCode(aurCompany.getCompanyCode());
                    }
                    if (aurCompany.getCompanyName() != null) {
                        existingAurCompany.setCompanyName(aurCompany.getCompanyName());
                    }
                    if (aurCompany.getErpType() != null) {
                        existingAurCompany.setErpType(aurCompany.getErpType());
                    }
                    if (aurCompany.getApiEndPoint() != null) {
                        existingAurCompany.setApiEndPoint(aurCompany.getApiEndPoint());
                    }
                    if (aurCompany.getApiParameters() != null) {
                        ApiParameters newApiParameters = aurCompany.getApiParameters();
                        ApiParameters existingApiParameters = existingAurCompany.getApiParameters();
                        if (!StringUtils.hasText(newApiParameters.getPassword()) && existingApiParameters != null) {
                            newApiParameters.setPassword(existingApiParameters.getPassword());
                        }
                        existingAurCompany.setApiParameters(newApiParameters);
                    }

                    return existingAurCompany;
                }
            )
            .map(aurCompanyRepository::save)
            .map(this::withoutPassword);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurCompany.getId().toString())
        );
    }

    /**
     * {@code GET  /aur-companies} : get all the aurCompanies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurCompanies in body.
     */
    @GetMapping("/aur-companies")
    public List<AurCompany> getAllAurCompanies() {
        log.debug("REST request to get all AurCompanies");
        return aurCompanyRepository.findAll().stream().map(this::withoutPassword).collect(Collectors.toList());
    }

    /**
     * {@code GET  /aur-companies/:id} : get the "id" aurCompany.
     *
     * @param id the id of the aurCompany to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurCompany, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aur-companies/{id}")
    public ResponseEntity<AurCompany> getAurCompany(@PathVariable Long id) {
        log.debug("REST request to get AurCompany : {}", id);
        Optional<AurCompany> aurCompany = aurCompanyRepository.findById(id).map(this::withoutPassword);
        return ResponseUtil.wrapOrNotFound(aurCompany);
    }

    /**
     * {@code DELETE  /aur-companies/:id} : delete the "id" aurCompany.
     *
     * @param id the id of the aurCompany to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-companies/{id}")
    public ResponseEntity<Void> deleteAurCompany(@PathVariable Long id) {
        log.debug("REST request to delete AurCompany : {}", id);
        aurCompanyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code source}, persistence context'te izlenen (managed) bir entity oldugu icin
     * uzerinde dogrudan {@code setApiParameters(...)} cagirmak Hibernate'in dirty-checking'ini
     * tetikler ve transaction commit olurken sifreyi DB'de yanlislikla null'a dusurur. Bu yuzden
     * REST yaniti icin hicbir zaman persist edilmeyecek, ayri/gecici bir kopya olusturulur.
     */
    private AurCompany withoutPassword(AurCompany source) {
        AurCompany masked = new AurCompany();
        masked.setId(source.getId());
        masked.setCompanyCode(source.getCompanyCode());
        masked.setCompanyName(source.getCompanyName());
        masked.setErpType(source.getErpType());
        masked.setApiEndPoint(source.getApiEndPoint());

        if (source.getApiParameters() != null) {
            ApiParameters maskedApiParameters = new ApiParameters();
            maskedApiParameters.setErpApiActive(source.getApiParameters().getErpApiActive());
            maskedApiParameters.setDepoNo(source.getApiParameters().getDepoNo());
            maskedApiParameters.setUsername(source.getApiParameters().getUsername());
            maskedApiParameters.setPassword(null);
            masked.setApiParameters(maskedApiParameters);
        }

        return masked;
    }
}
