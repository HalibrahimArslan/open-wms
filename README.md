# Open WMS

Depo yönetim sistemi. Spring Boot + React tabanlı, ERP'den bağımsız çalışabilen
bir WMS çekirdeği.

## Öne çıkan tasarım: ERP'den bağımsız çekirdek

Uygulamanın hiçbir yeri hangi ERP'ye bağlı olduğunu bilmez. Tüm sipariş
işlemleri tek bir sözleşme üzerinden yürür ve her ERP bu sözleşmenin bir
adaptörüdür:

```
ErpOrderResource  ──►  ErpGatewayRouter  ──►  ErpGatewayRegistry
   (tek REST ucu)       (şirketin erpType         │
                         değerine göre seçer)     ├─ LocalOrderGateway    (LOCAL)
                                                  ├─ MikroServices        (MIKRO_V16, MIKRO_V15)
                                                  └─ UyumsoftOrderGateway (UYUMSOFT)
```

Yeni bir ERP eklemek için `ErpOrderGateway` arayüzünü uygulayan bir `@Service`
yazmak ve `erpTypes()` ile hangi `ErpConnectionType` değerlerini karşıladığını
bildirmek yeterlidir. Registry onu açılışta bulur; router, resource ve istemci
sözleşmesi değişmez.

Adaptörlerin hepsi aynı kanonik DTO'ları döndürür, dolayısıyla **yanıt şekli
hangi ERP bağlı olursa olsun aynıdır**. Ayrıntılı kurallar:
[`.claude/rules/erp-adapter.md`](.claude/rules/erp-adapter.md)

### Entegrasyonsuz çalışma

Şirketin `erpType` değeri `LOCAL` ise ya da `api_parameters` içindeki
`erpApiActive` değeri `"1"` değilse siparişler yerel `aur_erp_data` tablosundan
okunur. Böylece sistem hiçbir ERP bağlantısı olmadan ayağa kalkar ve çalışır.

## Hızlı başlangıç

```bash
docker compose up -d --build
```

Ayağa kalkan servisler:

| Servis | Adres |
|---|---|
| Uygulama | http://localhost:8080 |
| PostgreSQL | `localhost:5432` (`wms` / `wms` / `wms`) |
| MinIO konsolu | http://localhost:9001 |
| Mail arayüzü (mailpit) | http://localhost:8025 |
| WebSocket sunucusu | `ws://localhost:8090/websocket` |

Örnek veriyi yükle (menü ağacı, şirket, depo, roller, demo siparişler):

```bash
docker exec -i wms-postgres psql -U wms -d wms < seed/local-seed.sql
```

Giriş: `admin` / `admin`

Ayarları değiştirmek için `.env.example` dosyasını `.env` olarak kopyalayın.

## Yapı

| Dizin | İçerik |
|---|---|
| `src/main/java` | Spring Boot 4 uygulaması (Java 25) |
| `src/main/resources/config/liquibase` | Veritabanı şeması |
| `seed/` | Lokal geliştirme için örnek veri |
| `.claude/rules/` | Projeye özel mimari kurallar |

Arayüz bu reponun dışında, `../open-wms-app` deposunda yer alır.

Bildirimler için küçük Node.js WebSocket sunucusu bu reponun dışında,
`../websocket-server` (bu repoyla aynı `wms/` dizininin altında) yer alır.

## Geliştirme

```bash
./mvnw                # backend (dev profili)
./mvnw verify         # testler
```

`./mvnw verify`, `*IT` entegrasyon testlerini de calistirir; bunlar Testcontainers
ile PostgreSQL 18 actigi icin Docker gerektirir. Sema Liquibase ile kurulur, uzerine
`seed/local-seed.sql` yuklenir. `ApiContractIT`, arayuzun kullandigi uclarin
cevaplarini `src/test/resources/api-snapshots/` altindaki kayitlarla karsilastirir;
bir cevap bilerek degistiyse kayitlar `-Dsnapshot.update=true` ile yenilenir ve
fark gozden gecirilip commit edilir.

Gereksinimler: JDK 25, PostgreSQL 13. Docker ile çalışırken hiçbiri
yerel olarak kurulu olmak zorunda değildir — `Dockerfile.local` derlemeyi
kendi içinde yapar.

### Liquibase sürümü

Spring Boot 4.1 Liquibase 5.x'i yönetir; ancak Liquibase 5 ile lisans Apache 2.0'dan
FSL-1.1'e (Functional Source License) geçti. Proje bu yüzden Apache 2.0 lisanslı son
seri olan 4.x'te (`liquibase.version`, `pom.xml`) tutuluyor. 5.x'e geçmek bir lisans
kararıdır.

## Yapılandırma

Dış sistem adresleri ve kimlik bilgileri koda gömülmez; `application*.yml`
içinde ortam değişkeni olarak tanımlıdır:

| Değişken | Açıklama |
|---|---|
| `POSTGRESQL_CONNECTION_URL`, `POSTGRESQL_DB_USERNAME`, `POSTGRESQL_DB_PASSWORD` | Veritabanı |
| `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_URL` | SMTP |
| `MINIO_URL`, `MINIO_ACCESSKEY`, `MINIO_SECRETKEY`, `MINIO_BUCKETNAME` | Obje deposu |
| `WEBSOCKET_URI` | Bildirim sunucusu |
| `METABASE_SECRET_URL`, `METABASE_SECRET_KEY`, `METABASE_DASHBOARD_CODE` | Metabase gömülü raporlar |
| `SENTRY_DSN` | Hata takibi (boş bırakılırsa kapalı) |

Şirkete özel ERP ayarları `aur_company` tablosundaki `erp_type` ve
`api_parameters` alanlarından okunur.

### Obje deposu (MinIO)

Dosya yüklemeleri S3 uyumlu bir obje deposunda tutulur. `MINIO_BUCKETNAME` ile
verilen bucket'i hazırlamak için ayrı bir kurulum adımı gerekmez: uygulama
açılırken bucket'in var olup olmadığına bakar, yoksa oluşturur
(`config/MinioBucketInitializer`). Bucket zaten varsa içeriğine dokunulmaz.

Depo açılış anında erişilemezse uygulama yine de ayağa kalkar; yalnızca uyarı
basılır ve dosya yükleme istekleri kendi hatasını döndürür. `MINIO_BUCKETNAME`
boş bırakılırsa kontrol tamamen atlanır.
