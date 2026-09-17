package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Tag;
import com.hisarresearch.wms.repository.TagRepository;
import com.hisarresearch.wms.service.dto.tag.TagDTO;
import com.hisarresearch.wms.service.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {
    private final Logger log = LoggerFactory.getLogger(TagService.class);


    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    public List<TagDTO> getAllTags(){
        log.debug("Request to get all Tags");
        return tagRepository.findAll().stream().map(tagMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Optional<Tag> findById(String name){
        log.debug("Request to get Tag : {}", name);
        return tagRepository.findById(name);
    }

    @Transactional
    public Tag save(Tag tag){
        log.debug("Request to save Tag : {}", tag);
        return tagRepository.save(tag);
    }

}
