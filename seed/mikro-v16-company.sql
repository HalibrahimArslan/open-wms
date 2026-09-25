-- =====================================================================
-- Sirket 2: Mikro V16 (HISAR2026) entegrasyonu
--
-- local-seed.sql'den SONRA calistirilir:
--     docker exec -i wms-postgres psql -U wms -d wms < seed/mikro-v16-company.sql
--     docker restart wms-app      # sirket/kullanici bilgisi onbellekte
--
-- Idempotenttir. Olusturulanlar:
--   aur_company 2   : erp_type = MIKRO_V16, erpApiActive = true
--                     api_endpoint = http://host.docker.internal:8081/api
--                     (mikro-v16 docker'da :8081'de; wms-app konteynerinden host'a
--                      host.docker.internal ile gidilir, bkz. docker-compose extra_hosts)
--                     mikro-v16 girisi: admin / admin
--   warehouse       : '1' / Merkez Depo (Mikro'daki depo 1), sirket 2
--   aur_role        : ROLE_ADMIN (sirket 2), tum menuler bagli
--   aur_user        : mikro / admin  (sirket 2, ROLE_ADMIN, depo 1'e yetkili)
--
-- api_parameters.password ApiPasswordCipher ile sifreli olmali (AES/GCM, anahtar
-- jhipster.security.authentication.jwt.base64-secret'in SHA-256'si). Asagidaki deger
-- application-{dev,prod}.yml'deki varsayilan secret ile "admin"in sifrelenmis halidir;
-- secret'i env ile degistirirseniz sifreyi Sirket Yonetimi ekranindan yeniden girin.
-- =====================================================================

BEGIN;

-- ---------------------------------------------------------------- sirket
INSERT INTO aur_company (id, company_code, company_name, erp_type, api_endpoint, api_parameters)
VALUES (2, 2, 'HISAR Mikro V16', 'MIKRO_V16', 'http://host.docker.internal:8081/api',
        '{"erpApiActive": true, "username": "admin", "password": "HD6BBmUijGLdJSq4IRP1uJFtqtb7l7Kfp0n7QOMFvGU5"}'::jsonb)
ON CONFLICT (id) DO UPDATE
   SET company_code   = EXCLUDED.company_code,
       company_name   = EXCLUDED.company_name,
       erp_type       = EXCLUDED.erp_type,
       api_endpoint   = EXCLUDED.api_endpoint,
       api_parameters = EXCLUDED.api_parameters;

-- eski Mikro token'i kalmasin; ilk istekte yeniden /authenticate yapilir
DELETE FROM erp_jwt_data WHERE erp_type = 'MIKRO_V16';

-- ---------------------------------------------------------------- depo
INSERT INTO warehouse (id, code, name, company_code, is_real, countable,
                       transfer_code, auto_scan, unique_picking_address,
                       picking_rule_type, receiving_code)
SELECT nextval('warehouse_seq'), '1', 'Merkez Depo (Mikro)', '2', true, true, '1', false, false, 'DEFAULT', '1'
WHERE NOT EXISTS (SELECT 1 FROM warehouse WHERE code = '1' AND company_code = '2');

-- ---------------------------------------------------------------- rol + menuler
INSERT INTO aur_role (id, role_name, company_code)
SELECT nextval('aur_role_seq'), 'ROLE_ADMIN', 2
WHERE NOT EXISTS (SELECT 1 FROM aur_role WHERE role_name = 'ROLE_ADMIN' AND company_code = 2);

INSERT INTO aur_menu_role_rel (menu_id, role_id)
SELECT m.id, r.id FROM aur_menu m
CROSS JOIN aur_role r WHERE r.role_name = 'ROLE_ADMIN' AND r.company_code = 2
ON CONFLICT DO NOTHING;

-- ---------------------------------------------------------------- kullanici
-- parola: admin (hash config/liquibase/data/user.csv ile ayni).
-- password_version = 1: aksi halde UserService parolayi generic parolayla degistirir.
INSERT INTO aur_user (id, login, password_hash, first_name, last_name, email, activated,
                      lang_key, password_version, company_code, created_by, created_date)
SELECT nextval('aur_user_seq'), 'mikro', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC',
       'Mikro', 'Admin', 'mikro@localhost', true, 'tr', 1, 2, 'system', now()
WHERE NOT EXISTS (SELECT 1 FROM aur_user WHERE login = 'mikro');

INSERT INTO jhi_user_authority (user_id, authority_name)
SELECT u.id, a.name FROM aur_user u
JOIN jhi_authority a ON a.name IN ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MINIO', 'COUNTER')
WHERE u.login = 'mikro'
ON CONFLICT DO NOTHING;

INSERT INTO aur_user_role_rel (user_id, role_id)
SELECT u.id, r.id FROM aur_user u, aur_role r
WHERE u.login = 'mikro' AND r.role_name = 'ROLE_ADMIN' AND r.company_code = 2
ON CONFLICT DO NOTHING;

INSERT INTO user_depo_rel (id, user_id, warehouse_id, created_by, created_date)
SELECT nextval('user_depo_rel_seq'), u.id, w.id, 'system', now()
FROM aur_user u, warehouse w
WHERE u.login = 'mikro' AND w.code = '1' AND w.company_code = '2'
  AND NOT EXISTS (SELECT 1 FROM user_depo_rel r WHERE r.user_id = u.id AND r.warehouse_id = w.id);

-- sirket id'si elle verildi; sekans geride kalmasin
SELECT setval('aur_company_seq', GREATEST(1000, coalesce((SELECT max(id) FROM aur_company), 0) + 1), false);

COMMIT;
