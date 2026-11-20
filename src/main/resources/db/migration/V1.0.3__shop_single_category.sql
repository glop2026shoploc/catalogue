ALTER TABLE shop ADD COLUMN category_id UUID;

INSERT INTO category (name)
SELECT 'Autres'
WHERE EXISTS (SELECT 1 FROM shop)
  AND NOT EXISTS (SELECT 1 FROM category);

UPDATE shop AS s
SET category_id = COALESCE(
    (SELECT sc.category_id
     FROM shop_category sc
     JOIN category c ON c.category_id = sc.category_id
     WHERE sc.shop_id = s.shop_id
     ORDER BY c.name
     LIMIT 1),
    (SELECT category_id FROM category ORDER BY name LIMIT 1)
);

ALTER TABLE shop ALTER COLUMN category_id SET NOT NULL;
ALTER TABLE shop ADD CONSTRAINT fk_shop_category
    FOREIGN KEY (category_id) REFERENCES category(category_id);
CREATE INDEX idx_shop_category_id ON shop(category_id);
CREATE INDEX idx_shop_status ON shop(status);
DROP TABLE shop_category;
