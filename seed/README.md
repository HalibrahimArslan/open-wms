# Lokal Seed Verisi

`local-seed.sql`, bos bir veritabanini lokalde kullanilabilir hale getirir.

Menu agaci `../depoyonetim-web/src/routes/AppWithState.jsx` icindeki React Router
rotalarindan cikarilmistir; `path` degerleri UI'in `navigate()` cagrilariyla birebir
eslesir. `:menuId` parametresi iceren rotalar (`firmlist`, `unique-barcode-firmlist`,
`cari-selection`) `index = true` olarak isaretlenmistir; UI bunlari
`/d:<depoKodu>/<menuId>/<path>` seklinde acar.

Iceridigi kayitlar:

| Tablo | Kayit |
|---|---|
| `aur_company` | `1 / WMS Lokal` |
| `warehouse` | `1 / Merkez Depo`, `2 / Yan Depo` (`transfer_code` ve `receiving_code` depo koduyla ayni) |
| `aur_role` | `ADMIN` |
| `aur_user` | `admin` ve `user` kullanicilarina `company_code = 1` |
| `aur_user_role_rel` | her iki kullanici da `ADMIN` rolunde |
| `jhi_user_authority` | `admin`e `COUNTER` yetkisi (el terminalinde sayim listesi icin) |
| `user_depo_rel` | her iki kullanici da iki depoya yetkili |
| `aur_menu` | — menuler artik seed'de degil, `2026091300000002_added_data_AurMenu.xml` changeset'inde |
| `aur_menu_role_rel` | tum menuler `ADMIN` rolune bagli |
| `product` | 5 demo urun (`8690000000011`..`59`) |
| `aur_erp_data` | mal kabul (`sip_tip = 1`): 3 siparis / 7 satir, `320.01.001` ve `320.01.002` |
| `aur_erp_data` | sevkiyat (`sip_tip = 0`): `S-3001` / 3 satir, `320.02.001` |
| `aur_adres_*` | adres bilesenleri (tek haneli unite/kat/goz kodlari, adresler 6 hane). Merkez Depo: 1 bolum, 4 reyon, `RAF` + `KNT` + `GEC`; Yan Depo: 1 bolum, 2 reyon, `RAF` + `GEC` |
| `aur_depo_urun_adres` | Merkez Depo: `A01111` ve `A02111` toplama gozleri, `A03111` kontrol adresi, `A99111` gecici adres; Yan Depo: `B99111` gecici adres, `B01111` toplama gozu |
| `aur_depo_urun_adres_stok` | `S-3001` satirlarinin urunleri toplama gozlerinde (250 / 180 / 60) |
| `aur_order_master` / `aur_order_detail` | `A-1001`'den devam eden bir mal kabul siparisi (3 acik satir) |
| `aur_order_master` / `aur_order_detail` | `S-3001`'den `admin`e atanmis bir sevkiyat siparisi (`MSK`, 3 satir) |
| `order_picking_transaction` | sevkiyat satirlarinin acilis (sifir miktarli) toplama hareketleri |
| `pallet_barcode` / `pallet_barcode_order_rel` | `9999990000026` paleti, siparisin ilk satirina bagli |

## Sevkiyat akisini denemek

Sevkiyat ekranlari siparisleri `sip_tip = 0` ile okur; seed bu tipte tek bir siparis
(`S-3001`), bu siparisin urunlerini tasiyan adres/stok kayitlarini ve siparisin `admin`
kullanicisina atanmis halini kurar. Yani **Sevkiyat > Sevkiyatlarim** ekrani acildiginda
siparis hazir gelir; toplamada okutulacak adres barkodlari `A01111` (STK-001,
STK-003) ve `A02111` (STK-005), kontrol adresi ise `A03111`'dir.

Atama adimini da denemek istersen bu siparisin WMS kayitlarini silmek yeterli
(`order_picking_transaction`, `aur_order_detail`, `aur_order_master`); siparis o zaman
**Cari Secimi > Ege Ticaret A.S.** ekraninda yeniden atanabilir hale gelir. Satirlar
ancak atanmamisken secilebilir gorunur, atanmis satirlar "aktif" isaretlenir.

`warehouse.transfer_code` degeri depo koduyla ayni tutulur: UI adres, stok ve kontrol
adresi sorgularinda depo kodu yerine bu alani kullaniyor, farkli olursa toplama
ekranlari bos gelir.

Siparisin son adimi olan ERP'ye sevkiyat bildirimi yerel modda heniz yazilmadi
(`LocalOrderGateway.dispatchOrder` -> `notImplemented`), yani toplama sonrasi
`aur_erp_data.sip_teslim_miktar` otomatik artmaz.

Menuler `company_code = NULL` ile eklenir; `aur_vw_user_menu_rel` sorgusu
`company_code = :companyCode or company_code is null` filtreledigi icin
tum sirketlerde gorunurler.

## Depolar arasi transferi ve gecici adresten yerlestirmeyi denemek

**Depolar Arasi Transfer** ekraninda cikis depo Merkez Depo, giris depo Yan Depo
secilip `A01111` adresinden bir urun (orn. `8690000000011`) aktarilir; urun Yan
Depo'nun gecici adresi `B99111`'e duser. Ardindan depo secicisinden Yan Depo'ya
gecilip **Gecici Adresten Yerlestirme** ekraninda `B01111` adresi ve ayni barkod
okutularak urun rafa yerlestirilir.

Ters yon de denenebilir: Yan Depo'dan Merkez Depo'ya aktarilan urun `A99111`
gecici adresine duser ve Merkez Depo'da `A01111` gibi bir rafa yerlestirilir.

Mal kabul ve transfer, urunu giris deposunun `receiving_code` degerine gore
buldugu gecici adrese koyar; bu yuzden her depoda `receiving_code` depo koduyla
ayni ve tek bir gecici adres vardir. Birden fazla gecici adres desteklenmez.

## Calistirma

    docker exec -i wms-postgres psql -U wms -d wms < seed/local-seed.sql

Idempotenttir (`ON CONFLICT DO NOTHING`), tekrar tekrar calistirilabilir.

Uygulama calisirken seed yuklenirse degisiklikler hemen gorunmeyebilir: kullanicilar,
depolar ve roller onbellekte tutulur (varsayilan omur 1 saat). Onbellegi bosaltmak icin
uygulamayi yeniden baslatin ya da admin token'iyla:

    curl -X DELETE -H "Authorization: Bearer <token>" http://localhost:8080/management/caches

## Dogrulama

    TOKEN=$(curl -s -X POST http://localhost:8080/api/authenticate \
      -H 'Content-Type: application/json' \
      -d '{"username":"admin","password":"admin"}' | jq -r .id_token)

    curl -s http://localhost:8080/api/menu-tree -H "Authorization: Bearer $TOKEN" | jq
