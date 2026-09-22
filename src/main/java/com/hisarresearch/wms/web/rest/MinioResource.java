package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.Upload;
import com.hisarresearch.wms.security.AuthoritiesConstants;
import com.hisarresearch.wms.service.MinioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class MinioResource {
    private final Logger log = LoggerFactory.getLogger(MinioResource.class);

    private final MinioService minioService;

    public MinioResource(MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("/download/{fileName}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MINIO + "\")")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileName") String fileName) {
        try {
            log.debug("Minio download process is started {}",fileName);
            InputStream stream =  minioService.downloadFile(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/single-file-upload")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MINIO + "\")")
    public ResponseEntity<Void> handleSingleFileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("Minio single file upload is started {}",file);
        minioService.uploadObject(file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/multiple-file-upload")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MINIO + "\")")
    public ResponseEntity<List<Upload>> handleMultipleFileUpload(@RequestParam("files") MultipartFile[] files) {
        log.debug("Multiple file upload is started, {} files", files.length);
        List<Upload> uploadedFiles = new ArrayList<>() ;
        for(MultipartFile file: files){
            Upload uploadedFile = minioService.uploadObject(file);
            uploadedFiles.add(uploadedFile);
        }
        return ResponseEntity.ok().body(uploadedFiles);
    }
}
