package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.Authority;
import com.hisarresearch.wms.security.AuthoritiesConstants;
import com.hisarresearch.wms.service.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AuthorityResource {
    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private final AuthorityService authorityService;

    public AuthorityResource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<Authority> getAuthorities() {
        log.debug("REST request to get Authorities");
        return authorityService.findAll();
    }

    @PostMapping("/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) throws URISyntaxException {
        log.debug("REST request to create Authority : {}", authority);
        Authority saved = authorityService.create(authority.getName());
        return ResponseEntity.created(new URI("/api/admin/authorities/" + saved.getName())).body(saved);
    }

    @DeleteMapping("/authorities/{name}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteAuthority(@PathVariable String name) {
        log.debug("REST request to delete Authority : {}", name);
        authorityService.delete(name);
        return ResponseEntity.noContent().build();
    }
}
