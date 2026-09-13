-- =====================================================================
-- WMS lokal gelistirme seed verisi
--
-- Menu agaci ../depoyonetim-web/src/routes/AppWithState.jsx icindeki
-- rotalardan cikarilmistir. Menunun UI'da gorunebilmesi icin gereken
-- sirket / rol / depo kayitlari da burada olusturulur.
--
-- Calistirmak icin:
--     docker exec -i wms-postgres psql -U wms -d wms < seed/local-seed.sql
--
-- Idempotenttir, birden fazla kez calistirilabilir.
-- =====================================================================

BEGIN;

-- ---------------------------------------------------------------- sirket
INSERT INTO aur_company (id, company_code, company_name)
VALUES (1, 1, 'WMS Lokal')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------- depo
INSERT INTO warehouse (id, code, name, company_code, is_real, countable,
                       transfer_code, auto_scan, unique_picking_address,
                       picking_rule_type, receiving_code)
VALUES (1, '1', 'Merkez Depo', '1', true, true, '8', false, false, 'DEFAULT', '0')
ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------- kullanicilarin sirketi
UPDATE aur_user SET company_code = 1 WHERE login IN ('admin', 'user') AND company_code IS NULL;

-- ------------------------------------------------- demo parolalari
-- Lokal demo kullanicilarinin parolalarini JHipster varsayilanlarina sabitler:
--   admin / admin      user / user
-- (hash'ler config/liquibase/data/user.csv ile ayni)
--
-- password_version = 1 onemli: UserService, versiyon 0 gorunce parolayi
-- "Depoyonetim_20xx!*" generic parolasiyla degistiriyor.
UPDATE aur_user SET password_hash = '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC',
                    password_version = 1
 WHERE login = 'admin';
UPDATE aur_user SET password_hash = '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K',
                    password_version = 1
 WHERE login = 'user';

-- ---------------------------------------------------------------- rol
INSERT INTO aur_role (id, role_name, company_code)
VALUES (1, 'ADMIN', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO aur_user_role_rel (user_id, role_id)
SELECT u.id, 1 FROM aur_user u WHERE u.login IN ('admin', 'user')
ON CONFLICT DO NOTHING;

-- ---------------------------------------------------------------- depo yetkisi
INSERT INTO user_depo_rel (id, user_id, warehouse_id, created_by, created_date)
SELECT u.id, u.id, 1, 'system', now()
FROM aur_user u WHERE u.login IN ('admin', 'user')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------- menuler
-- parent_menu_id = 0  -> kok menu
-- index = true        -> UI /d:<depo>/<menuId>/<path> seklinde yonlendirir
--                        (rotada :menuId parametresi olan ekranlar)
-- company_code = NULL -> tum sirketlerde gorunur
INSERT INTO aur_menu (id, parent_menu_id, menu_name, menu_type, path, index, icon, company_code) VALUES
    (1,   0,  'Kontrol Paneli',              'TERMINAL', 'dashboard',                            false, 'mdi:view-dashboard',        NULL),

    (10,  0,  'Mal Kabul',                   'TERMINAL', NULL,                                   false, 'mdi:truck-delivery',        NULL),
    (11,  10, 'Firmadan Mal Kabul',          'TERMINAL', 'firmlist',                             true,  'mdi:domain',                NULL),
    (12,  10, 'Tekil Barkodlu Mal Kabul',    'TERMINAL', 'unique-barcode-firmlist',              true,  'mdi:barcode-scan',          NULL),
    (13,  10, 'Irsaliye Kontrol',            'TERMINAL', 'waybill-control',                      false, 'mdi:file-document-check',   NULL),

    (20,  0,  'Sevkiyat',                    'TERMINAL', NULL,                                   false, 'mdi:truck-fast',            NULL),
    (21,  20, 'Cari Secimi',                 'TERMINAL', 'cari-selection',                       true,  'mdi:account-tie',           NULL),
    (22,  20, 'Sevkiyatlarim',               'TERMINAL', 'dispatchingorder',                     false, 'mdi:clipboard-list',        NULL),

    (30,  0,  'Adres Islemleri',             'TERMINAL', NULL,                                   false, 'mdi:map-marker',            NULL),
    (31,  30, 'Adresler',                    'TERMINAL', 'address',                              false, 'mdi:map-marker-multiple',   NULL),
    (32,  30, 'Kontrol Adres',               'TERMINAL', 'kontroladres',                         false, 'mdi:map-marker-check',      NULL),
    (33,  30, 'Urun Adres Tanimi',           'TERMINAL', 'productaddressdef',                    false, 'mdi:package-variant',       NULL),
    (34,  30, 'Adres Operasyonlari',         'TERMINAL', 'address-operations',                   false, 'mdi:cog-transfer',          NULL),
    (35,  30, 'Urun Adres Degisimi',         'TERMINAL', 'productaddressreplacement',            false, 'mdi:swap-horizontal',       NULL),
    (36,  30, 'Gecici Adresten Yerlestirme', 'TERMINAL', 'productplacement',                     false, 'mdi:archive-arrow-down',    NULL),
    (37,  30, 'Adres Tanimlari',             'TERMINAL', 'address-tanim',                        false, 'mdi:map-marker-plus',       NULL),
    (371, 37, 'Bolum',                       'TERMINAL', 'address-tanim/department',             false, 'mdi:office-building',       NULL),
    (372, 37, 'Reyon',                       'TERMINAL', 'address-tanim/hall',                   false, 'mdi:view-column',           NULL),
    (373, 37, 'Unite',                       'TERMINAL', 'address-tanim/unit',                   false, 'mdi:cube-outline',          NULL),
    (374, 37, 'Kat',                         'TERMINAL', 'address-tanim/flat',                   false, 'mdi:layers',                NULL),
    (375, 37, 'Goz',                         'TERMINAL', 'address-tanim/room',                   false, 'mdi:grid',                  NULL),
    (376, 37, 'Adres Tipi',                  'TERMINAL', 'address-tanim/address-type',           false, 'mdi:shape',                 NULL),

    (40,  0,  'Sayim',                       'TERMINAL', NULL,                                   false, 'mdi:counter',               NULL),
    (41,  40, 'Sayim Islemleri',             'TERMINAL', 'counting',                             false, 'mdi:clipboard-check',       NULL),
    (42,  40, 'Sayim Tanimlari',             'TERMINAL', 'counting-definition',                  false, 'mdi:clipboard-edit',        NULL),

    (50,  0,  'Palet Barkod',                'TERMINAL', 'palletbarcode',                        false, 'mdi:barcode',               NULL),

    (60,  0,  'Raporlar',                    'TERMINAL', NULL,                                   false, 'mdi:chart-bar',             NULL),
    (61,  60, 'Siparis Takibi',              'TERMINAL', 'ordertracing',                         false, 'mdi:magnify',               NULL),
    (62,  60, 'Yerlestirme Gecmisi',         'TERMINAL', 'placementhistory',                     false, 'mdi:history',               NULL),
    (63,  60, 'Performans Kontrol',          'TERMINAL', 'performance-control',                  false, 'mdi:speedometer',           NULL),
    (64,  60, 'Mikro Rapor',                 'TERMINAL', 'report',                               false, 'mdi:file-chart',            NULL),

    (70,  0,  'Tanimlar',                    'TERMINAL', 'definitions',                          false, 'mdi:cog',                   NULL),
    (71,  70, 'Mail Tanimlari',              'TERMINAL', 'definitions/mails',                    false, 'mdi:email',                 NULL),
    (72,  70, 'Menu Tanimlari',              'TERMINAL', 'definitions/menus',                    false, 'mdi:menu',                  NULL),
    (73,  70, 'Kural Tanimlari',             'TERMINAL', 'definitions/rules',                    false, 'mdi:gavel',                 NULL),
    (74,  70, 'Rol Tanimlari',               'TERMINAL', 'definitions/role-definitions',         false, 'mdi:shield-account',        NULL),
    (75,  70, 'Kullanici Rol Tanimlari',     'TERMINAL', 'definitions/user-role-definitions',    false, 'mdi:account-key',           NULL),
    (76,  70, 'Rol-Menu Iliskileri',         'TERMINAL', 'definitions/role-menu-definitions',    false, 'mdi:link-variant',          NULL),
    (77,  70, 'Sofor Tanimlari',             'TERMINAL', 'definitions/driver-definitions',       false, 'mdi:card-account-details',  NULL),
    (78,  70, 'Rezerve Urunler',             'TERMINAL', 'definitions/reserve-products',         false, 'mdi:bookmark',              NULL),

    (80,  0,  'Kullanicilar',                'TERMINAL', 'users',                                false, 'mdi:account-group',         NULL),
    (81,  0,  'Geri Bildirimler',            'TERMINAL', 'feedbacks',                            false, 'mdi:comment-quote',         NULL),
    (82,  0,  'CSV Yukleme',                 'TERMINAL', 'csv-upload',                           false, 'mdi:file-upload',           NULL),
    (83,  0,  'Depolar Arasi Transfer',      'TERMINAL', 'depolararasitransfer',                 false, 'mdi:transfer',              NULL),
    (84,  0,  'Parcali Urunler',             'TERMINAL', 'partial-item',                         false, 'mdi:puzzle',                NULL),
    (85,  0,  'Lot Urun Tanimi',             'TERMINAL', 'lot-product-definition',               false, 'mdi:tag-multiple',          NULL)
ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------- tum menuler ADMIN rolune
INSERT INTO aur_menu_role_rel (menu_id, role_id)
SELECT m.id, 1 FROM aur_menu m
ON CONFLICT DO NOTHING;

-- ---------------------------------------------------------------- local erp modu
-- erp_tipi = 2 (ErpConnectionType.LOCAL) ve erpApiActive = "0":
-- ErpGatewayRouter bu sirket icin LocalOrderGateway'i secer, ERP'ye hic gitmez.
UPDATE aur_company
   SET erp_tipi = '2',
       api_parameters = '{"erpApiActive": "0"}'::jsonb
 WHERE company_code = 1;

-- ---------------------------------------------------------------- urunler
INSERT INTO product (barkod, company_code, stok_kodu, stok_adi, ana_grup, kategori_adi,
                     stok_birimi, miktar, description, skt_flag, lot_based_tracking)
VALUES
    ('8690000000011', '1', 'STK-001', 'Vida M8x40 Galvaniz',   'BAGLANTI', 'Vida',   'ADET', 0, 'Lokal demo urunu', false, false),
    ('8690000000028', '1', 'STK-002', 'Somun M8 Paslanmaz',    'BAGLANTI', 'Somun',  'ADET', 0, 'Lokal demo urunu', false, false),
    ('8690000000035', '1', 'STK-003', 'Pul M8 Genis',          'BAGLANTI', 'Pul',    'ADET', 0, 'Lokal demo urunu', false, false),
    ('8690000000042', '1', 'STK-004', 'Kose Baglanti Sac 90',  'SAC',      'Profil', 'ADET', 0, 'Lokal demo urunu', false, false),
    ('8690000000059', '1', 'STK-005', 'Ray Profil 2m',         'SAC',      'Profil', 'MT',   0, 'Lokal demo urunu', false, false)
ON CONFLICT (barkod, company_code) DO NOTHING;

-- ---------------------------------------------------------------- erp siparis verisi
-- aur_erp_data, ERP entegrasyonu varken disaridan beslenen tablodur. Local modda
-- aur_vw_firm_order_exists / aur_vw_firm_order_detail / aur_vw_firm_orderlist_bulk
-- view'lari bu tablodan okur.
--
-- sip_tip = 1 ve cari_baglanti_tipi = '1' olmali: servis katmani cariBaglantiTipi'yi
-- sipTip'in metin karsiligi olarak gonderiyor.
INSERT INTO aur_erp_data (id, sip_guid, sip_depo_no, sip_sube_no, sip_tip, sip_cins,
                          sip_evrak_seri, sip_evrak_sira, sip_satir_no, sip_belge_no,
                          sip_musteri_kod, sip_stok_kod, stok_adi, barkod, stok_birimi,
                          sip_miktar, sip_teslim_miktar, sip_tarih, teslim_tarihi,
                          planlanan_sevk_tarihi, cari_kod, cari_unvan, bolge_kodu,
                          bolge_adi, cari_hareket_tipi, cari_baglanti_tipi)
VALUES
    -- CARI-001 / SIP-2026-0001 (3 satir, hepsi acik)
    (1, 'uid-0001', 1, 1, 1, 0, 'A', 1001, 1, 'SIP-2026-0001', 'CARI-001', 'STK-001', 'Vida M8x40 Galvaniz',  '8690000000011', 'ADET', 100, 0,  '2026-09-01', '2026-09-10', '2026-09-08', 'CARI-001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    (2, 'uid-0002', 1, 1, 1, 0, 'A', 1001, 2, 'SIP-2026-0001', 'CARI-001', 'STK-002', 'Somun M8 Paslanmaz',   '8690000000028', 'ADET', 200, 50, '2026-09-01', '2026-09-10', '2026-09-08', 'CARI-001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    (3, 'uid-0003', 1, 1, 1, 0, 'A', 1001, 3, 'SIP-2026-0001', 'CARI-001', 'STK-003', 'Pul M8 Genis',         '8690000000035', 'ADET', 150, 0,  '2026-09-01', '2026-09-10', '2026-09-08', 'CARI-001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    -- CARI-001 / SIP-2026-0002 (1 acik, 1 kapanmis satir -> kapanmis olan listelenmemeli)
    (4, 'uid-0004', 1, 1, 1, 0, 'A', 1002, 1, 'SIP-2026-0002', 'CARI-001', 'STK-004', 'Kose Baglanti Sac 90', '8690000000042', 'ADET', 40,  10, '2026-09-03', '2026-09-12', '2026-09-11', 'CARI-001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    (5, 'uid-0005', 1, 1, 1, 0, 'A', 1002, 2, 'SIP-2026-0002', 'CARI-001', 'STK-005', 'Ray Profil 2m',        '8690000000059', 'MT',   25,  25, '2026-09-03', '2026-09-12', '2026-09-11', 'CARI-001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    -- CARI-002 / SIP-2026-0003
    (6, 'uid-0006', 1, 1, 1, 0, 'B', 2001, 1, 'SIP-2026-0003', 'CARI-002', 'STK-001', 'Vida M8x40 Galvaniz',  '8690000000011', 'ADET', 500, 120,'2026-09-05', '2026-09-15', '2026-09-14', 'CARI-002', 'Demir Celik A.S.',    '06', 'Ankara',           '0', '1'),
    (7, 'uid-0007', 1, 1, 1, 0, 'B', 2001, 2, 'SIP-2026-0003', 'CARI-002', 'STK-005', 'Ray Profil 2m',        '8690000000059', 'MT',   80,  0,  '2026-09-05', '2026-09-15', '2026-09-14', 'CARI-002', 'Demir Celik A.S.',    '06', 'Ankara',           '0', '1')
ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------- sequence'lari ileri al
SELECT setval('aur_company_seq',        1000, false);
SELECT setval('aur_menu_seq',           1000, false);
SELECT setval('aur_role_seq',           1000, false);
SELECT setval('aur_menu_role_rel_seq',  1000, false);
SELECT setval('sequence_generator',     1000, false);
SELECT setval('aur_erp_data_seq',       1000, false);
SELECT setval('product_seq',            1000, false);

COMMIT;
