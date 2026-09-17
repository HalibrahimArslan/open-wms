package com.hisarresearch.wms.utility;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import javax.persistence.metamodel.SingularAttribute;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;

@Service
public class AurQueryService extends QueryService<Object> {

    @SuppressWarnings("unchecked")
    public <ENTITY> Specification<ENTITY> createSpecification(Object criteria, Class<?> metaModelClass) {
        Specification<ENTITY> specification = Specification.where(null);

        for (Method getter : criteria.getClass().getMethods()) {
            if (getter.getName().startsWith("get") && !getter.getName().equals("getClass")) {
                try {
                    Object filterValue = getter.invoke(criteria);
                    if (filterValue != null) {
                        String fieldName = Introspector.decapitalize(getter.getName().substring(3));

                        Field metaField = metaModelClass.getDeclaredField(fieldName);
                        metaField.setAccessible(true);
                        SingularAttribute<? super ENTITY, ?> attribute = (SingularAttribute<? super ENTITY, ?>) metaField.get(null);

                        // Filter tipine göre method çağır
                        if (filterValue.getClass().getSimpleName().contains("StringFilter")) {
                            specification = specification.and((Specification<ENTITY>) buildStringSpecification((StringFilter) filterValue, (SingularAttribute<? super Object, String>) attribute));
                        } else if (filterValue.getClass().getSimpleName().contains("LongFilter")) {
                            specification = specification.and((Specification<ENTITY>) buildRangeSpecification((LongFilter) filterValue, (SingularAttribute<? super Object, Long>) attribute));
                        } else if (filterValue.getClass().getSimpleName().contains("InstantFilter")) {
                            specification = specification.and((Specification<ENTITY>) buildRangeSpecification((InstantFilter) filterValue, (SingularAttribute<? super Object, Instant>)  attribute));
                        }
                        // başka filtre türleri varsa burada genişletilebilir
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Specification oluşturulurken hata: " + getter.getName(), e);
                }
            }
        }

        return specification;
    }


}
