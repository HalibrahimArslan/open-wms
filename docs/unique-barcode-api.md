# Unique Barcode API Dokümantasyonu

## Endpoint'ler

### `POST /api/unique-barcodes`

Tekil barkod oluşturur. `adet` alanı **hedef CREATED barkod sayısını** belirtir; mevcut duruma göre eksik kadar üretilir.

**Request Body:** `UniqueBarcodeCreateDTO`  
**Response:** `List<UniqueBarcodeResponseDTO>` — o `partiCode` için tüm `CREATED` statüsündeki barkodlar

#### Akıllı Üretim Mantığı

| Durum | Ne olur |
|---|---|
| Hiç CREATED barkod yok | `adet` kadar barkod üretilir |
| Kısmen CREATED barkod var | Eksik kadar üretilir, toplamda `adet` adet CREATED sağlanır |
| `adet` kadar veya fazla CREATED var | Yeni barkod üretilmez, mevcut CREATED'lar döner |

> **Örnek:** Daha önce 10 barkod üretildi, 2'si okutuldu (RECEIVING_SCANNED). 8 CREATED kaldı.
> `adet: 10` ile yeni istek gelirse → lot 11 ve 12 üretilir, response'ta 10 CREATED barkod döner.

#### Miktar Limiti

`referenceAmount` kontrolünde **%10 tolerans** uygulanır:

```
allowedLimit = referenceAmount × 1.10
(mevcutToplam + yeniToplam) > allowedLimit → hata
```

#### Lot Bazlı Ürünler

Ürünün `lotBasedTracking = true` ise bu endpoint **kayıt oluşturmaz**: `adet` kadar barkod string'i üretilir, `partiCode = "0000"` ve `lotNumber = 0` ile DTO olarak döner ama veritabanına yazılmaz (lock/limit kontrolü atlanır). `false` (varsayılan) ise yukarıdaki akıllı üretim + persist akışı işler.

---

### `POST /api/unique-barcodes/bulk`

Birden fazla `UniqueBarcodeCreateDTO` tek seferde gönderilir. Her DTO bağımsız olarak işlenir; aynı `partiCode`'a ait olanlar kendi aralarında akıllı üretim mantığına tabi tutulur.

**Request Body:** `List<UniqueBarcodeCreateDTO>`  
**Response:** `List<UniqueBarcodeResponseDTO>` — tüm DTO'ların CREATED barkodlarının birleşimi

---

### `GET /api/unique-barcodes`

Tekil barkodları sayfalı ve filtreli listeler.

**Query Parameters:**

| Parametre | Tip | Açıklama |
|---|---|---|
| `erpOrderInfo.equals` | `String` | ERP sipariş numarasına göre tam eşleşme |
| `erpOrderInfo.contains` | `String` | ERP sipariş numarasında içerir |
| `barcode.equals` | `String` | Tekil barkod değerine göre tam eşleşme |
| `barcode.contains` | `String` | Tekil barkod değerinde içerir |
| `page` | `int` | Sayfa numarası (0'dan başlar) |
| `size` | `int` | Sayfa başı kayıt sayısı |
| `sort` | `string` | Sıralama: `id,asc` / `id,desc` |

**Response:** `List<UniqueBarcodeResponseDTO>` (header'da pagination bilgisi)

---

## Product Endpoint'leri

Tekil barkod / lot bazlı takip ayrımı `Product.lotBasedTracking` flag'i üzerinden yapıldığı için ürün yönetimi endpoint'leri buraya dahildir.

### `POST /api/product`

Tek ürün oluşturur.

- **Body:** `ProductWithoutAddressDTO` (`@Valid` — tüm zorunlu alanlar)
- **Response:** `ProductWithoutAddressDTO`
- Aynı `barcode` + `companyCode` ile ürün varsa `400 productAlreadyExists`.

### `PATCH /api/product`

Mevcut ürünü kısmi günceller (JSON Merge Patch).

- **Content-Type:** `application/merge-patch+json`
- **Body:** `ProductWithoutAddressDTO` — `barcode` + `companyCode` ile ürün bulunur
- **Güncellenebilir alanlar:** yalnızca `stokAdi` ve `lotBasedTracking` (null gönderilen alan atlanır)
- **Response:** `ProductWithoutAddressDTO`
- Ürün yoksa `400 productNotFound`.

### `GET /api/product` (+ `/count`, `-via-amount`)

Ürünleri kriterli ve sayfalı listeler.

| Query Parametresi | Açıklama |
|---|---|
| `multiSearch.contains` | Barkod **veya** stok kodu **veya** stok adı üzerinde tek alanlı OR araması |
| `lotBasedTracking.equals` | `true` → sadece lot bazlı, `false` → sadece tekil barkod ürünleri |
| `barkod`, `stokKodu`, `stokAdi`, `companyCode`, `depoCode` | İlgili alan filtreleri (`.equals`, `.contains`) |

---

## DTO'lar

### `UniqueBarcodeCreateDTO` — POST body

| Alan | Tip | Zorunlu | Kural | Açıklama |
|---|---|---|---|---|
| `product` | `ProductWithoutAddressDTO` | **Evet** | `@NotNull`, iç alanlar da valide edilir | Ürün bilgisi |
| `customerCode` | `String` | **Evet** | `@NotBlank` | Müşteri kodu |
| `erpOrderNo` | `String` | **Evet** | `@NotBlank` | ERP sipariş numarası |
| `receivingDate` | `String` | **Evet** | `yyMMdd` formatı (örn: `260611`) | Giriş tarihi |
| `adet` | `Integer` | **Evet** | `@Positive`, tam sayı | Hedef CREATED barkod sayısı |
| `quantity` | `BigDecimal` | **Evet** | `@Positive` | Her barkodun miktarı |
| `referenceAmount` | `BigDecimal` | **Evet** | `@Positive` | Sipariş referans miktarı (limit = referenceAmount × 1.10) |
| `description` | `Map<String, Object>` | Hayır | — | Serbest ek bilgi alanı |

#### Stok Birimi Kuralları

| Stok Birimi | Kural |
|---|---|
| `ADET` | `quantity` 1 olmak zorunda |
| `METRE`, `METREKARE`, `METRETUL`, `KILOGRAM` | `adet` 1 olmak zorunda |

#### `product` alanı — `ProductWithoutAddressDTO`

| Alan | Tip | Zorunlu | Açıklama |
|---|---|---|---|
| `barcode` | `String` | **Evet** | Ürün barkodu |
| `companyCode` | `String` | **Evet** | Firma kodu |
| `stokAdi` | `String` | **Evet** | Stok adı |
| `stokKodu` | `String` | **Evet** | Stok kodu |
| `anaGrup` | `String` | **Evet** | Ana grup |
| `kategoriAdi` | `String` | **Evet** | Kategori adı |
| `stokBirimi` | `String` | **Evet** | Stok birimi (`ADET`, `KG`, `METRE` vb.) |
| `miktar` | `Double` | Hayır | Miktar |
| `description` | `String` | Hayır | Ürün açıklaması |
| `sktFlag` | `Boolean` | Hayır | SKT takibi açık mı? (default: `false`) |
| `lotBasedTracking` | `Boolean` | Hayır | `true` ise ürün lot bazında takip edilir; tekil barkod kaydı atılmaz (default: `false`) |

---

### `UniqueBarcodeResponseDTO` — Response

| Alan | Tip | Açıklama |
|---|---|---|
| `id` | `Long` | Barkod ID'si |
| `barcode` | `String` | Üretilen tekil barkod değeri |
| `partiCode` | `String` | Parti kodu (`erpBarkod + receivingDate`) |
| `lotNumber` | `Long` | Lot numarası |
| `status` | `UniqueBarcodeState` | `CREATED` \| `RECEIVING_SCANNED` \| `IN_TEMPORARY_AREA` |
| `quantity` | `BigDecimal` | Miktar |
| `receivingDate` | `Instant` | Giriş tarihi (ISO 8601) |
| `customerCode` | `String` | Müşteri kodu |
| `erpOrderInfo` | `String` | ERP sipariş bilgisi |
| `description` | `Map<String, Object>` | Ek bilgi |
| `lastModifiedDate` | `Instant` | Son değişiklik tarihi |
| `stokKodu` | `String` | Stok kodu |
| `stokAdi` | `String` | Stok adı |
| `anaGrup` | `String` | Ana grup |
| `kategoriAdi` | `String` | Kategori adı |
| `stokBirimi` | `String` | Stok birimi |
| `barkod` | `String` | Ürünün orijinal ERP barkodu |

---

## Hata Kodları

| HTTP | `errorKey` | Ne zaman |
|---|---|---|
| 400 | `quantityLimitExceeded` | `mevcutToplam + yeniToplam > referenceAmount × 1.10` |
| 400 | `invalidQuantityForAdet` | ADET birimli üründe `quantity != 1` |
| 400 | `invalidAdetForUnit` | KG/METRE vb. birimde `adet != 1` |
| 400 | `notFound` | Okutulmak istenen barkod sistemde yok |
| 400 | `alreadyScanned` | Barkod zaten `RECEIVING_SCANNED` |
| 400 | `quantityMismatch` | Operatör miktarı barkod miktarıyla uyuşmuyor |
| 400 | `transactionAmountMismatch` | Sipariş satırı işlem miktarı barkod miktarıyla uyuşmuyor |
| 400 | `productNotFound` | Güncellenmek/kabul edilmek istenen ürün (`barcode`+`companyCode`) bulunamadı |
| 400 | `productAlreadyExists` | `POST /api/product` ile aynı `barcode`+`companyCode` zaten mevcut |

---

## Örnek İstekler

### Tekil Oluşturma

```json
POST /api/unique-barcodes
{
  "product": {
    "barcode": "8690123456789",
    "companyCode": "AUR",
    "stokAdi": "Örnek Ürün",
    "stokKodu": "STK001",
    "anaGrup": "HAMMADDE",
    "kategoriAdi": "KAT-A",
    "stokBirimi": "KG"
  },
  "customerCode": "MUS001",
  "erpOrderNo": "2025001",
  "receivingDate": "260611",
  "adet": 10,
  "quantity": 50.000,
  "referenceAmount": 500.000
}
```

**Response:** O `partiCode` için tüm `CREATED` barkodlar (yeni + mevcut)

### Toplu Oluşturma

```json
POST /api/unique-barcodes/bulk
[
  { ...DTO1... },
  { ...DTO2... }
]
```
