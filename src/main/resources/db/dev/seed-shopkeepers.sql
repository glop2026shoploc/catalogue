-- Donnees de demonstration pour tester le filtre US-207.
-- A executer manuellement sur une base Catalogue de developpement.

INSERT INTO shopkeepers (id, name, category, address)
VALUES
    ('20700000-0000-4000-8000-000000000001', 'Boulangerie du Centre', 'Boulangerie', '12 rue de la Paix'),
    ('20700000-0000-4000-8000-000000000002', U&'Presse du March\00e9', 'Presse', U&'4 place du March\00e9'),
    ('20700000-0000-4000-8000-000000000003', 'Boutique des Fleurs', U&'V\00eatements', '8 rue des Fleurs'),
    ('20700000-0000-4000-8000-000000000004', U&'\00c9picerie du Quartier', 'Alimentation', '17 avenue Victor Hugo')
ON CONFLICT (id) DO UPDATE SET
    name = EXCLUDED.name,
    category = EXCLUDED.category,
    address = EXCLUDED.address;
