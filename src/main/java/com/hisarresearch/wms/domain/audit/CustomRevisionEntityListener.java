package com.hisarresearch.wms.domain.audit;

import com.hisarresearch.wms.security.SecurityUtils;
import org.hibernate.envers.RevisionListener;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

public class CustomRevisionEntityListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
        customRevisionEntity.setCreatedBy(SecurityUtils.getCurrentUserLogin().orElse(null));
        customRevisionEntity.setCreatedDate(Instant.now());
    }
}
