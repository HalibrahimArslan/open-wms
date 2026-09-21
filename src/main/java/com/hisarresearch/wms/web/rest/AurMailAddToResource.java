package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurMailAddTo;
import com.hisarresearch.wms.service.AurMailAddToService;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AurMailAddToResource {
    private final Logger log = LoggerFactory.getLogger(AurMailAddToResource.class);

    private static final String ENTITY_NAME = "auraMailAddTo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurMailAddToService aurMailAddToService;

    public AurMailAddToResource(AurMailAddToService aurMailAddToService) {
        this.aurMailAddToService = aurMailAddToService;
    }

    /**
     * {@code GET  /public-mails} : get all the public mails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aur-integration-logs in body.
     */
    @GetMapping("/public-mails/{companyCode}")
    public ResponseEntity<List<AurMailAddTo>> getPublicMails(@PathVariable int companyCode) {
        log.debug("REST request to get AurMailAddTo company: {}", companyCode);
        List<AurMailAddTo> mails = aurMailAddToService.findByCompanyCode(companyCode);
        return ResponseEntity.ok().body(mails);
    }


    /**
     * {@code POST  /public-mail} : save public mail.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (CREATED)} and the created mail in body.
     */
    @PostMapping("/public-mail")
    public ResponseEntity<AurMailAddTo> savePublicMail(@RequestBody AurMailAddTo aurMailAddTo) throws URISyntaxException {
        log.debug("REST request to save AurMailAddTo : {}", aurMailAddTo);
        if(aurMailAddTo.getId() != null){
            throw new BadRequestAlertException("A new mail cannot already have an ID", "publicMailAddress", "idexists");
        }
        AurMailAddTo newMail = aurMailAddToService.save(aurMailAddTo);
        return ResponseEntity
            .created(new URI("/api/public-mail" + aurMailAddTo.getMailAdres()))
            .headers(HeaderUtil.createAlert(applicationName, "publicMail.created", newMail.getMailAdres()))
            .body(newMail);
    }

    /**
     * {@code DELETE  /id} : delete public mail by id.
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/public-mail/{id}")
    public ResponseEntity<Void> deletePublicMail(@PathVariable long id) {
        log.debug("REST request to delete AurMailAddTo : {}", id);
        aurMailAddToService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
