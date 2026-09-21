package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.address.*;
import com.hisarresearch.wms.service.AddressComponentService;
import com.hisarresearch.wms.service.dto.address.components.*;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;
import com.hisarresearch.wms.framework.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing the address components.
 */
@RestController
@RequestMapping("/api")
public class AddressComponentResource {
    private final Logger log = LoggerFactory.getLogger(AddressComponentResource.class);

    private static final String ENTITY_NAME = "addressComponent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AddressComponentService addressComponentService;

    public AddressComponentResource(AddressComponentService addressComponentService) {
        this.addressComponentService = addressComponentService;
    }


    /**
     * {@code GET  /department} : get address departments.
     * @param companyCode user's companyCode
     * @param depoCode related depoCode
     * @return list of address departments
     */
    @GetMapping("/departments/{companyCode}/{depoCode}")
    public List<AddressDepartment> getAddressDepartments(@PathVariable String companyCode, @PathVariable String depoCode) {
        log.debug("REST request to get address departments for {} and {}", companyCode,depoCode);
        return addressComponentService.getAddressDepartment(companyCode,depoCode);
    }

    /**
     * {@code GET  /halls} : get address halls.
     * @param companyCode user's companyCode
     * @param depoCode related depoCode
     * @return list of address halls
     */
    @GetMapping("/halls/{companyCode}/{depoCode}")
    public List<AddressHall> getAddressHalls(@PathVariable String companyCode, @PathVariable String depoCode) {
        log.debug("REST request to get address halls for {} and {}", companyCode,depoCode);
        return addressComponentService.getAddressHalls(companyCode,depoCode);
    }

    /**
     * {@code GET  /units} : get address units.
     * @param companyCode user's companyCode
     * @param depoCode related depoCode
     * @return list of address units
     */
    @GetMapping("/units/{companyCode}/{depoCode}")
    public List<AddressUnit> getAddressUnits(@PathVariable String companyCode, @PathVariable String depoCode) {
        log.debug("REST request to get address units for {} and {}", companyCode,depoCode);
        return addressComponentService.getAddressUnits(companyCode,depoCode);
    }

    /**
     * {@code GET  /flats} : get address flats.
     * @param companyCode user's companyCode
     * @param depoCode related depoCode
     * @return list of address flats
     */
    @GetMapping("/flats/{companyCode}/{depoCode}")
    public List<AddressFlat> getAddressFlats(@PathVariable String companyCode, @PathVariable String depoCode) {
        log.debug("REST request to get address flats for {} and {}", companyCode,depoCode);
        return addressComponentService.getAddressFlat(companyCode,depoCode);
    }

    /**
     * {@code GET  /rooms} : get address rooms.
     * @param companyCode user's companyCode
     * @param depoCode related depoCode
     * @return list of address rooms
     */
    @GetMapping("/rooms/{companyCode}/{depoCode}")
    public List<AddressRoom> getAddressRooms(@PathVariable String companyCode, @PathVariable String depoCode) {
        log.debug("REST request to get address rooms for {} and {}", companyCode,depoCode);
        return addressComponentService.getAddressRooms(companyCode,depoCode);
    }

    /**
     * {@code GET  /address-types} : get address types.
     * @param companyCode user's companyCode
     * @param depoCode related depoCode
     * @return list of address types
     */
    @GetMapping("/address-types/{companyCode}/{depoCode}")
    public List<AddressType> getAddressTypes(@PathVariable String companyCode, @PathVariable String depoCode) {
        log.debug("REST request to get address types for {} and {}", companyCode,depoCode);
        return addressComponentService.getAddressTypes(companyCode,depoCode);
    }

    /**
     * {@code POST  /department} : save address department.
     * @param addressDepartmentDTO the address department to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new address department
     */
    @PostMapping("/department")
    public ResponseEntity<AddressDepartmentDTO> saveAddressDepartment(@RequestBody @Valid AddressDepartmentDTO addressDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to save address department {}", addressDepartmentDTO);
        if (addressDepartmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new address department cannot already have an ID", "addressDepartment", "idexists");
        }
        AddressDepartmentDTO createdOne =  addressComponentService.saveAddressDepartment(addressDepartmentDTO);
        return ResponseEntity
            .created(new URI("/api/department" + createdOne.getCode()))
            .headers(HeaderUtil.createAlert(applicationName, "addressDepartment.created", createdOne.getCode()))
            .body(createdOne);
    }

    /**
     * {@code POST  /hall} : save address hall.
     * @param addressHallDTO the address hall to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new address hall
     */
    @PostMapping("/hall")
    public ResponseEntity<AddressHallDTO> saveAddressHall(@RequestBody @Valid AddressHallDTO addressHallDTO) throws URISyntaxException {
        log.debug("REST request to save address hall {}", addressHallDTO);
        if (addressHallDTO.getId() != null) {
            throw new BadRequestAlertException("A new address hall cannot already have an ID", "addressHall", "idexists");
        }
        AddressHallDTO createdOne =  addressComponentService.saveAddressHall(addressHallDTO);
        return ResponseEntity
            .created(new URI("/api/hall" + createdOne.getCode()))
            .headers(HeaderUtil.createAlert(applicationName, "addressHall.created", createdOne.getCode()))
            .body(createdOne);
    }

    /**
     * {@code POST  /unit} : save address unit.
     * @param addressUnitDTO the address unit to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new address unit
     */
    @PostMapping("/unit")
    public ResponseEntity<AddressUnitDTO> saveAddressUnit(@RequestBody @Valid AddressUnitDTO addressUnitDTO) throws URISyntaxException {
        log.debug("REST request to save address unit {}", addressUnitDTO);
        if (addressUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new address unit cannot already have an ID", "addressUnit", "idexists");
        }
        AddressUnitDTO createdOne =  addressComponentService.saveAddressUnit(addressUnitDTO);
        return ResponseEntity
            .created(new URI("/api/unit" + createdOne.getCode()))
            .headers(HeaderUtil.createAlert(applicationName, "addressUnit.created", createdOne.getCode()))
            .body(createdOne);
    }

    /**
     * {@code POST  /flat} : save address flat.
     * @param addressFlatDTO the address flat to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new address flat
     */
    @PostMapping("/flat")
    public ResponseEntity<AddressFlatDTO> saveAddressFlat(@RequestBody @Valid AddressFlatDTO addressFlatDTO) throws URISyntaxException {
        log.debug("REST request to save address flat {}", addressFlatDTO);
        if (addressFlatDTO.getId() != null) {
            throw new BadRequestAlertException("A new address flat cannot already have an ID", "addressFlat", "idexists");
        }
        AddressFlatDTO createdOne =  addressComponentService.saveAddressFlat(addressFlatDTO);
        return ResponseEntity
            .created(new URI("/api/flat" + createdOne.getCode()))
            .headers(HeaderUtil.createAlert(applicationName, "addressFlat.created", createdOne.getCode()))
            .body(createdOne);
    }

    /**
     * {@code POST  /room} : save address room.
     * @param addressRoomDTO the address room to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new address room
     */
    @PostMapping("/room")
    public ResponseEntity<AddressRoomDTO> saveAddressRoom(@RequestBody @Valid AddressRoomDTO addressRoomDTO) throws URISyntaxException {
        log.debug("REST request to save address room {}", addressRoomDTO);
        if (addressRoomDTO.getId() != null) {
            throw new BadRequestAlertException("A new address room cannot already have an ID", "addressRoom", "idexists");
        }
        AddressRoomDTO createdOne =  addressComponentService.saveAddressRoom(addressRoomDTO);
        return ResponseEntity
            .created(new URI("/api/room" + createdOne.getCode()))
            .headers(HeaderUtil.createAlert(applicationName, "addressRoom.created", createdOne.getCode()))
            .body(createdOne);
    }


    /**
     * {@code POST  /address-type} : save address type.
     * @param addressTypeDTO the address type to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new address type
     */
    @PostMapping("/address-type")
    public ResponseEntity<AddressTypeDTO> saveAddressRoom(@RequestBody @Valid AddressTypeDTO addressTypeDTO) throws URISyntaxException {
        log.debug("REST request to save address type {}", addressTypeDTO);
        if (addressTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new address type cannot already have an ID", "addressType", "idexists");
        }
        AddressTypeDTO createdOne =  addressComponentService.saveAddressType(addressTypeDTO);
        return ResponseEntity
            .created(new URI("/api/address-type" + createdOne.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "addressType.created", createdOne.getCode()))
            .body(createdOne);
    }

    /**
     * {@code PUT  /department} : update existing address department.
     * @param addressDepartmentDTO the address department to update
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the updated address department
     */
    @PutMapping("/department")
    public ResponseEntity<AddressDepartmentDTO> updateAddressDepartment(@RequestBody @Valid AddressDepartmentDTO addressDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to update address department {}", addressDepartmentDTO);
        if (addressDepartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Updated department has an ID", "addressDepartment", "idNotFound");
        }
        Optional<AddressDepartmentDTO> updatedAddressDepartment =  addressComponentService.updateAddressDepartment(addressDepartmentDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedAddressDepartment,
            HeaderUtil.createAlert(applicationName, "addressDepartment.updated", addressDepartmentDTO.getCode())
        );
    }

    /**
     * {@code PUT  /hall} : update existing address hall.
     * @param addressHallDTO the address hall to update
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the updated address hall
     */
    @PutMapping("/hall")
    public ResponseEntity<AddressHallDTO> updateAddressHall(@RequestBody @Valid AddressHallDTO addressHallDTO) throws URISyntaxException {
        log.debug("REST request to update address hall {}", addressHallDTO);
        if (addressHallDTO.getId() == null) {
            throw new BadRequestAlertException("Updated hall has an ID", "addressHall", "idNotFound");
        }
        Optional<AddressHallDTO> updatedAddressHall =  addressComponentService.updateAddressHall(addressHallDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedAddressHall,
            HeaderUtil.createAlert(applicationName, "addressHall.updated", addressHallDTO.getCode())
        );
    }

    /**
     * {@code PUT  /unit} : update existing address unit.
     * @param addressUnitDTO the address unit to update
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the updated address unit
     */
    @PutMapping("/unit")
    public ResponseEntity<AddressUnitDTO> updateAddressUnit(@RequestBody @Valid AddressUnitDTO addressUnitDTO) throws URISyntaxException {
        log.debug("REST request to update address unit {}", addressUnitDTO);
        if (addressUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Updated unit has an ID", "addressUnit", "idNotFound");
        }
        Optional<AddressUnitDTO> updatedAddressUnit =  addressComponentService.updateAddressUnit(addressUnitDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedAddressUnit,
            HeaderUtil.createAlert(applicationName, "addressUnit.updated", addressUnitDTO.getCode())
        );
    }


    /**
     * {@code PUT  /flat} : update existing address flat.
     * @param addressFlatDTO the address flat to update
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the updated address flat
     */
    @PutMapping("/flat")
    public ResponseEntity<AddressFlatDTO> updateAddressFlat(@RequestBody @Valid AddressFlatDTO addressFlatDTO) throws URISyntaxException {
        log.debug("REST request to update address flat {}", addressFlatDTO);
        if (addressFlatDTO.getId() == null) {
            throw new BadRequestAlertException("Updated unit has an ID", "addressFlat", "idNotFound");
        }
        Optional<AddressFlatDTO> updatedAddressFlat =  addressComponentService.updateAddressFlat(addressFlatDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedAddressFlat,
            HeaderUtil.createAlert(applicationName, "addressFlat.updated", addressFlatDTO.getCode())
        );
    }

    /**
     * {@code PUT  /room} : update existing address room.
     * @param addressRoomDTO the address room to update
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the updated address room
     */
    @PutMapping("/room")
    public ResponseEntity<AddressRoomDTO> updateAddressFlat(@RequestBody @Valid AddressRoomDTO addressRoomDTO) throws URISyntaxException {
        log.debug("REST request to update address room {}", addressRoomDTO);
        if (addressRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Updated room has an ID", "addressRoom", "idNotFound");
        }
        Optional<AddressRoomDTO> updatedAddressRoom =  addressComponentService.updateAddressRoom(addressRoomDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedAddressRoom,
            HeaderUtil.createAlert(applicationName, "addressRoom.updated", addressRoomDTO.getCode())
        );
    }

    /**
     * {@code PUT  /address-type} : update existing address-type.
     * @param addressTypeDTO the address-type to update
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the updated address-type
     */
    @PutMapping("/address-type")
    public ResponseEntity<AddressTypeDTO> updateAddressFlat(@RequestBody @Valid AddressTypeDTO addressTypeDTO) throws URISyntaxException {
        log.debug("REST request to update address type {}", addressTypeDTO);
        if (addressTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Updated address type has an ID", "addressType", "idNotFound");
        }
        Optional<AddressTypeDTO> updatedAddressType =  addressComponentService.updateAddressType(addressTypeDTO);
        return ResponseUtil.wrapOrNotFound(
            updatedAddressType,
            HeaderUtil.createAlert(applicationName, "addressType.updated", addressTypeDTO.getCode())
        );
    }

    /**
     * {@code DELETE /department} : delete department by id.
     *
     * @param id the id of the department to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/department/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.debug("REST request to delete Address Department: {}", id);
        addressComponentService.deleteAddressDepartment(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "addressDepartment.deleted", String.valueOf(id))).build();
    }

    /**
     * {@code DELETE /hall} : delete hall by id.
     *
     * @param id the id of the hall to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hall/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        log.debug("REST request to delete Address Hall: {}", id);
        addressComponentService.deleteAddressHall(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "addressHall.deleted", String.valueOf(id))).build();
    }


    /**
     * {@code DELETE /unit} : delete unit by id.
     *
     * @param id the id of the unit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        log.debug("REST request to delete Address Unit: {}", id);
        addressComponentService.deleteAddressUnit(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "addressUnit.deleted", String.valueOf(id))).build();
    }


    /**
     * {@code DELETE /flat} : delete flat by id.
     *
     * @param id the id of the flat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flat/{id}")
    public ResponseEntity<Void> deleteFlat(@PathVariable Long id) {
        log.debug("REST request to delete Address Flat: {}", id);
        addressComponentService.deleteAddressFlat(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "addressFlat.deleted", String.valueOf(id))).build();
    }


    /**
     * {@code DELETE /room} : delete room by id.
     *
     * @param id the id of the room to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.debug("REST request to delete Address Room: {}", id);
        addressComponentService.deleteAddressRoom(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "addressRoom.deleted", String.valueOf(id))).build();
    }


    /**
     * {@code DELETE /address-type} : delete address-type by id.
     *
     * @param id the id of the address-type to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/address-type/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete Address Type: {}", id);
        addressComponentService.deleteAddressType(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "addressType.deleted", String.valueOf(id))).build();
    }


}
