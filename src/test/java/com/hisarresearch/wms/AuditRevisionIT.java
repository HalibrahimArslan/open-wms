package com.hisarresearch.wms;

import static org.assertj.core.api.Assertions.assertThat;

import com.hisarresearch.wms.domain.audit.CustomRevisionEntity;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Envers revizyon numarasi eskisi gibi hibernate_sequence'tan birer birer gelmeli.
 * Hibernate 6+ ortuk sekans adini degistirdigi icin generator acikca tanimli; bu test
 * rev'in gercekten o sekanstan alindigini dogrular.
 */
class AuditRevisionIT extends AbstractIntegrationTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void revisionNumberComesFromHibernateSequence() {
        Long before = jdbcTemplate.queryForObject("SELECT last_value FROM hibernate_sequence", Long.class);
        Boolean called = jdbcTemplate.queryForObject("SELECT is_called FROM hibernate_sequence", Boolean.class);
        long expected = Boolean.TRUE.equals(called) ? before + 1 : before;

        CustomRevisionEntity revision = transactionTemplate.execute(status -> {
            CustomRevisionEntity entity = new CustomRevisionEntity();
            entity.setTimestamp(System.currentTimeMillis());
            entity.setCreatedBy("test");
            entity.setCreatedDate(Instant.now());
            entityManager.persist(entity);
            return entity;
        });

        assertThat((long) revision.getId()).isEqualTo(expected);
        assertThat(jdbcTemplate.queryForObject("SELECT count(*) FROM revinfo WHERE rev = ?", Long.class, revision.getId())).isEqualTo(1L);
    }
}
