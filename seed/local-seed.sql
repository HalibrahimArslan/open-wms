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
VALUES (1, '1', 'Merkez Depo', '1', true, true, '1', false, false, 'DEFAULT', '1')
ON CONFLICT (id) DO NOTHING;

-- Yan Depo, depolar arasi transferi ve gecici adresten yerlestirmeyi denemek icindir.
-- Mal kabul ve transfer, urunu deponun receiving_code'u ile bulunan gecici adrese
-- koyar; bu yuzden iki depoda da receiving_code depo koduyla ayni ve her depoda tek
-- bir gecici adres vardir (A99111, B99111).
INSERT INTO warehouse (id, code, name, company_code, is_real, countable,
                       transfer_code, auto_scan, unique_picking_address,
                       picking_rule_type, receiving_code)
VALUES (2, '2', 'Yan Depo', '1', true, true, '2', false, false, 'DEFAULT', '2')
ON CONFLICT (id) DO NOTHING;

-- transfer_code, UI'in adres/stok sorgularinda depo kodu yerine kullandigi degerdir
-- (Sevkiyat ekrani adresleri ve kontrol adresini bu koda gore ariyor); receiving_code
-- ise gecici adresin aranacagi depodur. Lokalde ikisi de depo koduyla ayni olmali;
-- seed'i daha once calistirmis veritabanlarinda da duzeltilir.
UPDATE warehouse SET transfer_code = code, receiving_code = code WHERE id = 1;

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

-- ---------------------------------------------------------------- sayim yetkisi
-- El terminalindeki sayim listesi, sayim tanimindaki gorunurluk yetkisini (COUNTER /
-- CHECKER) kullanicinin yetkileriyle karsilastirir. admin'e COUNTER verilir ki
-- sayim lokalde dogrudan denenebilsin.
INSERT INTO jhi_user_authority (user_id, authority_name)
SELECT u.id, 'COUNTER' FROM aur_user u WHERE u.login = 'admin'
ON CONFLICT DO NOTHING;

-- ---------------------------------------------------------------- depo yetkisi
INSERT INTO user_depo_rel (id, user_id, warehouse_id, created_by, created_date)
SELECT u.id, u.id, 1, 'system', now()
FROM aur_user u WHERE u.login IN ('admin', 'user')
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_depo_rel (id, user_id, warehouse_id, created_by, created_date)
SELECT u.id + 2, u.id, 2, 'system', now()
FROM aur_user u WHERE u.login IN ('admin', 'user')
  AND NOT EXISTS (SELECT 1 FROM user_depo_rel r WHERE r.user_id = u.id AND r.warehouse_id = 2)
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
-- erp_type = LOCAL ve erpApiActive = false:
-- ErpGatewayRouter bu sirket icin LocalOrderGateway'i secer, ERP'ye hic gitmez.
UPDATE aur_company
   SET erp_type = 'LOCAL',
       api_parameters = '{"erpApiActive": false}'::jsonb
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
-- cari_baglanti_tipi her zaman sip_tip'in metin karsiligi olmali: servis katmani
-- cariBaglantiTipi'yi sipTip'ten uretip oyle sorguluyor. sip_tip degeri ekrani da
-- belirler: Mal Kabul listeleri sipTip = 1, Sevkiyat listeleri sipTip = 0 ile gelir.
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

-- ---------------------------------------------------------- sevkiyat siparis verisi
-- Sevkiyat (Musteri Sevkiyat) ekranlari ayni tablodan ama sip_tip = 0 ile okur;
-- Cari Secimi listesi /api/firmList/<depo>/0 cagrisini yapar. Asagidaki tek siparis,
-- yukaridaki mal kabul siparisleriyle karismamasi icin ayri bir cariye (320.02.001)
-- ve ayri bir evrak serisine (S-3001) yazilmistir.
--
-- Satirlarin urunleri, asagida adreslere yerlestirilen stokla ayni urunlerdir:
-- Sevkiyat ekrani yalnizca depoda stogu olan satirlari secilebilir kabul ediyor.
INSERT INTO aur_erp_data (id, sip_guid, sip_depo_no, sip_sube_no, sip_tip, sip_cins,
                          sip_evrak_seri, sip_evrak_sira, sip_satir_no, sip_belge_no,
                          sip_musteri_kod, sip_stok_kod, stok_adi, barkod, stok_birimi,
                          sip_miktar, sip_teslim_miktar, sip_tarih, teslim_tarihi,
                          planlanan_sevk_tarihi, cari_kod, cari_unvan, bolge_kodu,
                          bolge_adi, cari_hareket_tipi, cari_baglanti_tipi)
VALUES
    -- 320.02.001 / S-3001 (3 satir, hepsi acik)
    (8,  'uid-0008', 1, 1, 0, 0, 'S', 3001, 1, 'S-3001', '320.02.001', 'STK-001', 'Vida M8x40 Galvaniz', '8690000000011', 'ADET', 60, 0, '2026-09-08', '2026-09-18', '2026-09-17', '320.02.001', 'Ege Ticaret A.S.', '35', 'Izmir', '0', '0'),
    (9,  'uid-0009', 1, 1, 0, 0, 'S', 3001, 2, 'S-3001', '320.02.001', 'STK-003', 'Pul M8 Genis',        '8690000000035', 'ADET', 30, 0, '2026-09-08', '2026-09-18', '2026-09-17', '320.02.001', 'Ege Ticaret A.S.', '35', 'Izmir', '0', '0'),
    (10, 'uid-0010', 1, 1, 0, 0, 'S', 3001, 3, 'S-3001', '320.02.001', 'STK-005', 'Ray Profil 2m',       '8690000000059', 'MT',   12, 0, '2026-09-08', '2026-09-18', '2026-09-17', '320.02.001', 'Ege Ticaret A.S.', '35', 'Izmir', '0', '0')
ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------- adres tanimlari
-- Adres bilesenleri (bolum/reyon/unite/kat/goz) ile adres tipleri, aur_depo_urun_adres
-- uzerindeki bilesik yabanci anahtarlarin karsiligidir; bunlar olmadan adres eklenemez.
-- Adres metni, AddressService.generateAddress kuralindaki gibi bilesen kodlarinin
-- sirayla birlestirilmesiyle olusur: bolum + reyon + unite + kat + goz.
-- Arayuzdeki adres okutma alani (AddressBarcode) 6 haneli adres bekledigi icin
-- unite, kat ve goz kodlari tek hanedir: A + 01 + 1 + 1 + 1 = A01111.
INSERT INTO aur_adres_tip (id, code, description, status, company_code, depo_code) VALUES
    (1, 'RAF', 'Raf Adresi',     true, '1', '1'),
    (2, 'KNT', 'Kontrol Adresi', true, '1', '1'),
    (5, 'GEC', 'Gecici Adres',   true, '1', '1'),
    (3, 'RAF', 'Raf Adresi',     true, '1', '2'),
    (4, 'GEC', 'Gecici Adres',   true, '1', '2')
ON CONFLICT (code, depo_code, company_code) DO NOTHING;

INSERT INTO aur_adres_bolum (id, code, description, status, company_code, depo_code) VALUES
    (1, 'A', 'A Bolumu', true, '1', '1'),
    (2, 'B', 'B Bolumu', true, '1', '2')
ON CONFLICT (code, depo_code, company_code) DO NOTHING;

INSERT INTO aur_adres_reyon (id, code, description, status, company_code, depo_code) VALUES
    (1, '01', '1. Reyon',       true, '1', '1'),
    (2, '02', '2. Reyon',       true, '1', '1'),
    (3, '03', 'Kontrol Reyonu', true, '1', '1'),
    (6, '99', 'Gecici Reyon',   true, '1', '1'),
    (4, '01', '1. Reyon',       true, '1', '2'),
    (5, '99', 'Gecici Reyon',   true, '1', '2')
ON CONFLICT (code, depo_code, company_code) DO NOTHING;

INSERT INTO aur_adres_unite (id, code, description, status, company_code, depo_code) VALUES
    (1, '1', '1. Unite', true, '1', '1'),
    (2, '1', '1. Unite', true, '1', '2')
ON CONFLICT (code, depo_code, company_code) DO NOTHING;

INSERT INTO aur_adres_kat (id, code, description, status, company_code, depo_code) VALUES
    (1, '1', '1. Kat', true, '1', '1'),
    (2, '1', '1. Kat', true, '1', '2')
ON CONFLICT (code, depo_code, company_code) DO NOTHING;

INSERT INTO aur_adres_oda (id, code, description, status, company_code, depo_code) VALUES
    (1, '1', '1. Goz', true, '1', '1'),
    (2, '1', '1. Goz', true, '1', '2')
ON CONFLICT (code, depo_code, company_code) DO NOTHING;

-- toplama_gozu = true olan adresler toplama onerisine girer (DefaultStrategy yalnizca
-- toplama gozlerini okuyor), kontrol_adres = true olan adres ise sevkiyat ekranindaki
-- kontrol adresi listesini besler. Adres metni ayni zamanda okutulan adres barkodudur.
INSERT INTO aur_depo_urun_adres (id, status, adres, depo_no, company_code, adres_tipi,
                                 bolum, reyon, unite, kat, oda,
                                 gecici_adres, toplama_gozu, kontrol_adres, countable,
                                 created_by, created_date, last_modified_by, last_modified_date)
VALUES
    (1, true, 'A01111', '1', '1', 'RAF', 'A', '01', '1', '1', '1', false, true,  false, true,  'system', now(), 'system', now()),
    (2, true, 'A02111', '1', '1', 'RAF', 'A', '02', '1', '1', '1', false, true,  false, true,  'system', now(), 'system', now()),
    (3, true, 'A03111', '1', '1', 'KNT', 'A', '03', '1', '1', '1', false, false, true,  false, 'system', now(), 'system', now()),
    (6, true, 'A99111', '1', '1', 'GEC', 'A', '99', '1', '1', '1', true,  false, false, false, 'system', now(), 'system', now()),
    -- Yan Depo: gecici adres ve bir toplama gozu
    (4, true, 'B99111', '2', '1', 'GEC', 'B', '99', '1', '1', '1', true,  false, false, false, 'system', now(), 'system', now()),
    (5, true, 'B01111', '2', '1', 'RAF', 'B', '01', '1', '1', '1', false, true,  false, true,  'system', now(), 'system', now())
ON CONFLICT (company_code, depo_no, adres) DO NOTHING;

-- ------------------------------------------------------------- adreslerdeki stok
-- S-3001 siparisinin urunleri toplama gozlerine, siparis miktarinin uzerinde bir
-- miktarla yerlestirilir; boylece sevkiyat toplamasi kismi de yapilabilir.
-- barkod_tipi = 'RAF' ve status = true, uygulamanin kendi yazdigi kayitlarla aynidir
-- (AurDepoUrunAdresStokService). palet_barkod_id, palete bagli olmayan stokta 1'dir.
INSERT INTO aur_depo_urun_adres_stok (id, status, urun_adres_id, stok_kod, barcode, barkod_tipi,
                                      palet_barkod_id, depo_code, company_code, miktar,
                                      created_by, created_date, last_modified_by, last_modified_date)
VALUES
    (1, true, 1, 'STK-001', '8690000000011', 'RAF', 1, '1', '1', 250, 'system', now(), 'system', now()),
    (2, true, 1, 'STK-003', '8690000000035', 'RAF', 1, '1', '1', 180, 'system', now(), 'system', now()),
    (3, true, 2, 'STK-005', '8690000000059', 'RAF', 1, '1', '1',  60, 'system', now(), 'system', now())
ON CONFLICT (id) DO NOTHING;

-- --------------------------------------- admine atanmis sevkiyat (MSK) siparisi
-- S-3001'in satirlarindan, Sevkiyat atama ekraninin (DispatchAssignDialog ->
-- /api/aur-tmp-detail) urettigi kayitlarin aynisi: MSK tipinde bir siparis basligi,
-- her satir icin bir aur_order_detail ve satirin acilisini kaydeden sifir miktarli
-- bir toplama hareketi. Siparis boylece admin'in "Sevkiyatlarim" ekraninda hazir gelir.
--
-- Atama akisiyla ayni degerler: baslik OPEN, satirlar IN_PROGRESS (createTmpDetail),
-- assigned_order = true (kullaniciya atanmis siparis), cari_code bos -- yalnizca
-- cariBaglantiTipi = '4' olan grup siparislerinde doluyor --, depo_no = depo
-- transfer_code'u, order_depo_code = ERP depo numarasi. Toplama hareketinin adresi 1:
-- atama ekrani addressId'yi sabit '1' gonderiyor.
--
-- Blok, ayni siparis zaten atanmissa hicbir sey yapmaz; seed tekrar calistirilabilir.
DO $$
DECLARE
    v_order_id bigint;
BEGIN
    IF EXISTS (SELECT 1
                 FROM aur_order_detail d
                 JOIN aur_order_master m ON m.id = d.aur_order_id
                WHERE m.op_type = 'MSK' AND d.siparis_no = 'S-3001') THEN
        RETURN;
    END IF;

    v_order_id := nextval('aur_order_master_seq');
    INSERT INTO aur_order_master (id, order_info, status, depo_no, order_depo_code,
                                  firm_code, firm_name, cari_code, baglanti_tipi, bolge_kodu,
                                  belge_no, op_type, aur_user_id, assigned_order,
                                  created_by, created_date, last_modified_by, last_modified_date)
    SELECT v_order_id, 'AUR-' || v_order_id, 'OPEN', 1, 1,
           '320.02.001', 'Ege Ticaret A.S.', '', '0', '35',
           'S-3001', 'MSK', u.id, true,
           'admin', now(), 'admin', now()
    FROM aur_user u WHERE u.login = 'admin';

    INSERT INTO aur_order_detail (id, aur_order_id, status, stok_kodu, barcode, stok_birimi,
                                  siparis_miktar, teslim_miktar, stok_adi, sip_uid, siparis_no,
                                  observer_amount, is_piece, aur_partial_item_id, version)
    SELECT nextval('aur_order_detail_seq'), v_order_id, 'IN_PROGRESS', aed.sip_stok_kod, aed.barkod,
           aed.stok_birimi, aed.sip_miktar, 0, aed.stok_adi, aed.sip_guid, aed.sip_belge_no,
           0, false, 0, 0
      FROM aur_erp_data aed
     WHERE aed.sip_belge_no = 'S-3001'
       AND aed.sip_tip = 0
     ORDER BY aed.sip_satir_no;

    INSERT INTO order_picking_transaction (id, reference_id, urun_adres_id, status,
                                           transaction_amount, transaction_type,
                                           stock_code, barcode,
                                           created_by, created_date, last_modified_by, last_modified_date)
    SELECT nextval('order_picking_transaction_seq'), d.id, 1, true,
           0, 'PICKING', d.stok_kodu, d.barcode,
           'admin', now(), 'admin', now()
      FROM aur_order_detail d
     WHERE d.aur_order_id = v_order_id;
END $$;

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
SELECT setval('aur_adres_tip_seq',   GREATEST(1000, coalesce((SELECT max(id) FROM aur_adres_tip),   0) + 1), false);
SELECT setval('aur_adres_bolum_seq', GREATEST(1000, coalesce((SELECT max(id) FROM aur_adres_bolum), 0) + 1), false);
SELECT setval('aur_adres_reyon_seq', GREATEST(1000, coalesce((SELECT max(id) FROM aur_adres_reyon), 0) + 1), false);
SELECT setval('aur_adres_unite_seq', GREATEST(1000, coalesce((SELECT max(id) FROM aur_adres_unite), 0) + 1), false);
SELECT setval('aur_adres_kat_seq',   GREATEST(1000, coalesce((SELECT max(id) FROM aur_adres_kat),   0) + 1), false);
SELECT setval('aur_adres_oda_seq',   GREATEST(1000, coalesce((SELECT max(id) FROM aur_adres_oda),   0) + 1), false);
SELECT setval('aur_depo_urun_adres_seq',       GREATEST(1000, coalesce((SELECT max(id) FROM aur_depo_urun_adres),      0) + 1), false);
SELECT setval('aur_depo_urun_adres_stok_seq',  GREATEST(1000, coalesce((SELECT max(id) FROM aur_depo_urun_adres_stok), 0) + 1), false);
SELECT setval('order_picking_transaction_seq', GREATEST(1000, coalesce((SELECT max(id) FROM order_picking_transaction), 0) + 1), false);
SELECT setval('aur_order_master_seq',          GREATEST(1000, coalesce((SELECT max(id) FROM aur_order_master),          0) + 1), false);
SELECT setval('aur_order_detail_seq',          GREATEST(1000, coalesce((SELECT max(id) FROM aur_order_detail),          0) + 1), false);
SELECT setval('pallet_barcode_seq',            GREATEST(1000, coalesce((SELECT max(id) FROM pallet_barcode),            0) + 1), false);
SELECT setval('pallet_barcode_order_rel_seq',  GREATEST(1000, coalesce((SELECT max(id) FROM pallet_barcode_order_rel),  0) + 1), false);

COMMIT;
