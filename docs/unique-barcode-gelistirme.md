# Tekil Barkod — İş Analizi ve Süreç Dokümantasyonu

**Branch:** `feature/Tekil-Barkod-Adet`
**Son güncelleme:** 2026-06-24

---

## Amaç

Depoya giren her ürün birimini ayrı bir barkod ile takip etmek.
Klasik ERP barkodları ürün tipini tanımlar; tekil barkod ise o ürünün **o spesifik fiziksel birimini** tanımlar.

Örnek:
- ERP barkodu `8690123456789` → "Bu bir X ürünü"
- Tekil barkod `8690123456789260611001230002025003` → "Bu, 11 Haziran 2026'da MUS001 müşterisinden gelen, 3 numaralı lot, X ürünü"

---

## Genel Süreç

```
1. Ürün Girişi Planlanır
        ↓
2. Tekil Barkodlar Üretilir  (POST /api/unique-barcodes)
        ↓
3. Ürün Depoya Fiziksel Olarak Gelir
        ↓
4. Ürün Kabul Taraması Yapılır  (Sipariş kabul akışı içinde)
        ↓
5. Barkod "Kabul Edildi" Statüsüne Geçer
```

---

## Barkod Yaşam Döngüsü

Tekil barkodun alabileceği üç durum vardır (`UniqueBarcodeState`):

| Durum | Anlamı | Ne zaman girilir |
|---|---|---|
| `CREATED` | Barkod üretildi, henüz fiziksel kabul yapılmadı | Barkodlar oluşturulduğunda otomatik |
| `RECEIVING_SCANNED` | Ürün depoya kabul edildi, barkod okutuldu | Ürün kabul taraması sırasında |
| `IN_TEMPORARY_AREA` | Barkod geçici adrese transfer edildi (irsaliye/adres operasyonu) | Ürün kabul adres operasyonu tamamlanınca |

**Geçişler (`UniqueBarcodeEvent`):**

```
CREATED ──RECEIVE_SCAN──▶ RECEIVING_SCANNED ──TRANSFER_TO_TEMPORARY_AREA──▶ IN_TEMPORARY_AREA
   ▲                              │
   └──── CANCEL_RECEIVE_SCAN ─────┘
```

- `RECEIVING_SCANNED` durumundan `CANCEL_RECEIVE_SCAN` ile `CREATED`'a geri dönülebilir (okutma iptali). Bu geçiş **state machine** üzerinden işler ve barkodun sipariş bağını (`aurOrderDetail`) da temizler. Sipariş askıya alındığında (`suspendOrder`) ilgili detayların okutulmuş barkodları otomatik bu yolla `CREATED`'a döner.
- `IN_TEMPORARY_AREA` geçişinde barkoda adres (`address_id`) atanır ve `aur_depo_urun_adres_stok` aggregate kaydı güncellenir.

---

## Takip Modeli: Tekil Barkod vs Lot Bazlı

Bir ürün ya **tekil barkod** ile ya da **lot bazında** takip edilir; ayrım `Product.lotBasedTracking` (boolean, default `false`) flag'i ile yapılır.

| `lotBasedTracking` | Takip modeli | `POST /api/unique-barcodes` davranışı | Ürün kabul davranışı |
|---|---|---|---|
| `false` (varsayılan) | **Tekil barkod** | Gerçek parti/lot ile barkod üretilir ve **kaydedilir** | Tekil barkod akışı (`RECEIVING_SCANNED` → `IN_TEMPORARY_AREA`) |
| `true` | **Lot bazlı** | "Oluşturmuş gibi" parti=`0000`, lot=`0` ile DTO döner, **kayıt atılmaz** | Klasik stok akışı (`assignProductToAddress`) |

Flag, `PATCH /api/product` ile güncellenebilir. Ürün kabulde ürün bulunamazsa `product.notFound` hatası verilir.

---

## İş Süreçleri

### Süreç 1 — Tekil Barkod Üretimi

**Tetikleyici:** Ürün girişi öncesinde operatör veya sistem barkod oluşturma talebinde bulunur.

**Gerekli bilgiler:**

| Bilgi | Açıklama | Zorunlu |
|---|---|---|
| Ürün bilgisi | Stok kodu, stok adı, firma kodu, birim vb. | Evet |
| Müşteri kodu | Ürünü gönderen müşterinin kodu | Evet |
| ERP sipariş numarası | Bu girişe ait ERP'deki sipariş no | Evet |
| Giriş tarihi | `YYAAGG` formatında (örn: `260611` = 11 Haziran 2026) | Evet |
| Adet | Kaç adet tekil barkod üretileceği | Evet |
| Miktar | Her birimin miktarı (kg, adet vb.) | Evet |
| Ek açıklama | Serbest alan, isteğe bağlı ek bilgi | Hayır |

**Sonuç:** Sisteme `adet` kadar tekil barkod kaydedilir, her biri farklı lot numarasına sahiptir. Tümü `CREATED` durumundadır.

**Lot numarası kuralı:** Aynı ürün + aynı giriş tarihine ait en yüksek lot numarasından devam edilir. Eş zamanlı istekler çakışmayacak şekilde sıralanır.

---

### Süreç 2 — Ürün Kabul Taraması

**Tetikleyici:** Fiziksel ürün depoya geldiğinde, operatör ürün kabul ekranında barkodu okutur.

**Sistem ne yapar:**

1. Okutulan barkodu kayıtlarda arar.
2. Barkodun durumunu kontrol eder.
3. Gönderilen miktarı barkod üzerindeki miktarla karşılaştırır.
4. Sipariş satırının işlem miktarını barkod miktarıyla karşılaştırır.
5. Tüm kontroller geçerse barkodu `RECEIVING_SCANNED` yapar ve ilgili sipariş satırına bağlar.

---

## İş Kuralları

### Barkod Üretimi

**K-01:** Her tekil barkod sistemde benzersizdir; aynı barkod iki kez kaydedilemez.

**K-02:** Giriş tarihi `YYAAGG` formatında olmalıdır (örn: `260611`). Başka format kabul edilmez.

**K-03:** Adet pozitif tam sayı olmalıdır.

**K-04:** Miktar pozitif sayısal değer olmalıdır, sıfır ve negatif kabul edilmez.

**K-05:** Ürün bilgisi (stok kodu + firma kodu) zorunludur; eksik ürün bilgisiyle barkod üretilemez.

**K-06:** Aynı ürün + aynı giriş tarihi için lot numaraları mevcut en yüksek değerden devam eder. Aynı gün 5 barkod üretildiyse ertesi gün yeni üretim lot 6'dan başlar.

---

### Ürün Kabul Taraması

**K-07:** Okutulan barkod sistemde kayıtlı olmalıdır; tanınmayan barkod kabul edilmez.

**K-08:** Daha önce kabul edilmiş (`RECEIVING_SCANNED`) bir barkod tekrar okutulamaz.

**K-09:** Operatörün girdiği miktar, barkodun kayıtlı miktarıyla tam olarak eşleşmelidir; fazla veya eksik miktar kabul edilmez.

**K-10:** Sipariş satırındaki işlem miktarı, barkodun kayıtlı miktarıyla eşleşmelidir. Bu kural, barkodun yanlış sipariş satırına bağlanmasını önler.

---

## Hata Mesajları ve Açıklamaları

| Hata Kodu | Ne zaman çıkar | Kullanıcıya anlamı |
|---|---|---|
| `notFound` | Okutulan barkod sistemde kayıtlı değil | Bu barkod tanınmıyor. Doğru ürünü okuttuğunuzdan emin olun. |
| `alreadyScanned` | Barkod daha önce kabul taramasından geçmiş | Bu ürün zaten kabul edildi. Aynı birim iki kez kabul edilemez. |
| `quantityMismatch` | Girilen miktar barkod miktarıyla uyuşmuyor | Girdiğiniz miktar bu barkodun miktarıyla eşleşmiyor. Barkod miktarı: X, girilen: Y. |
| `transactionAmountMismatch` | Sipariş satırı işlem miktarı barkod miktarıyla uyuşmuyor | Bu barkod ilgili sipariş satırının miktarıyla uyuşmuyor. Doğru barkodu okuttuğunuzdan emin olun. |

---

## Takip ve Geçmiş (Audit)

Tekil barkod üzerinde yapılan her değişiklik (durum geçişi, sipariş bağlantısı vb.) otomatik olarak kayıt altına alınır:

- Kim değiştirdi
- Ne zaman değiştirdi
- Değişiklikten önceki ve sonraki değer

Bu kayıtlar silinmez; geriye dönük her barkodun geçmişi izlenebilir.

---

## Sorgulama

Tekil barkodlar şu kriterlere göre listelenebilir:

| Filtre | Açıklama |
|---|---|
| ERP Sipariş Bilgisi | Belirli bir siparişe ait tüm barkodları getir |
| Sayfalama | Büyük listeler için sayfa sayfa getirme (sayfa no + sayfa boyutu) |
| Sıralama | ID'ye göre artan/azalan |

---

## Geliştirme Fazları

### Faz 1 — Temel Altyapı (Tamamlandı)

- `UniqueBarcode` entity: state machine, Hibernate Envers audit, tüm kolonlar
- `UniqueBarcodeState`: `CREATED`, `RECEIVING_SCANNED`
- `POST /api/unique-barcodes` — tekil barkod oluşturma
- `POST /api/unique-barcodes/bulk` — toplu barkod oluşturma
- `GET /api/unique-barcodes` — sayfalı listeleme (`erpOrderInfo`, `barcode` filtresi)
- `AurOrderMasterService` entegrasyonu: ürün kabul akışında `updateToReceivingScanned` çağrısı
- `AurOrderDetailResponseDTO` + mapper: `uniqueBarcodeList` alanı `lastModifiedDate`'e göre azalan sıralı

---

### Faz 2 — Akıllı Barkod Üretimi ve Miktar Toleransı (Tamamlandı)

**Hedef:** Yeniden barkod üretim taleplerinde gereksiz barkod oluşturmayı önle; miktar limitinde %10 esneklik sağla.

#### Akıllı Lot Takibi

`adet` artık "kaç tane üret" değil, "kaç tane CREATED barkod olsun" anlamına gelir.

**Akış:**

```
1. Bu partiCode için mevcut CREATED barkod sayısı sorgulanır
2. toCreate = dto.adet - mevcutCreatedSayısı
3. toCreate > 0 ise → eksik kadar yeni barkod üretilir (maxLot'tan devam)
4. toCreate <= 0 ise → yeni barkod üretilmez
5. Response: tüm CREATED barkodlar döner (eski + yeni)
```

**Örnek:**

| Adım | Açıklama | Sonuç |
|---|---|---|
| İstek 1 | `adet: 10` → 0 CREATED mevcut | Lot 1–10 üretilir, 10 barkod döner |
| 2 barkod okutulur | Lot 2 ve 3 → `RECEIVING_SCANNED` | 8 CREATED kalır |
| İstek 2 | `adet: 10` → 8 CREATED mevcut | Lot 11–12 üretilir, toplam 10 CREATED döner |
| İstek 3 | `adet: 10` → 10 CREATED mevcut | Hiç üretilmez, mevcut 10 döner |

#### Miktar Limiti (%10 Tolerans)

`referenceAmount` kontrolünde %10 fazlasına izin verilir.

```
allowedLimit = referenceAmount × 1.10
existingTotal + incomingTotal > allowedLimit → hata
```

Hata mesajında kullanıcıya gerçek limit (`allowedLimit`) gösterilir.

---

### Faz 3 — Geçici Adres, Hibrit Takip ve Ürün Kabul Entegrasyonu (Tamamlandı)

**Hedef:** Tekil barkodu ürün kabul/adres akışına bağlamak, lot bazlı ürünler için hibrit takip kurmak ve aggregate stok kaydını tek kaynaktan beslemek.

- **`IN_TEMPORARY_AREA` durumu + state machine event'leri** (`RECEIVE_SCAN`, `CANCEL_RECEIVE_SCAN`, `TRANSFER_TO_TEMPORARY_AREA`).
- **`UniqueBarcode.address`** (`@ManyToOne → AurDepoUrunAdres`, kolon `address_id`) — geçici alana transferde atanır.
- **`Product.lotBasedTracking` flag'i** — tekil barkod / lot bazlı ayrımı. `createUniqueBarcodes` lot bazlı üründe kayıt atmaz (parti/lot `0000`).
- **Ürün kabul ayrımı flag ile:** `completeReceivingAddressOperation` artık `UnitOfMeasure` yerine `lotBasedTracking`'e bakar. Tekil → `transferDetailBarcodesToTemporaryArea`; lot bazlı → `assignProductToAddress`.
- **Aggregate stok güncelleme:** Tekil barkodlar `IN_TEMPORARY_AREA`'ya geçince `aur_depo_urun_adres_stok` kaydı `SUM(quantity)` ile **SET** edilir (async `@TransactionalEventListener`). Tekil barkod source-of-truth, adres stok türetilmiş kayıt.
- **Sipariş askıya alma (`suspendOrder`):** Askıya alınan siparişin detaylarına ait `RECEIVING_SCANNED` barkodlar `cancelReceiveScanByOrderDetail` ile `CREATED`'a geri çekilir. Bu işlem **state machine'in `CANCEL_RECEIVE_SCAN` event'i üzerinden** yapılır (SM'nin ilk fiili kullanımı); revert mantığı `cancelReceiveScanAction`'da tek kaynakta toplanır (`CREATED` + `aurOrderDetail = null`).

#### Product CRUD Endpoint'leri

- `POST /api/product` — tek ürün oluşturma (`ProductWithoutAddressDTO`, aynı barkod+companyCode varsa `product.alreadyExists`).
- `PATCH /api/product` (`application/merge-patch+json`) — partial update; şu an yalnızca `stokAdi` ve `lotBasedTracking` güncellenir.
- `GET /api/product` + `/count` + `-via-amount` — kriterli listeleme. `multiSearch` ile barkod/stok kodu/stok adı üzerinde tek alanlı OR araması; `lotBasedTracking.equals` filtresi.

#### Mimari / Refactor

- **`ReceivingAddressService`** — `completeReceivingAddressOperation` `MikroServices`'ten ayrı servise taşındı.
- **`ErpTokenService`** — `getToken`/`fetchToken` `MikroServices`'ten ayrıştırıldı; `ProductService`, `ProductAddressService`, `OrderService`, `BarcodeService` token için bunu kullanır. Bu sayede **constructor circular dependency** giderildi (Spring Boot 2.4.7'de field cycle serbest, constructor cycle değil).
- Hata mesajları i18n dosyalarına taşındı (`product.notFound`, `product.alreadyExists`).

---

### Sonraki Aşamalar

- Saha okutma için REST endpoint'leri: `RECEIVE_SCAN` / `TRANSFER_TO_TEMPORARY_AREA` (şu an yalnızca kabul akışından tetikleniyor; `CANCEL_RECEIVE_SCAN` ise `suspendOrder` ile state machine üzerinden devrede)
- `RECEIVE_SCAN` ve `TRANSFER_TO_TEMPORARY_AREA` akışlarının da (şu an doğrudan `setStatus`) state machine'e taşınarak tek mekanizmada birleştirilmesi
- Tek bir barkodun detayını getirme (`GET /api/unique-barcodes/{barcode}`)
- Filtre genişletmesi: durum, parti kodu, tarih aralığı, adres
- `UniqueBarcodeResponseDTO`'ya adres alanının eklenmesi
- Durum geçişlerinin genişletilmesi: sevkiyat, iade, sayım
- `transactionAmountMismatch` ve `quantityMismatch` kurallarının tek kurala indirgenmesi değerlendirmesi
- Kalan `getToken` çağrılarının (AurPartialDetailsService, DepolarArasiTransferService, Resource/Scheduler katmanı) `ErpTokenService`'e taşınması