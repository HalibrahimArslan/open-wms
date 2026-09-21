package com.hisarresearch.wms.config;

import com.hisarresearch.wms.domain.AurSayimTanim;
import com.hisarresearch.wms.domain.ErpJwtData;
import java.time.Duration;

import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.service.erp.MikroServices;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import com.hisarresearch.wms.framework.config.JHipsterProperties;
import com.hisarresearch.wms.framework.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.hisarresearch.wms.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.hisarresearch.wms.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.hisarresearch.wms.domain.User.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Authority.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Upload.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Upload.class.getName() + ".tags");
            createCache(cm, com.hisarresearch.wms.domain.Upload.class.getName() + ".comments");
            createCache(cm, com.hisarresearch.wms.domain.User.class.getName() + ".authorities");
            createCache(cm, com.hisarresearch.wms.domain.User.class.getName() + ".roles");
            createCache(cm, com.hisarresearch.wms.domain.User.class.getName() + ".warehouses");
            createCache(cm, com.hisarresearch.wms.domain.AurUser.class.getName() + ".authorities");
            createCache(cm, Warehouse.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Tag.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Feedback.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Feedback.class.getName() + ".comments");
            createCache(cm, com.hisarresearch.wms.domain.UploadComment.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.FeedbackComment.class.getName());
            createCache(cm, ErpJwtData.class.getName());
            createCache(cm, "erpDataByErpType");
            createCache(cm,"microFirmList");
            createCache(cm, com.hisarresearch.wms.domain.JhiUser.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurMenu.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurCompany.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurRole.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurMenuRoleRel.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurUser.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurUserRoleRel.class.getName());
            createCache(cm, MikroServices.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.OrderPickingTransaction.class.getName());
            createCache(cm, AurSayimTanim.class.getName());
            createCache(cm, AurSayimTanim.class.getName() + ".aurSayimUruns");
            createCache(cm, com.hisarresearch.wms.domain.AurSayimUrun.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurPartialItem.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurPartialItem.class.getName() + ".aurPartialDetails");
            createCache(cm, com.hisarresearch.wms.domain.AurPartialDetails.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.UserFirmRel.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.UserDepoRel.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Order.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Order.class.getName() + ".orderRows");
            createCache(cm, com.hisarresearch.wms.domain.OrderRow.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.OrderStatus.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.AurDispatchAreaControl.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.ProcessTree.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.ProcessTree.class.getName() + ".processLeaves");
            createCache(cm, com.hisarresearch.wms.domain.ProcessLeaf.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.CountingAddressException.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Customer.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.Rule.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.address.AurDepoUrunAdres.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.address.AurDepoUrunAdres.class.getName() + ".productAddressList");
            createCache(cm, com.hisarresearch.wms.domain.AurDriver.class.getName());
            createCache(cm, com.hisarresearch.wms.domain.CountingUserAddressRel.class.getName());


            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
