package com.hisarresearch.wms.service.dto.upload;

import com.hisarresearch.wms.service.dto.uploadcomment.UploadCommentDTO;
import com.hisarresearch.wms.service.dto.tag.TagDTO;

import java.util.List;

public class UploadDTO {
    private Long id;
    private String url;
    private String title;
    private String description;
    private String companyCode;
    private List<TagDTO> tags;
    private List<UploadCommentDTO> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public List<UploadCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<UploadCommentDTO> comments) {
        this.comments = comments;
    }
}
