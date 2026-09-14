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
| `warehouse` | `1 / Merkez Depo` |
| `aur_role` | `ADMIN` |
| `aur_user` | `admin` ve `user` kullanicilarina `company_code = 1` |
| `aur_user_role_rel` | her iki kullanici da `ADMIN` rolunde |
| `user_depo_rel` | her iki kullanici da Merkez Depo'ya yetkili |
| `aur_menu` | — menuler artik seed'de degil, `2026091300000002_added_data_AurMenu.xml` changeset'inde |
| `aur_menu_role_rel` | tum menuler `ADMIN` rolune bagli |
| `product` | 5 demo urun (`8690000000011`..`59`) |
| `aur_erp_data` | 3 siparis / 7 satir, `320.01.001` ve `320.01.002` |
| `aur_order_master` / `aur_order_detail` | `A-1001`'den devam eden bir mal kabul siparisi (3 acik satir) |
| `pallet_barcode` / `pallet_barcode_order_rel` | `9999990000026` paleti, siparisin ilk satirina bagli |

Menuler `company_code = NULL` ile eklenir; `aur_vw_user_menu_rel` sorgusu
`company_code = :companyCode or company_code is null` filtreledigi icin
tum sirketlerde gorunurler.

## Calistirma

    docker exec -i wms-postgres psql -U wms -d wms < seed/local-seed.sql

Idempotenttir (`ON CONFLICT DO NOTHING`), tekrar tekrar calistirilabilir.

## Dogrulama

    TOKEN=$(curl -s -X POST http://localhost:8080/api/authenticate \
      -H 'Content-Type: application/json' \
      -d '{"username":"admin","password":"admin"}' | jq -r .id_token)

    curl -s http://localhost:8080/api/menu-tree -H "Authorization: Bearer $TOKEN" | jq
