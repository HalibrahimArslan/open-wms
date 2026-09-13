package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.Upload;
import com.hisarresearch.wms.domain.UploadComment;
import com.hisarresearch.wms.service.dto.upload.UploadDTO;
import com.hisarresearch.wms.service.UploadService;
import com.hisarresearch.wms.service.mapper.UploadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Transactional
public class UploadResource {
    private static final String ENTITY_NAME = "upload";
    private final Logger log = LoggerFactory.getLogger(UploadResource.class);

    private final UploadService uploadService;

    private final UploadMapper uploadMapper;

    public UploadResource(UploadService uploadService, UploadMapper uploadMapper) {
        this.uploadService = uploadService;
        this.uploadMapper = uploadMapper;
    }

    /**
     * get all upload items
     *
     * @return list of upload items
     */

    @GetMapping("/uploads")
    public ResponseEntity<List<UploadDTO>> getAllUploads() {
        log.debug("REST request to get all Uploads");
        List<Upload> uploads = uploadService.getAllUploads();
        List<UploadDTO> uploadDto = uploads
            .stream()
            .map(upload -> {
                Set<UploadComment> filteredComments = upload.getComments().stream()
                    .filter(comment -> comment.getParentComment() == null)
                    .collect(Collectors.toSet());
                upload.setComments(filteredComments);
                return uploadMapper.toDto(upload);
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(uploadDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadDTO> createUpload(@Valid @RequestBody UploadDTO uploadDTO) throws URISyntaxException {
        log.debug("REST request to save Upload : {}", uploadDTO);
        Upload upload = uploadService.save(uploadDTO);
        return ResponseEntity.created(new URI("/api/uploads/" + upload.getId()))
            .body(uploadMapper.toDto(upload));
    }

}
