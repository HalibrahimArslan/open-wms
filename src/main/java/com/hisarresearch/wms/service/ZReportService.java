package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurVwZReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ZReportService {
    private final Logger log = LoggerFactory.getLogger(ZReportService.class);

    @Autowired
    private EntityManager em;

    public List<AurVwZReport> dailyZReport(int previousDay) {
        log.debug("Z Raporu mail gönderme işlemi başladı");
        Query q = em.createNativeQuery("SELECT * FROM z_raporu(?1)", AurVwZReport.class);
        StringBuilder sb = new StringBuilder();
        sb.append(previousDay).append(" days");
        q.setParameter(1, sb.toString());
        return q.getResultList();
    }
}
