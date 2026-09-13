# ERP entegrasyon kurali: tek sozlesme, adaptor deseni

Bu projede ERP entegrasyonlari **tek bir arayuz + adaptor** deseniyle yazilir.
Amac, ERP'ler degistiginde ya da yenisi eklendiginde uygulamanin geri kalanina
dokunmak zorunda kalmamaktir.

## Yapi

| Parca | Sorumluluk |
|---|---|
| `service/erp/ErpOrderGateway` | Tek sozlesme. Tum ERP islemleri burada tanimlidir. |
| `service/erp/ErpGatewayRegistry` | Adaptorleri `erpTypes()` degerine gore indeksler. |
| `service/erp/ErpGatewayRouter` | Sirketin `erpTipi` degerine gore adaptoru secer, token/apiPath uretir. `@Primary`. |
| `service/erp/ErpModeResolver` | Entegrasyonun acik olup olmadigini belirler. |
| `web/rest/ErpOrderResource` | ERP'den bagimsiz tek REST ucu. |

Mevcut adaptorler: `LocalOrderGateway` (LOCAL), `MikroServices` (MIKRO_V16 + MIKRO_V15),
`NetsisServices` (NETSIS), `UyumsoftOrderGateway` (UYUMSOFT).

## Kurallar

1. **Resource katmaninda ERP'ye ozel dallanma yazma.**
   `if (ErpConnectionType.X == erpCode) { ... }` gibi kontroller `ErpOrderResource`
   icine girmez. Secim `ErpGatewayRouter`'in isidir.

2. **Yeni ERP eklemek = yeni bir `@Service`.**
   `ErpOrderGateway` arayuzunu uygula, `erpTypes()` ile karsiladigin
   `ErpConnectionType` degerlerini bildir. Registry, router, resource ve istemci
   sozlesmesi degismez; baska hicbir dosyaya dokunma.

3. **Kanonik DTO dondur.**
   Adaptorler `AurCariDto`, `AurCariOrderDto`, `AurCariOrderDetailListDto`,
   `AurCariOrderDetailDto`, `StockDetailResponseDto` dondurur. ERP'nin ham JSON'unu
   `Object` olarak disari sizdirma; yanitlar hangi ERP bagli olursa olsun ayni
   sekle sahip olmalidir. Ortak zenginlestirme icin
   `AurOrderMasterService.enrichDepoOrderDetails(...)` kullanilir.

4. **Heniz yazilmamis islem icin de metot tanimli olur.**
   Istisna firlatma; `ErpOperationResult.notImplemented("...")` ya da bos koleksiyon
   dondur ve `TODO` ile hangi is kuralinin eksik oldugunu yaz. Is kurallari sonradan
   eklenebilir, sozlesme bastan tam olmalidir.

5. **Entegrasyonsuz calisma varsayilan olarak desteklenir.**
   `erpTipi = LOCAL` ya da `apiParameters` icindeki `erpApiActive != "1"` oldugunda
   `LocalOrderGateway` devreye girer ve siparisler yerel `aur_erp_data` tablosundan
   okunur. Kayitli adaptoru olmayan bir ERP tipinde de yerel adaptore dusulur.

6. **ERP adresleri ve kimlik bilgileri koda gomulmez.**
   Endpoint, kullanici, token gibi degerler `application*.yml` icinde `${ENV_VAR:}`
   olarak tanimlanir; sirket bazli olanlar `aur_company.api_parameters` alanindan gelir.
