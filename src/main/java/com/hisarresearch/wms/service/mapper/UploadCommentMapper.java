package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.UploadComment;
import com.hisarresearch.wms.service.dto.uploadcomment.UploadCommentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface UploadCommentMapper extends EntityMapper<UploadCommentDTO, UploadComment>{

}
