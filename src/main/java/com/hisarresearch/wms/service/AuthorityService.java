package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Authority;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.repository.AuthorityRepository;
import com.hisarresearch.wms.repository.UserRepository;
import com.hisarresearch.wms.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AuthorityService {
    private static final String ENTITY_NAME = "authority";

    private static final Set<String> SYSTEM_AUTHORITIES = Set.of(
        AuthoritiesConstants.ADMIN,
        AuthoritiesConstants.USER,
        AuthoritiesConstants.ANONYMOUS,
        AuthoritiesConstants.COUNTER,
        AuthoritiesConstants.CHECKER,
        AuthoritiesConstants.MINIO
    );

    private final Logger log = LoggerFactory.getLogger(AuthorityService.class);

    private final AuthorityRepository authorityRepository;

    private final UserRepository userRepository;

    public AuthorityService(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Authority> findAll() {
        return authorityRepository.findAll(Sort.by("name"));
    }

    public Authority create(String name) {
        log.debug("Request to create Authority : {}", name);
        String trimmed = name == null ? "" : name.trim();
        if (trimmed.isEmpty()) {
            throw new BadRequestAlertException("Yetki adı boş olamaz", ENTITY_NAME, "nameempty");
        }
        if (trimmed.length() > 50) {
            throw new BadRequestAlertException("Yetki adı en fazla 50 karakter olabilir", ENTITY_NAME, "nametoolong");
        }
        if (authorityRepository.existsById(trimmed)) {
            throw new BadRequestAlertException("Bu yetki zaten tanımlı: " + trimmed, ENTITY_NAME, "nameexists");
        }
        Authority authority = new Authority();
        authority.setName(trimmed);
        return authorityRepository.save(authority);
    }

    public void delete(String name) {
        log.debug("Request to delete Authority : {}", name);
        if (SYSTEM_AUTHORITIES.contains(name)) {
            throw new BadRequestAlertException("Sistem yetkisi silinemez: " + name, ENTITY_NAME, "systemauthority");
        }
        if (!authorityRepository.existsById(name)) {
            throw new BadRequestAlertException("Yetki bulunamadı: " + name, ENTITY_NAME, "notfound");
        }
        if (userRepository.existsByAuthorities_Name(name)) {
            throw new BadRequestAlertException("Bu yetki kullanıcılara atanmış, önce kullanıcılardan kaldırın: " + name, ENTITY_NAME, "inuse");
        }
        authorityRepository.deleteById(name);
    }
}
