---
name: run-open-wms
description: open-wms backend'ini calistir, baslat, durdur, testlerini kos ve suren uygulamaya curl ile istek at. "uygulamayi ayaga kaldir", "run/start open-wms", "backend'i calistir", "bir ucu dene", "acilis logunda uyari var mi", "testleri calistir" gibi isteklerde kullan.
---

open-wms, Spring Boot 4 / Java 25 ile yazilmis bir REST backend'idir; arayuzu yoktur
(arayuz komsu `../open-wms-app` deposundadir). Uygulama
`.claude/skills/run-open-wms/wms.sh` ile arka planda baslatilir ve `curl` ile surulur.
Tum yollar repo kokune goredir.

## Gereksinimler

- JDK 25 (`java -version` -> 25), Docker + docker compose v2, `curl`, `python3`, `ss`.
- Komsu `../websocket-server` deposu: compose'daki `websocket` servisi oradan derlenir.
- `.env` opsiyonel; yoksa compose'daki varsayilanlar kullanilir (`cp .env.example .env`).

## Calistirma (ajan yolu)

```bash
.claude/skills/run-open-wms/wms.sh start     # konteynerler + uygulama + seed, hazir olunca doner (~17 sn)
.claude/skills/run-open-wms/wms.sh smoke     # admin girisi + 3 uc, hepsi 200 ise cikis 0
.claude/skills/run-open-wms/wms.sh call GET /api/account
.claude/skills/run-open-wms/wms.sh log "Unsafe::"
.claude/skills/run-open-wms/wms.sh stop
```

| komut | ne yapar |
|---|---|
| `deps` | `docker compose up -d --wait postgres mail websocket minio` |
| `start` | `deps`, sonra `./mvnw` (dev profili) arka planda; `Started WmsApp` gorunce `seed/local-seed.sql` yukler. Hata olursa `Caused by` satirlarini basar, cikis 1 |
| `status` | 8080'i dinleyen surec var mi (yoksa cikis 1) |
| `token [kullanici] [sifre]` | JWT basar; varsayilan `admin`/`admin`, seed'de `user`/`user` da var |
| `call METOD YOL [JSON]` | admin token'i ile istek; govdeyi ve son satirda `HTTP <kod>` basar |
| `smoke` | `/api/account`, `/api/aur-depo-urun-adres-stok`, `/management/health` |
| `log [desen]` | ANSI kodlari temizlenmis uygulama logu, desen verilirse `grep -E` |
| `stop` | 8080'deki JVM'e SIGTERM, surec cikana kadar bekler; konteynerlere dokunmaz |

Log: `/tmp/open-wms/app.log` (`WMS_LOG_DIR` ile degisir). Uygulama `http://localhost:8080`.
Dev profilinde devtools acik; `target/classes` degisince uygulama kendini yeniden baslatir.

Konteynerler `stop` ile durmaz. Lokal ortam bilerek otomatik baslamaz (compose'da
`restart:` yok); kapatmak gerekirse `docker compose stop`.

## Test

```bash
./mvnw verify
```

Beklenen: 18 birim testi (surefire) + 41 entegrasyon testi (failsafe, `*IT`), `BUILD SUCCESS`,
yaklasik 1 dakika. `*IT` testleri Testcontainers ile kendi PostgreSQL'ini acar; calisan
uygulamaya ya da compose veritabanina ihtiyac duymaz. Tek IT: `./mvnw verify -Dit.test=CacheIT -Dtest=NoUnitTests -Dsurefire.failIfNoSpecifiedTests=false`.

## Tuzaklar

- **Duz `./mvnw` acilista duser** (`'url' must start with "jdbc"`): dev profili
  `POSTGRESQL_CONNECTION_URL`, `MINIO_ACCESSKEY` gibi degiskenleri bekler ama `.env`
  compose icin yazilmistir (`POSTGRES_DB`, `MINIO_ACCESS_KEY`). Eslemeyi compose'daki
  `app` servisi yapar; host'ta `wms.sh` ayni eslemeyi `localhost` adresleriyle yapar.
- **`.env` bash ile `source` edilemez**: `JAVA_OPTS=-Xms256m -Xmx1024m` tirnaksizdir,
  bash `-Xmx1024m`'yi komut sanar (`-Xmx1024m: command not found`). `wms.sh` dosyayi
  satir satir okur. Ayrica `.env`'deki `SPRING_PROFILE`/`JAVA_OPTS` konteyner icindir,
  `./mvnw`'ye sizdirilmaz.
- **Seed semadan sonra yuklenir**: sema acilista Liquibase ile kurulur; bos veritabaninda
  seed'i uygulamadan once yuklemek tablolar olmadigi icin basarisiz olur. Seed idempotent,
  her `start`'ta tekrar yuklenir.
- **`sun.misc.Unsafe` uyarisi acilista degil, ilk istekte** cikar: ModelMapper
  (`typetools.TypeResolver`) ilk kullanildiginda. Uyari kontrolu icin `start` yetmez,
  `smoke` sonrasi `log "Unsafe::"` bakilir. JVM bu uyariyi surec basina bir kez basar;
  ModelMapper bilerek tutuluyor.
- **Port bosaldi != surec bitti**: SIGTERM sonrasi 8080 hemen kapanir, JVM kisa bir sure
  daha yasar. `stop` bu yuzden PID'i bekler; hemen ardindan `start` guvenlidir.
- **`[WARNING] Parameter 'fork' is unknown`** ve enforcer `DependencyConvergence`
  uyarilari `./mvnw` ciktisinda her seferinde gorunur; zararsizdir.

## Sorun giderme

- **Tum `*IT` testleri `NoClassDefFoundError: WebSocketClientService$1` ile duser**
  (Mockito "Cannot instrument class"): sinif dosyasi diskte oldugu halde oldu ve dosya
  degistirdikten hemen sonraki kosularda goruldu; hicbir sey degistirmeden tekrar
  kosunca gecti. Acik IDE'nin `target/classes`'i ayni anda derlemesinden supheleniliyor
  (kanitlanmadi). Testi yeniden kos.
- **`verify` logunda `Error while instrumenting org/drools/drl/parser/lang/DRL6Lexer`
  (JaCoCo `MethodTooLargeException`)**: Drools'un dev metodu JaCoCo ile olculemez;
  testler etkilenmez, gurultudur.
- **`start` "8080 zaten dinleniyor" der**: baska bir kopya (IDE'den baslatilmis olabilir)
  calisiyor. `wms.sh stop` onu da durdurur; IDE'ninkini durdurmadan once kullaniciya sor.
