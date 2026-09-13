package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.TagService;
import com.hisarresearch.wms.service.dto.tag.TagDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TagResource {
    private static final String ENTITY_NAME = "tag";
    private final Logger log = LoggerFactory.getLogger(TagResource.class);

    private final TagService tagService;

    public TagResource(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDTO>> getAllTags() {
        log.debug("REST request to get all Tags");
        return ResponseEntity.ok().body(tagService.getAllTags());

    }
}
