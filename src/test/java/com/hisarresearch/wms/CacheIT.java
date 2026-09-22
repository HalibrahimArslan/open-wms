package com.hisarresearch.wms;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import com.hisarresearch.wms.domain.User;
import com.hisarresearch.wms.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Onbellek yapisi: JCache saglayicisi Caffeine, Spring {@code @Cacheable} ve Hibernate
 * ikinci seviye onbellegi ayni CacheManager uzerinden calisir.
 */
class CacheIT extends AbstractIntegrationTest {

    @Autowired
    javax.cache.CacheManager jcacheManager;

    @Autowired
    CacheManager springCacheManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TransactionTemplate transactionTemplate;

    @BeforeEach
    void clearAll() {
        springCacheManager.getCacheNames().forEach(name -> springCacheManager.getCache(name).clear());
        entityManagerFactory.getCache().evictAll();
    }

    @Test
    void cachesAreBackedByCaffeineWithConfiguredLimits() {
        assertThat(jcacheManager.getCachingProvider().getClass().getName()).startsWith("com.github.benmanes.caffeine.jcache");
        assertThat(jcacheManager.getCacheNames()).contains(UserRepository.USERS_BY_LOGIN_CACHE, User.class.getName());

        @SuppressWarnings("unchecked")
        CaffeineConfiguration<Object, Object> configuration = jcacheManager
            .getCache(User.class.getName())
            .getConfiguration(CaffeineConfiguration.class);
        assertThat(configuration.getMaximumSize()).hasValue(100);
        assertThat(configuration.getExpireAfterWrite()).hasValue(TimeUnit.SECONDS.toNanos(3600));
    }

    @Test
    void cacheableRepositoryMethodIsServedFromCache() {
        org.springframework.cache.Cache cache = springCacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE);
        assertThat(cache.get("admin")).isNull();

        String firstName = transactionTemplate.execute(status -> userRepository.findOneWithAuthoritiesByLogin("admin").orElseThrow().getFirstName());
        assertThat(cache.get("admin")).isNotNull();

        // Veritabani dogrudan degisse de ikinci cagri onbellekteki kaydi dondurur.
        transactionTemplate.executeWithoutResult(status -> jdbcTemplate.update("UPDATE aur_user SET first_name = 'OnbellekDisi' WHERE login = 'admin'"));
        try {
            Optional<User> cached = transactionTemplate.execute(status -> userRepository.findOneWithAuthoritiesByLogin("admin"));
            assertThat(cached.orElseThrow().getFirstName()).isEqualTo(firstName);

            cache.evict("admin");
            Optional<User> fresh = transactionTemplate.execute(status -> userRepository.findOneWithAuthoritiesByLogin("admin"));
            assertThat(fresh.orElseThrow().getFirstName()).isEqualTo("OnbellekDisi");
        } finally {
            String original = firstName;
            transactionTemplate.executeWithoutResult(status -> jdbcTemplate.update("UPDATE aur_user SET first_name = ? WHERE login = 'admin'", original));
            cache.evict("admin");
        }
    }

    @Test
    void entityIsServedFromHibernateSecondLevelCache() {
        Long adminId = jdbcTemplate.queryForObject("SELECT id FROM aur_user WHERE login = 'admin'", Long.class);
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
        try {
            findUserInNewEntityManager(adminId);
            assertThat(statistics.getSecondLevelCacheMissCount()).isPositive();
            assertThat(statistics.getSecondLevelCachePutCount()).isPositive();
            assertThat(entityManagerFactory.getCache().contains(User.class, adminId)).isTrue();

            long hitsBefore = statistics.getSecondLevelCacheHitCount();
            long queriesBefore = statistics.getPrepareStatementCount();
            findUserInNewEntityManager(adminId);
            assertThat(statistics.getSecondLevelCacheHitCount()).isGreaterThan(hitsBefore);
            assertThat(statistics.getPrepareStatementCount()).isEqualTo(queriesBefore);
        } finally {
            statistics.setStatisticsEnabled(false);
        }
    }

    private void findUserInNewEntityManager(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            assertThat(entityManager.find(User.class, id)).isNotNull();
        }
    }
}
