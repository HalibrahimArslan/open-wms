package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurUserRoleRel;
import com.hisarresearch.wms.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class UserRoleResource {
    private static final String ENTITY_NAME = "userRole";
    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRoleService userRoleService;

    public UserRoleResource(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping("/user-roles")
    public ResponseEntity<List<AurUserRoleRel>> getUserRoleList(Pageable pageable){
        log.debug("REST request to get user roles");
        Page<AurUserRoleRel> page = userRoleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/user-role")
    public ResponseEntity<List<AurUserRoleRel>> createUserRole(@RequestBody List<AurUserRoleRel> userRoleList) throws URISyntaxException {
        log.debug("REST request to save AurUserRoleRel : {}", userRoleList);
        List<AurUserRoleRel> userRoles = userRoleService.saveUserRoleRelBulk(userRoleList);
        return ResponseEntity
            .created(new URI("/user-rol-rel"))
            .headers(HeaderUtil.createAlert(applicationName,"userrolerel.created",userRoleList.toString()))
            .body(userRoles);
    }
}
