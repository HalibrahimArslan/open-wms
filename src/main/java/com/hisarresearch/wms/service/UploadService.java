package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Tag;
import com.hisarresearch.wms.domain.Upload;
import com.hisarresearch.wms.repository.UploadRepository;
import com.hisarresearch.wms.service.dto.tag.TagDTO;
import com.hisarresearch.wms.service.dto.upload.UploadDTO;
import com.hisarresearch.wms.service.mapper.UploadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@Transactional
public class UploadService {
    private final Logger log = LoggerFactory.getLogger(UploadService.class);


    private final UploadRepository uploadRepository;

    private final UploadMapper uploadMapper;

    private final TagService tagService;

    private final UserService userService;

    public UploadService(UploadRepository uploadRepository, UploadMapper uploadMapper, TagService tagService, UserService userService) {
        this.uploadRepository = uploadRepository;
        this.uploadMapper = uploadMapper;
        this.tagService = tagService;
        this.userService = userService;
    }

    @Transactional
    public List<Upload> getByCompanyCode(String companyCode) {
        log.debug("Request getByCompanyCode : {}", companyCode);
        return uploadRepository.findByCompanyCode(companyCode);
    }

    public List<Upload> getAllUploads() {
        log.debug("Request getAllUploads");
        return uploadRepository.findAll();
    }

    public Upload save(UploadDTO uploadDTO) {
        log.debug("Request to save Upload : {}", uploadDTO);
        Upload upload = uploadMapper.toEntity(uploadDTO);
        for (Tag tag : upload.getTags()) {
            Optional<Tag> newTag = tagService.findById(tag.getName());
            if (newTag.isEmpty()) {
                tagService.save(tag);
            }
        }
        upload = uploadRepository.save(upload);
        return upload;

    }

    public Upload saveMinioObject(String updatedFilename, String url, String bucketName) {
        log.debug("Request to save Upload by minio object : {}", updatedFilename);
        StringBuilder uploadUrl = new StringBuilder("https://");
        uploadUrl.append(url);
        uploadUrl.append("/");
        uploadUrl.append(bucketName);
        uploadUrl.append("/");
        uploadUrl.append(updatedFilename);
        UploadDTO upload = new UploadDTO();
        upload.setDescription("Feedback service uploaded file");
        upload.setUrl(uploadUrl.toString());
        upload.setTitle("FEEDBACK");
        upload.setCompanyCode(userService.getUserCompanyCode().toString());
        List<TagDTO> tags = new ArrayList<>();
        TagDTO tag = new TagDTO("FEEDBACK");
        tags.add(tag);
        upload.setTags(tags);
        return save(upload);
    }

}
