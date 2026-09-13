# Open WMS

Depo yönetim sistemi. Spring Boot + React tabanlı, ERP'den bağımsız çalışabilen
bir WMS çekirdeği.

## Öne çıkan tasarım: ERP'den bağımsız çekirdek

Uygulamanın hiçbir yeri hangi ERP'ye bağlı olduğunu bilmez. Tüm sipariş
işlemleri tek bir sözleşme üzerinden yürür ve her ERP bu sözleşmenin bir
adaptörüdür:

```
ErpOrderResource  ──►  ErpGatewayRouter  ──►  ErpGatewayRegistry
   (tek REST ucu)       (şirketin erpTipi         │
                         değerine göre seçer)     ├─ LocalOrderGateway    (LOCAL)
                                                  ├─ MikroServices        (MIKRO_V16, MIKRO_V15)
                                                  ├─ NetsisServices       (NETSIS)
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

Şirketin `erpTipi` değeri `LOCAL` ise ya da `api_parameters` içindeki
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
| `src/main/java` | Spring Boot uygulaması (Java 11, JHipster 7 tabanlı) |
| `src/main/webapp` | React arayüzü |
| `src/main/resources/config/liquibase` | Veritabanı şeması |
| `seed/` | Lokal geliştirme için örnek veri |
| `websocket-server/` | Bildirimler için küçük Node.js WebSocket sunucusu |
| `.claude/rules/` | Projeye özel mimari kurallar |

## Geliştirme

```bash
./mvnw                # backend (dev profili)
npm start             # frontend (webpack dev server)
./mvnw verify         # testler
```

Gereksinimler: JDK 11, Node 14, PostgreSQL 13. Docker ile çalışırken hiçbiri
yerel olarak kurulu olmak zorunda değildir — `Dockerfile.local` derlemeyi
kendi içinde yapar.

## Yapılandırma

Dış sistem adresleri ve kimlik bilgileri koda gömülmez; `application*.yml`
içinde ortam değişkeni olarak tanımlıdır:

| Değişken | Açıklama |
|---|---|
| `POSTGRESQL_CONNECTION_URL`, `POSTGRESQL_DB_USERNAME`, `POSTGRESQL_DB_PASSWORD` | Veritabanı |
| `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_URL` | SMTP |
| `MINIO_URL`, `MINIO_ACCESSKEY`, `MINIO_SECRETKEY`, `MINIO_BUCKETNAME` | Obje deposu |
| `NETSIS_TOKENPATH`, `NETSIS_QUERYPATH`, `NETSIS_SLIPPATH`, `NETSIS_DBNAME` | Netsis entegrasyonu |
| `WEBSOCKET_URI` | Bildirim sunucusu |
| `METABASE_SECRET_URL`, `METABASE_SECRET_KEY`, `METABASE_DASHBOARD_CODE` | Metabase gömülü raporlar |
| `SENTRY_DSN` | Hata takibi (boş bırakılırsa kapalı) |

Şirkete özel ERP ayarları `aur_company` tablosundaki `erp_tipi` ve
`api_parameters` alanlarından okunur.
