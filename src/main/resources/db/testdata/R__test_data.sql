-- =========================================================
--  Données de test — chargées UNIQUEMENT avec le profil "local"
-- =========================================================


INSERT INTO category (name)
VALUES ('Boulangerie'),
       ('Librairie'),
       ('Café')
ON CONFLICT (name) DO NOTHING;

INSERT INTO address (address_id, road, number, complement, city_id, latitude, longitude)
VALUES ('00000000-0000-0000-0000-000000000301', 'rue de la République', '12', NULL, '00000000-0000-0000-0000-000000000101', 50.6310, 3.0610),
       ('00000000-0000-0000-0000-000000000302', 'place du Marché', '3 bis', NULL, '00000000-0000-0000-0000-000000000101', 50.6265, 3.0480),
       ('00000000-0000-0000-0000-000000000303', 'avenue des Tilleuls', '45', NULL, '00000000-0000-0000-0000-000000000101', 50.6380, 3.0700),
       ('00000000-0000-0000-0000-000000000304', 'rue du Moulin', '8', NULL, '00000000-0000-0000-0000-000000000101', NULL, NULL), -- sans coordonnées : listé sans distance
       ('00000000-0000-0000-0000-000000000305', 'rue des Lilas', '2', NULL, '00000000-0000-0000-0000-000000000101', 50.6300, 3.0700)
ON CONFLICT DO NOTHING;

INSERT INTO shop (shop_id, name, siret_number, status, address_id)
VALUES ('00000000-0000-0000-0000-000000000401', 'Au Bon Pain', '10000000000011', 'VALIDATED', '00000000-0000-0000-0000-000000000301'),
       ('00000000-0000-0000-0000-000000000402', 'Le Café du Marché', '10000000000029', 'VALIDATED', '00000000-0000-0000-0000-000000000302'),
       ('00000000-0000-0000-0000-000000000403', 'Librairie des Tilleuls', '10000000000037', 'VALIDATED', '00000000-0000-0000-0000-000000000303'),
       ('00000000-0000-0000-0000-000000000404', 'La Fournée du Moulin', '10000000000045', 'VALIDATED', '00000000-0000-0000-0000-000000000304'),
       ('00000000-0000-0000-0000-000000000405', 'Commerce en attente', '10000000000052', 'WAITING', '00000000-0000-0000-0000-000000000305') -- ne doit jamais être listé
ON CONFLICT DO NOTHING;

INSERT INTO shop_category (shop_id, category_id)
SELECT link.shop_id, category.category_id
FROM (VALUES ('00000000-0000-0000-0000-000000000401'::uuid, 'Boulangerie'),
             ('00000000-0000-0000-0000-000000000402'::uuid, 'Café'),
             ('00000000-0000-0000-0000-000000000403'::uuid, 'Librairie'),
             ('00000000-0000-0000-0000-000000000404'::uuid, 'Boulangerie')) AS link (shop_id, category_name)
         JOIN category ON category.name = link.category_name
ON CONFLICT DO NOTHING;

-- ---------------------------------------------------------
--  Lille 
-- ---------------------------------------------------------

INSERT INTO address (address_id, road, number, complement, city_id, latitude, longitude)
VALUES ('00000000-0000-0000-0000-000000000311', 'Rue Esquermoise', '27', NULL, '00000000-0000-0000-0000-000000000102', 50.637754, 3.06193),
       ('00000000-0000-0000-0000-000000000312', 'Rue Nationale', '69 bis', NULL, '00000000-0000-0000-0000-000000000102', 50.635692, 3.059722),
       ('00000000-0000-0000-0000-000000000313', 'Place du Général de Gaulle', '15', NULL, '00000000-0000-0000-0000-000000000102', 50.636688, 3.06308)
ON CONFLICT DO NOTHING;

INSERT INTO shop (shop_id, name, siret_number, status, address_id)
VALUES ('00000000-0000-0000-0000-000000000411', 'Meert', '10000000000101', 'VALIDATED', '00000000-0000-0000-0000-000000000311'),
       ('00000000-0000-0000-0000-000000000412', 'Notting Hill Coffee', '10000000000102', 'VALIDATED', '00000000-0000-0000-0000-000000000312'),
       ('00000000-0000-0000-0000-000000000413', 'Le Furet du Nord', '10000000000103', 'VALIDATED', '00000000-0000-0000-0000-000000000313')
ON CONFLICT DO NOTHING;

INSERT INTO shop_category (shop_id, category_id)
SELECT link.shop_id, category.category_id
FROM (VALUES ('00000000-0000-0000-0000-000000000411'::uuid, 'Boulangerie'),
             ('00000000-0000-0000-0000-000000000412'::uuid, 'Café'),
             ('00000000-0000-0000-0000-000000000413'::uuid, 'Librairie')) AS link (shop_id, category_name)
         JOIN category ON category.name = link.category_name
ON CONFLICT DO NOTHING;
