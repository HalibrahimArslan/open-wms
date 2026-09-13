package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.Upload;
import com.hisarresearch.wms.service.dto.upload.UploadDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TagMapper.class, UploadCommentMapper.class})
public interface UploadMapper extends EntityMapper<UploadDTO, Upload> {
}
