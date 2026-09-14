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
-- Menu agaci artik seed verisi degil: urunun kendi menuleri oldugu icin
-- config/liquibase/changelog/2026091300000002_added_data_AurMenu.xml icinde,
-- migration'la birlikte kuruluyor. Burada yalnizca menuleri role baglama adimi kaldi.

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
    -- 320.01.001 / A-1001 (3 satir, hepsi acik)
    (1, 'uid-0001', 1, 1, 1, 0, 'A', 1001, 1, 'A-1001', '320.01.001', 'STK-001', 'Vida M8x40 Galvaniz',  '8690000000011', 'ADET', 100, 0,  '2026-09-01', '2026-09-10', '2026-09-08', '320.01.001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    (2, 'uid-0002', 1, 1, 1, 0, 'A', 1001, 2, 'A-1001', '320.01.001', 'STK-002', 'Somun M8 Paslanmaz',   '8690000000028', 'ADET', 200, 50, '2026-09-01', '2026-09-10', '2026-09-08', '320.01.001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    (3, 'uid-0003', 1, 1, 1, 0, 'A', 1001, 3, 'A-1001', '320.01.001', 'STK-003', 'Pul M8 Genis',         '8690000000035', 'ADET', 150, 0,  '2026-09-01', '2026-09-10', '2026-09-08', '320.01.001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    -- 320.01.001 / A-1002 (1 acik, 1 kapanmis satir -> kapanmis olan listelenmemeli)
    (4, 'uid-0004', 1, 1, 1, 0, 'A', 1002, 1, 'A-1002', '320.01.001', 'STK-004', 'Kose Baglanti Sac 90', '8690000000042', 'ADET', 40,  10, '2026-09-03', '2026-09-12', '2026-09-11', '320.01.001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    (5, 'uid-0005', 1, 1, 1, 0, 'A', 1002, 2, 'A-1002', '320.01.001', 'STK-005', 'Ray Profil 2m',        '8690000000059', 'MT',   25,  25, '2026-09-03', '2026-09-12', '2026-09-11', '320.01.001', 'Yildiz Makina Ltd.',  '34', 'Istanbul Anadolu', '0', '1'),
    -- 320.01.002 / B-2001
    (6, 'uid-0006', 1, 1, 1, 0, 'B', 2001, 1, 'B-2001', '320.01.002', 'STK-001', 'Vida M8x40 Galvaniz',  '8690000000011', 'ADET', 500, 120,'2026-09-05', '2026-09-15', '2026-09-14', '320.01.002', 'Demir Celik A.S.',    '06', 'Ankara',           '0', '1'),
    (7, 'uid-0007', 1, 1, 1, 0, 'B', 2001, 2, 'B-2001', '320.01.002', 'STK-005', 'Ray Profil 2m',        '8690000000059', 'MT',   80,  0,  '2026-09-05', '2026-09-15', '2026-09-14', '320.01.002', 'Demir Celik A.S.',    '06', 'Ankara',           '0', '1')
ON CONFLICT (id) DO NOTHING;

-- --------------------------------------------- devam eden siparis + palet barkodu
-- A-1001 siparisinin teslim edilmemis satirlarindan bir WMS mal kabul siparisi ve
-- bu siparise bagli bir palet barkodu. Boylece palet ekranlari bos veritabaninda da
-- calisir durumda acilir.
--
-- Palet barkodu EAN-13'tur ve PalletBarcodeService.generatePalletBarcode kuralina
-- uyar: tablo bossa taban 999999000001'dir, bir artirilir ve kontrol hanesi eklenir;
-- yani ilk barkod 999999000002 + kontrol hanesi 6 = 9999990000026. Seed'in urettigi
-- deger uygulamanin uretecegiyle ayni olmali, yoksa bir sonraki uretim bu kaydin
-- ilk 12 hanesini girdi olarak alirken tutarsizlik cikar.
--
-- Blok, barkod zaten varsa hicbir sey yapmaz; seed tekrar tekrar calistirilabilir.
DO $$
DECLARE
    v_order_id  bigint;
    v_detail_id bigint;
    v_pallet_id bigint;
BEGIN
    IF EXISTS (SELECT 1 FROM pallet_barcode WHERE barcode = '9999990000026') THEN
        RETURN;
    END IF;

    v_order_id := nextval('aur_order_master_seq');
    INSERT INTO aur_order_master (id, order_info, status, depo_no, order_depo_code,
                                  firm_code, firm_name, cari_code, baglanti_tipi, bolge_kodu,
                                  belge_no, op_type, aur_user_id, assigned_order,
                                  created_by, created_date, last_modified_by, last_modified_date)
    SELECT v_order_id, 'AUR-' || v_order_id, 'IN_PROGRESS', 1, 1,
           '320.01.001', 'Yildiz Makina Ltd.', '320.01.001', '1', '34',
           'A-1001', 'FMK', u.id, false,
           'admin', now(), 'admin', now()
    FROM aur_user u WHERE u.login = 'admin';

    INSERT INTO aur_order_detail (id, aur_order_id, status, stok_kodu, barcode, stok_birimi,
                                  siparis_miktar, teslim_miktar, stok_adi, sip_uid, siparis_no,
                                  observer_amount, is_piece, version)
    SELECT nextval('aur_order_detail_seq'), v_order_id, 'IN_PROGRESS', aed.sip_stok_kod, aed.barkod,
           aed.stok_birimi, aed.sip_miktar, aed.sip_teslim_miktar, aed.stok_adi,
           aed.sip_guid, aed.sip_belge_no, 0, false, 0
    FROM aur_erp_data aed
    WHERE aed.sip_belge_no = 'A-1001'
      AND coalesce(aed.sip_teslim_miktar, 0) < coalesce(aed.sip_miktar, 0)
    ORDER BY aed.sip_satir_no;

    SELECT id INTO v_detail_id
      FROM aur_order_detail WHERE aur_order_id = v_order_id ORDER BY id LIMIT 1;

    -- siparise baglandigi icin CREATED degil ASSIGNED
    v_pallet_id := nextval('pallet_barcode_seq');
    INSERT INTO pallet_barcode (id, barcode, status, created_by, created_date,
                                last_modified_by, last_modified_date)
    VALUES (v_pallet_id, '9999990000026', 'ASSIGNED', 'admin', now(), 'admin', now());

    INSERT INTO pallet_barcode_order_rel (id, pallet_barcode_id, aur_order_id, aur_order_detail_id,
                                          status, created_by, created_date,
                                          last_modified_by, last_modified_date)
    VALUES (nextval('pallet_barcode_order_rel_seq'), v_pallet_id, v_order_id, v_detail_id,
            true, 'admin', now(), 'admin', now());
END $$;

-- ------------------------------------------------- sequence'lari ileri al
-- Yukaridaki kayitlar id'lerini acikca veriyor; sekanslari bunlarin otesine tasimazsak
-- uygulamanin ilk insert'i mevcut bir id'yi tekrar uretip primary key hatasi alir.
-- 1000 tabani seed id'lerinin uzerinde bosluk birakir, max(id)+1 ise seed'den sonra
-- uygulama kayit eklemis olsa bile sekansin geri gitmesini onler.
-- aur_menu_role_rel, aur_user_role_rel ve product bilesik/dogal anahtarlidir, sekanslari yoktur.
SELECT setval('aur_company_seq',   GREATEST(1000, coalesce((SELECT max(id) FROM aur_company),   0) + 1), false);
SELECT setval('warehouse_seq',     GREATEST(1000, coalesce((SELECT max(id) FROM warehouse),     0) + 1), false);
SELECT setval('aur_role_seq',      GREATEST(1000, coalesce((SELECT max(id) FROM aur_role),      0) + 1), false);
SELECT setval('aur_user_seq',      GREATEST(1000, coalesce((SELECT max(id) FROM aur_user),      0) + 1), false);
SELECT setval('user_depo_rel_seq', GREATEST(1000, coalesce((SELECT max(id) FROM user_depo_rel), 0) + 1), false);
SELECT setval('aur_erp_data_seq',  GREATEST(1000, coalesce((SELECT max(id) FROM aur_erp_data),  0) + 1), false);
SELECT setval('aur_order_master_seq',          GREATEST(1000, coalesce((SELECT max(id) FROM aur_order_master),          0) + 1), false);
SELECT setval('aur_order_detail_seq',          GREATEST(1000, coalesce((SELECT max(id) FROM aur_order_detail),          0) + 1), false);
SELECT setval('pallet_barcode_seq',            GREATEST(1000, coalesce((SELECT max(id) FROM pallet_barcode),            0) + 1), false);
SELECT setval('pallet_barcode_order_rel_seq',  GREATEST(1000, coalesce((SELECT max(id) FROM pallet_barcode_order_rel),  0) + 1), false);

COMMIT;
