package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.Customer;
import com.hisarresearch.wms.service.CustomerService;
import com.hisarresearch.wms.service.dto.CustomerDTO;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class CustomerResource {
    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "customer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * {@code GET /customers/:companyCode} : get customers of company
     *
     * @param companyCode company code of warehouse
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all customers.
     */
    @GetMapping("/customers/{companyCode}")
    public ResponseEntity<List<Customer>> getCustomers(@PathVariable String companyCode) {
        log.debug("REST request to get customer list for company : {}", companyCode);
        List<Customer> customers = customerService.getByCompanyCode(companyCode);
        return ResponseEntity.ok().body(customers);
    }

    /**
     * {@code POST /customer} : create new customer
     *
     * @return the {@link ResponseEntity} with status {@code 201 (CREATED)} and with body new customer.
     */
    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to create Customer {}", customerDTO);
        if (customerDTO.getId() != null) {
            throw new BadRequestAlertException("A new Customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Customer newCustomer = customerService.save(customerDTO);
        return ResponseEntity
            .created(new URI("/api/customer/" + newCustomer.getMail()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, newCustomer.getMail()))
            .body(newCustomer);
    }

    /**
     * {@code DELETE /customer} : delete customer
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)} .
     */
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete VendorMailAddress : {}", id);
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
