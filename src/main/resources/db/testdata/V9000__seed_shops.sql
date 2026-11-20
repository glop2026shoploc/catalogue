INSERT INTO category (name) VALUES
    ('Boulangerie'), ('Presse'), (U&'V\00eatements'), ('Alimentation')
ON CONFLICT (name) DO NOTHING;

WITH demo(name, siret, road, number) AS (
    VALUES
        ('Boulangerie du Centre', '20700000000011', 'rue de la Paix', '12'),
        ('Presse du Marché', '20700000000012', 'place du Marché', '4'),
        ('Boutique des Fleurs', '20700000000013', 'rue des Fleurs', '8'),
        ('Épicerie du Quartier', '20700000000014', 'avenue Victor Hugo', '17'),
        ('Boulangerie des Halles', '20700000000015', 'rue du Centre', '2'),
        ('Shop en attente', '20700000000016', 'rue du Test', '1')
)
INSERT INTO address (road, number, city_id)
SELECT demo.road, demo.number, '20700000-0000-4000-8000-000000000099'
FROM demo
WHERE NOT EXISTS (
    SELECT 1 FROM shop WHERE siret_number = demo.siret
);

WITH demo(name, siret, status, road, number, category) AS (
    VALUES
        ('Boulangerie du Centre', '20700000000011', 'VALIDATED', 'rue de la Paix', '12', 'Boulangerie'),
        ('Presse du Marché', '20700000000012', 'VALIDATED', 'place du Marché', '4', 'Presse'),
        ('Boutique des Fleurs', '20700000000013', 'VALIDATED', 'rue des Fleurs', '8', U&'V\00eatements'),
        ('Épicerie du Quartier', '20700000000014', 'VALIDATED', 'avenue Victor Hugo', '17', 'Alimentation'),
        ('Boulangerie des Halles', '20700000000015', 'VALIDATED', 'rue du Centre', '2', 'Boulangerie'),
        ('Shop en attente', '20700000000016', 'WAITING', 'rue du Test', '1', 'Presse')
)
INSERT INTO shop (name, siret_number, status, address_id, category_id)
SELECT demo.name, demo.siret, demo.status, address.address_id, category.category_id
FROM demo
JOIN address ON address.road = demo.road AND address.number = demo.number
JOIN category ON category.name = demo.category
WHERE NOT EXISTS (
    SELECT 1 FROM shop WHERE siret_number = demo.siret
);
