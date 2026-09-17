package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ERP tipine gore adaptor kayit defteri.
 *
 * <p>{@link ErpOrderGateway} uygulayan tum bean'ler burada {@link ErpOrderGateway#erpTypes()}
 * degerine gore indekslenir. Yeni bir ERP eklemek icin tek yapilmasi gereken, arayuzu
 * uygulayan yeni bir {@code @Service} yazip kendi tipini bildirmesidir; bu sinif dahil
 * hicbir yerde kod degistirmek gerekmez.
 *
 * <p>Indeksleme ilk kullanimda yapilir: {@link ErpGatewayRouter} hem bu defteri kullandigi
 * hem de kendisi bir {@code ErpOrderGateway} oldugu icin, acilista tum bean'leri toplamak
 * dairesel bagimliliga yol acardi. {@link ObjectProvider} ile bean'ler ancak ilk cagrida
 * cozulur. Bos {@code erpTypes()} donen bean'ler (router'in kendisi) atlanir.
 */
@Service
public class ErpGatewayRegistry {

    private final Logger log = LoggerFactory.getLogger(ErpGatewayRegistry.class);

    private final ObjectProvider<ErpOrderGateway> gatewayProvider;
    private volatile Map<ErpConnectionType, ErpOrderGateway> byType;

    public ErpGatewayRegistry(ObjectProvider<ErpOrderGateway> gatewayProvider) {
        this.gatewayProvider = gatewayProvider;
    }

    private Map<ErpConnectionType, ErpOrderGateway> index() {
        Map<ErpConnectionType, ErpOrderGateway> local = byType;
        if (local == null) {
            synchronized (this) {
                local = byType;
                if (local == null) {
                    local = build();
                    byType = local;
                }
            }
        }
        return local;
    }

    private Map<ErpConnectionType, ErpOrderGateway> build() {
        Map<ErpConnectionType, ErpOrderGateway> map = new EnumMap<>(ErpConnectionType.class);
        gatewayProvider.stream().forEach(gateway ->
            gateway.erpTypes().forEach(type -> {
                ErpOrderGateway previous = map.put(type, gateway);
                if (previous != null) {
                    log.warn("{} icin birden fazla adaptor bulundu: {} -> {}", type,
                        adapterName(previous), adapterName(gateway));
                }
            }));
        log.info("ERP adaptorleri: {}", map.entrySet().stream()
            .map(e -> e.getKey() + "=" + adapterName(e.getValue()))
            .collect(Collectors.joining(", ")));
        return map;
    }

    /** Spring proxy'lerinin ($$EnhancerBySpringCGLIB$$...) arkasindaki gercek sinif adi. */
    private static String adapterName(ErpOrderGateway gateway) {
        return ClassUtils.getUserClass(gateway).getSimpleName();
    }

    public Optional<ErpOrderGateway> find(ErpConnectionType type) {
        return type == null ? Optional.empty() : Optional.ofNullable(index().get(type));
    }

    /** Entegrasyon kapaliyken kullanilan adaptor. */
    public ErpOrderGateway local() {
        ErpOrderGateway local = index().get(ErpConnectionType.LOCAL);
        if (local == null) {
            throw new IllegalStateException("LOCAL ERP adaptoru bulunamadi");
        }
        return local;
    }

    public Map<ErpConnectionType, String> registered() {
        return index().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> adapterName(e.getValue())));
    }
}
