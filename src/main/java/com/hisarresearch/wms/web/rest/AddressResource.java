package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurOrderAdres;
import com.hisarresearch.wms.domain.address.*;
import com.hisarresearch.wms.repository.address.*;
import com.hisarresearch.wms.service.*;
import com.hisarresearch.wms.service.criteria.AurDepoUrunAdresCriteria;
import com.hisarresearch.wms.service.dto.address.*;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.business.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping("/api")
@Transactional
public class AddressResource {

    private static final String ENTITY_NAME = "address";
    private final Logger log = LoggerFactory.getLogger(AddressResource.class);

    private final UserService userService;

    private final AurDepoAdresRepository aurDepoAdresRepository;

    private final AddressService addressService;

    private final AddressQueryService addressQueryService;

    private final TranslationService translationService;



    public AddressResource(AurDepoAdresRepository aurDepoAdresRepository,
                           AddressService addressService, AddressQueryService addressQueryService,
                           UserService userService, TranslationService translationService) {
        this.aurDepoAdresRepository = aurDepoAdresRepository;
        this.addressService = addressService;
        this.addressQueryService = addressQueryService;
        this.userService = userService;
        this.translationService = translationService;
    }

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @GetMapping("/aur-adres-tanim-bulk")
    public List<AurDepoUrunAdres> getAddressList() {
        log.debug("REST request to get all address list");
        return addressService.getAddressList();
    }

    @GetMapping("/address-list")
    public ResponseEntity<List<AurDepoUrunAdres>> getAddressListByCriteria(AurDepoUrunAdresCriteria aurDepoUrunAdresCriteria, Pageable pageable) {
        log.debug("REST request to get all address definition list");
        Page<AurDepoUrunAdres> page = addressQueryService.findByCriteria(aurDepoUrunAdresCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/address-list/count")
    public ResponseEntity<Long> countAddressList(AurDepoUrunAdresCriteria criteria) {
        log.debug("REST request to count address list by criteria: {}", criteria);
        return ResponseEntity.ok().body(addressQueryService.countByCriteria(criteria));
    }

    @GetMapping("/aur-adres-depo-tanim")
    public ResponseEntity<List<AurDepoUrunAdres>> getAddressDefinition(Pageable pageable) {
        log.debug("REST request to get all address list page by page");
        final Page<AurDepoUrunAdres> page = addressService.getAddressList(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/aur-depo-adres-tanim/{address}")
    public Long getAddressDefinitionByAddress(@PathVariable String address) {
        Long addressDefinition = addressService.findByAddress(address).getUrunAdresId();

        if (addressDefinition != null) {
            return addressDefinition;
        } else {
            throw new RuntimeException("Kayıt Bulunamadı");
        }
    }

    @GetMapping("/address-by-depo-code/{address}/{depoCode}")
    public Long getAddressDefinitionByAddressAndDepoCode(@PathVariable String address, @PathVariable String depoCode) {
        Optional<AurDepoUrunAdres> desiredAddress = addressService.findByAddressAndDepoCode(address, depoCode);

        if (desiredAddress.isPresent()) {
            return desiredAddress.get().getUrunAdresId();
        } else {
            String message = translationService.getErrorMessage("address.notFound.warehouse",address,depoCode);
            throw new BusinessException(message, ENTITY_NAME, "address.notFound.warehouse");
        }
    }

    @GetMapping("/aur-depo-gecici-adres/{depoCode}")
    public List<AurDepoUrunAdres> getTemporaryAddressDefinitions(@PathVariable String depoCode) {
        log.debug("REST request to get all temporary addresses");
        Integer companyCode = userService.getUserCompanyCode();
        return addressService.findByDepoNoAndCompanyCodeAndGeciciAdres(depoCode, companyCode.toString(), true);
    }

    @GetMapping("/aur-depo-kontrol-adres/{depoCode}")
    public List<AurDepoUrunAdres> getControlAddressDefinitions(@PathVariable String depoCode) {
        log.debug("REST request to get all control addresses");
        Integer companyCode = userService.getUserCompanyCode();
        return addressService.findByDepoNoAndCompanyCodeAndKontrolAdres(depoCode, companyCode.toString(), true);
    }

    @PatchMapping(value = "/aur-depo-address-bulk", consumes = "application/merge-patch+json")
    public ResponseEntity<AddressUpdateResponseDTO> partialUpdateBulk(@RequestBody List<AurDepoUrunAdres> addresses) {
        log.debug("REST request to partial update AurDepoUrunAdres List partially : {}", addresses);
        return ResponseEntity.ok().body(addressService.partialBulkUpdate(addresses));
    }

    @PatchMapping(value = "/aur-depo-address/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurDepoUrunAdres> partialUpdateAurDepoAdres(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurDepoUrunAdres aurDepoUrunAdres
    ) {
        log.debug("REST request to partial update AurDepoUrunAdres partially : {}, {}", id, aurDepoUrunAdres);
        if (aurDepoUrunAdres.getUrunAdresId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurDepoUrunAdres.getUrunAdresId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!aurDepoAdresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (aurDepoUrunAdres.getGeciciAdres() && aurDepoUrunAdres.getToplamaGozu()) {
            throw new BadRequestAlertException("Toplama Gozu ile gecici adres aynı anda aktif olamaz", ENTITY_NAME, "tmpvspickingaddress");
        }

        Optional<AurDepoUrunAdres> result = addressService.partialUpdate(aurDepoUrunAdres);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurDepoUrunAdres.getUrunAdresId().toString())
        );
    }

    @GetMapping("/empty-addresses/{warehouse}")
    public ResponseEntity<EmptyAddressDTO> getEmptyAddressList(@PathVariable String warehouse, @RequestParam(required = false) String rayon) {
        log.debug("REST request to get empty address list");
        if(Objects.isNull(rayon)) {
            return ResponseEntity.ok(addressService.getEmptyAddressList(warehouse));
        }

        return ResponseEntity.ok(addressService.getEmptyAddressListByRayon(warehouse,rayon));
    }

    @PostMapping("/address")
    public ResponseEntity<AurDepoUrunAdres> createAddress(@RequestBody AddressDTO addressDTO) throws URISyntaxException {
        log.debug("REST request to save Address : {}", addressDTO);
        if (addressDTO.getId() != null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idexists");
        }
        AurDepoUrunAdres newAddress = addressService.createAddress(addressDTO);
        return ResponseEntity
            .created(new URI("/api/address" + newAddress.getAdres()))
            .headers(HeaderUtil.createAlert(applicationName, "address.created", newAddress.getAdres()))
            .body(newAddress);
    }

    @PostMapping("/address-bulk")
    public ResponseEntity<List<AddressDTO>> createAddressBulk(@Valid @RequestBody AddressCreateBulkDTO addressCreateBulkDTO) {
        log.debug("REST request to save Address Bulk : {}", addressCreateBulkDTO);

        List<AddressDTO> newAddresses = addressService.createAddressBulk(addressCreateBulkDTO);
        return ResponseEntity.ok().body(newAddresses);
    }

    /**
     * {@code GET  /flow-department} : flow departments from address table to aur_address_bolum.
     */
    @GetMapping("/flow-departments")
    public void flowDepartments() {
        log.debug("REST request to transfer departments of address to own table");
        addressService.flowDepartments();
    }

    /**
     * {@code GET  /flow-halls} : flow halls from address table to aur_address_reyon.
     */
    @GetMapping("/flow-halls")
    public void flowHalls() {
        log.debug("REST request to transfer halls of address to own table");
        addressService.flowHalls();
    }

    /**
     * {@code GET  /flow-units} : flow units from address table to aur_address_unite.
     */
    @GetMapping("/flow-units")
    public void flowUnits() {
        log.debug("REST request to transfer units of address to own table");
        addressService.flowUnits();
    }

    /**
     * {@code GET  /flow-flat} : flow flats from address table to aur_address_kat.
     */
    @GetMapping("/flow-flats")
    public void flowFlats() {
        log.debug("REST request to transfer flats of address to own table");
        addressService.flowFlats();
    }

    /**
     * {@code GET  /flow-rooms} : flow rooms from address table to aur_address_oda.
     */
    @GetMapping("/flow-rooms")
    public void flowRooms() {
        log.debug("REST request to transfer rooms of address to own table");
        addressService.flowRooms();
    }

    /**
     * {@code GET  /flow-address-types} : flow address types from address table to aur_address_tip.
     */
    @GetMapping("/flow-address-types")
    public void flowAddressTypes() {
        log.debug("REST request to transfer address types of address to own table");
        addressService.flowAddressTypes();
    }

    /**
     * {@code DELETE  /address/:id} : delete address by id.
     */
    @DeleteMapping("/address/{id}")
    public void deleteAddress(@PathVariable long id) {
        log.debug("REST request to delete Address : {}", id);
        addressService.deleteAddress(id);
    }

    @PostMapping("/order-adres")
    public String orderAdres(@RequestBody List<AurOrderAdres> orderAdres){
        addressService.orderAdres(orderAdres);
        return "success";
    }


}
