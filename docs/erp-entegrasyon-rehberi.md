# ERP Entegrasyon Rehberi — `ErpOrderGateway`

Mikro'yu sıfırdan yeniden entegre etseydik (ya da yeni bir ERP eklesek), tek yapılması
gereken bu arayüzü uygulayan bir `@Service` yazmaktı. Bu doküman, o arayüzün bugün
gerektirdiği **tüm metotları ve model (DTO) şekillerini** tek yerde toplar.

Adaptor deseninin genel kuralları için: `.claude/rules/erp-adapter.md`.

---

## 1. Zorunlu iki metot

Her adaptör bunları uygulamak zorunda; router ve registry bunlar üzerinden çalışır.

| Metot | İş |
|---|---|
| `Set<ErpConnectionType> erpTypes()` | Bu adaptörün karşıladığı ERP tip(ler)i. `ErpGatewayRegistry` bean'leri buna göre indeksler. Boş küme dönenler kayda alınmaz. |
| `String getToken(String apiPath, String apiParameters)` | Uzak ERP için oturum anahtarı. Yerel/erişimsiz adaptörler `null` döner. |

```java
public enum ErpConnectionType {
    LOCAL(0), MIKRO_V16(1), UYUMSOFT(2), MIKRO_V15(3);
}
```

Mikro'da `getToken`, `ErpTokenService.getToken(apiPath, apiParameters)`'a delege eder —
token üretimi ayrı bir servise ayrıştırılmış, adaptörün kendisi bilmiyor.

---

## 2. Okuma metotları

Tüm okuma metotları `(token, apiPath, ...)` alır; `token`/`apiPath`'i çağıran taraf
`ErpGatewayRouter#context()`'ten alır, yerel modda ikisi de `null` gelir ve kullanılmaz.

| # | Metot | Ne getirir | Mikro'da gerçek karşılığı |
|---|---|---|---|
| 1 | `getFirmList(token, apiPath, int depoNo, int sipTip)` | Açık siparişi olan carilerin özet listesi | `GET {apiPath}/firmListOrderExists/{depoNo}/{sipTip}` (dedike REST ucu) |
| 2 | `getCariOrderList(token, apiPath, AurFirmListDto)` | Bir carinin açık siparişleri, sipariş başlığı bazında gruplu | `POST {apiPath}/firmOrderList` |
| 3 | `getCariOrderDetailList(token, apiPath, AurFirmListDto)` | Aynı sorgu, düz satır listesi halinde (WMS durumuyla zenginleştirilmiş) | `POST {apiPath}/firmOrderList` (aynı uç, farklı işleme: `AurOrderMasterService.getFilteredDepoOrderDetails`) |
| 4 | `getOrderDetail(token, apiPath, orderNo, sipTip, depoNo)` | Tek bir siparişin satır detayları | `GET {apiPath}/orderDetail/{orderNo}/{sipTip}/{depoNo}` |
| 5 | `getStockDetails(token, apiPath, List<String> barcodes, depoNo)` | Barkod listesiyle stok/ürün detayı, `Map<barkod, dto>` olarak | genel `executeService` ucu, `serviceName: stokService.stokDetaySorgula` |
| 6 | `getProductInfo(token, apiPath, ProductInfoRequestDto)` | Barkod / stok kodu / toplu barkod ile ürün arama (UI'nin tek bildiği uç) | aynı `stokService.stokDetaySorgula`, farklı arama modları |
| 7 | `getDepoList(token, apiPath, companyCode)` | Depo listesi | genel `executeService`, `serviceName: depoService.getDepoList` |
| 8 | `getOrderComprehensiveDetails(token, apiPath, OrderParamsDTO)` | Siparişin tüm satırlarıyla geniş detayı | `POST {apiPath}/searchOrderList` |
| 9 | `getWaybillList(token, apiPath, WaybillQueryRequestDto)` | Tarih aralığında irsaliye/evrak kayıtları (dashboard grafiği + irsaliye kontrol listesi) | genel `executeService`; `kaynak == null` ise `irsaliyeService.irsaliyeSorgula`, doluysa `irsaliyeService.irsaliyeSorgula1` |
| 10 | `getFirmStockOrderList(token, apiPath, FirmStockOrderListRequestDto)` | FMK (firma mal kabul) akışında bir firmanın sipariş kalemleri | genel `executeService`, `serviceName: depoService.getFirmStockOrderList` |

> Mikro'da "genel `executeService` ucu" dediğimiz, `{serviceName, data}` zarfını
> `{apiPath}/executeService`'e POST'layan tek bir dispatcher. Adaptörün kendi içinde
> (`MikroServices.callExecuteService` + `convertToList` yardımcıları) sarmalanır;
> UI veya `ErpOrderResource` bu string'leri hiç görmez.

---

## 3. Henüz kanonik olmayan yazma metotları

Bu üçü şu an ERP'nin ham cevabını (`Object`) geçiriyor; yerel iş kuralları
yazıldığında ortak bir yanıt şekli (`ErpOperationResult` gibi) sabitlenmeli.

| Metot | İş | Mikro'da gerçek karşılığı |
|---|---|---|
| `receiveOrder(token, apiPath, MalKabulRequestDto, addressId)` | Firmadan mal kabul | `POST {apiPath}/malKabulYap` |
| `dispatchOrder(token, apiPath, SevkiyatRequestDto)` | Müşteriye sevkiyat | `POST {apiPath}/sevkiyatYap` |
| `generateBarcode(token, apiPath, stokKod)` | Stok kodundan barkod üretimi | `GET {apiPath}/produceBarkod/{stokKod}` |

Yerel adaptör (`LocalOrderGateway`) bu üçü için `ErpOperationResult.notImplemented(...)`
döner — istisna fırlatmaz, ekran akışı yerelde de bozulmaz.

Kanonik şekle geçmiş ilk yazma metodu depolar arası transferdir:

| Metot | İş | Mikro'da gerçek karşılığı |
|---|---|---|
| `interWarehouseTransfer(token, apiPath, DepolarArasiTransferErpDto)` → `ErpOperationResult` | Depolar arası transferin ERP'ye bildirimi; `reference` ERP belge numarası | `POST {apiPath}/depolarArasiTransferYap` |

Yerel adaptör ERP'ye bir şey göndermeden `success = true` döner (stok hareketini
WMS zaten yapar). `success = false` dönerse `DepolarArasiTransferService` istisna
fırlatır; servis `rollbackOn = Exception.class` ile işaretli olduğu için yerel stok
hareketi de geri alınır.

```java
public class ErpOperationResult {
    boolean success;
    String message;
    String reference;   // is kurali eklendiginde uretilen belge/barkod no buraya yazilir
}
```

---

## 4. Kanonik yanıt DTO'ları

Adaptör hangi ERP olursa olsun aynı şekli döner; ham JSON `Object` olarak dışarı
sızmaz (bkz. `.claude/rules/erp-adapter.md` madde 3).

### `AurCariDto` — cari özeti

| Alan | Tip | Not |
|---|---|---|
| `cariKod`, `cariUnvan` | `String` | |
| `bolgeKodu`, `bolgeAdi` | `String` | |
| `cariHareketTipi`, `cariBaglantiTipi` | `String` | |
| `orderLineItemCount` | `Integer` | Sadece tek-cari sorgularında dolar |
| `orderList` | `List<AurCariOrderSummaryDto>` | Sadece tek-cari sorgularında dolar |

`AurCariOrderSummaryDto`: `orderNo`, `transGroupName`.

### `AurCariOrderDto` — cariye ait sipariş başlığı

`orderNo`, `orderDate`, `orderLineItemCount`, `orderDetail: List<AurCariOrderDetailDto>`

### `AurCariOrderDetailDto` — tekil sipariş satırı (geniş)

`barkod`, `durum`, `teslimMiktar`, `siparisMiktar`, `planlananSevkTarihi`,
`teslimTarihi`, `sipUid`, `stokAdi`, `stokBirimi`, `stokKodu`, `onaylayanKullanici`,
`depoNo`, `addressNo`, `stokMiktar`, `sevkAddress`, `sevkTel`, `sevkMuhatap`,
`sevkAcikAdres`, `sipDurum` (json: `siparisDurum`), `partialList`, `hasPiece`,
`sevkHazirMiktar`, `lotBasedTracking`, `kategoriAdi`, `birimIciAdet`, `anaGrupAdi`,
`altGrupAdi`, fiziksel özellikler (`agirlik`, `genislik`, `derinlik`, `yukseklik`, `dara`
→ `getPhysicalAttributes()` ile map'e çevrilir).

### `AurCariOrderDetailListDto` — düz sipariş satırı listesi

`id`, `orderNo`, `orderDate`, `orderLineItemCount`, `barkod`, `durum`, `teslimMiktar`,
`sipUid`, `siparisMiktar`, `stokAdi`, `stokKodu`, `stokBirimi`, `teslimTarihi`,
`planlananSevkTarihi`, `depoNo`, `addressNo`, `stokMiktar`, `sevkAddress`, `sevkTel`,
`sevkMuhatap`, `sevkAcikAdres`, `kullaniciAdi`, `aktif`, `sipDurum`, `sevkHazirMiktar`,
`onayDurum`

### `StockDetailResponseDto` — ürün/stok detayı

`stokKodu`, `stokAdi`, `barkod`, `stokBirimi`, `kategoriAdi`, `kategoriKodu`,
`anagrupAdi`, `anagrupKodu`, `birimIciAdet`, `description`, `depodakiMiktar`,
fiziksel özellikler (`agirlik`, `genislik`, `derinlik`, `yukseklik`, `dara`)

### `AurWaybillDto` — irsaliye/evrak kaydı

`evrakSeri`, `evrakSira`, `siparisNo`, `cariKod`, `cariUnvan`, `evrakTip`, `tarih`,
`kullanici`, `kaynak` (`"ERP"` ya da `"DYS"`)

---

## 5. Request DTO'ları

### `AurFirmListDto` — cari bazlı sorgular için ortak istek

`depoList: List<Integer>`, `firmCode`, `sipTip`, `transGroupCode`

### `ProductInfoRequestDto`

`stokKodu`, `stokAdi`, `barkod`, `barkodList: List<String>`, `depoNo` — hangi alan
doluysa o arama modu çalışır (barkod / stok kodu / toplu barkod).

### `WaybillQueryRequestDto`

`firmCode`, `evrakTip` (`1` sevkiyat, `13` mal kabul, `null` tümü), `kaynak`
(`null` → filtre yok / dashboard modu, `""`/`"DYS"`/`"ERP"` → kontrol listesi modu),
`beginDate`, `endDate`

### `FirmStockOrderListRequestDto`

`depoNo`, `firmCode`, `sipTip`

### `OrderParamsDTO`

`evrakSeri`, `evrakSira`, `sipTip`, `depoNo`

### `MalKabulRequestDto` (mal kabul)

`belgeNo`, `depoNo`, `tarih`, `firmCode`, `orderNo`, `erpUserCode`, `orderId`
(zorunlu), sürücü/araç bilgileri (`aracPlakaNo`, `dorsePlakaNo`, `soforAdi`,
`soforSoyadi`, `soforTckn`, `soforTel`), lookup alanları (`transportationType`,
`companyLogistics`, `carryType`), `orderDetailList: List<OrderLineItemDto>`

### `SevkiyatRequestDto` (sevkiyat)

`MalKabulRequestDto` ile aynı sürücü/araç/lookup alanları, `orderId` (zorunlu),
`orderDetailList: List<SevkiyatOrderLineItemDto>`

---

## 6. Henüz sözleşmeye taşınmamış, Mikro'ya özel iki metot

Bunlar `ErpOrderGateway`'de değil, doğrudan `MikroServices` üzerinde tanımlı ve
`Mikrov16Resource` altında ayrı uçlarla dışarı açılıyor. Sebep: davranışları tüm
ERP'ler için henüz netleşmemiş / tek kullanım noktası var, bu yüzden ortak
sözleşmeyi kirletmeden önce izole tutuldu.

| Metot | İş | Neden ayrı |
|---|---|---|
| `getFirmOrdersByCariKod(token, apiPath, FirmOrdersByCariKodRequestDto)` | Tek bir cari için açık siparişleri (`orderList`/`orderLineItemCount` dahil) getirir | `getFirmList`'in "tüm cariler" haliyle aynı Mikro servis adını (`depoService.getFirmListOrderExists`) paylaşıyor ama `cariKod` verilince tamamen farklı, zengin bir yanıt dönüyor — ileride `getFirmList` ile birleştirilecek |
| `getOrderDetailListByOrderNos(token, apiPath, OrderDetailListRequestDto)` | Belirli sipariş numaralarının kalem detaylarını getirir | `getCariOrderDetailList` ile aynı zenginleştirmeyi paylaşıyor, farkı cari koduyla değil doğrudan sipariş numaralarıyla sorgulaması |

`FirmOrdersByCariKodRequestDto`: `depoNo`, `sipTip`, `cbt`, `cariKod`
`OrderDetailListRequestDto`: `orderNoList: List<String>`, `sipTip`, `depoList: List<Integer>`

---

## 7. Yeni bir ERP eklerken checklist

1. `ErpOrderGateway`'i uygulayan yeni bir `@Service` yaz (bkz. `MikroServices`,
   `UyumsoftOrderGateway` örnekleri).
2. `erpTypes()` ile hangi `ErpConnectionType` değer(ler)ini karşıladığını bildir.
3. Yukarıdaki 10 okuma metodunu ve 3 yazma metodunu uygula. Henüz karşılığı
   olmayan işlem için istisna fırlatma — `Collections.emptyList()`/`emptyMap()`
   ya da `ErpOperationResult.notImplemented("...")` döndür, `// TODO` ile hangi
   iş kuralının eksik olduğunu yaz.
4. Registry, router, `ErpOrderResource` ve istemci (UI) sözleşmesine **dokunma** —
   bunlar otomatik olarak yeni adaptörü tanır.
5. ERP'ye özel ama henüz genelleşmemiş bir davranış varsa (bölüm 6'daki gibi),
   önce izole bir metot/uç olarak yaz; birden fazla adaptörde aynı şekilde
   ihtiyaç duyulduğunda `ErpOrderGateway`'e taşı.
6. Endpoint/kimlik bilgilerini `application*.yml`'de `${ENV_VAR:}` olarak
   tanımla; şirket bazlı olanlar `aur_company.api_parameters`'tan gelir.
