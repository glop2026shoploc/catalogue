CREATE TABLE shopkeepers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    category VARCHAR(80) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE INDEX idx_shopkeepers_category ON shopkeepers (LOWER(category));
