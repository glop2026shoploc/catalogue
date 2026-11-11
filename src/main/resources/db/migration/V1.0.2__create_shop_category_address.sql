CREATE TABLE category (
    category_id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE address (
    address_id UUID PRIMARY KEY,
    road VARCHAR(255) NOT NULL,
    number VARCHAR(20),
    complement VARCHAR(255),
    city_id UUID NOT NULL
);

CREATE TABLE shop (
    shop_id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    siret_number VARCHAR(14) NOT NULL UNIQUE,
    status VARCHAR(10) NOT NULL DEFAULT 'WAITING',
    address_id UUID NOT NULL UNIQUE REFERENCES address(address_id),
    category_id UUID NOT NULL REFERENCES category(category_id)
);
