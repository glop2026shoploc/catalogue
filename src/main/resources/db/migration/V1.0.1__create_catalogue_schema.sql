-- =========================================================
--  Magasins 
-- =========================================================
CREATE TABLE category
(
    category_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name        varchar(100) NOT NULL UNIQUE
);

CREATE TABLE address
(
    address_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    road       varchar(255) NOT NULL,
    number     varchar(20),  -- texte : "12 bis", "3B"...
    complement varchar(255),
    city_id    uuid         NOT NULL -- ville du service users (autre base : pas de FK)
);
CREATE INDEX idx_address_city ON address (city_id);

CREATE TABLE shop
(
    shop_id      uuid PRIMARY KEY      DEFAULT gen_random_uuid(),
    name         varchar(150) NOT NULL,
    siret_number char(14)     NOT NULL UNIQUE
        CONSTRAINT chk_shop_siret CHECK (siret_number ~ '^[0-9]{14}$'),
    status       varchar(20)  NOT NULL DEFAULT 'WAITING'
        CONSTRAINT chk_shop_status CHECK (status IN ('WAITING', 'VALIDATED', 'REJECTED')),
    address_id   uuid         NOT NULL UNIQUE -- 1–1
        REFERENCES address (address_id)
);

-- n–n shop / category
CREATE TABLE shop_category
(
    shop_id     uuid NOT NULL REFERENCES shop (shop_id) ON DELETE CASCADE,
    category_id uuid NOT NULL REFERENCES category (category_id) ON DELETE CASCADE,
    PRIMARY KEY (shop_id, category_id)
);
CREATE INDEX idx_shop_category_category ON shop_category (category_id);

CREATE TABLE opening_hours
(
    opening_hours_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id          uuid     NOT NULL REFERENCES shop (shop_id) ON DELETE CASCADE,
    day_of_week      smallint NOT NULL CHECK (day_of_week BETWEEN 1 AND 7), -- 1 = lundi (ISO)
    open_time        time     NOT NULL,
    close_time       time     NOT NULL,
    CHECK (close_time > open_time),
    UNIQUE (shop_id, day_of_week, open_time)
);
CREATE INDEX idx_opening_hours_shop ON opening_hours (shop_id);

-- =========================================================
--  Produits
-- =========================================================
CREATE TABLE product_type
(
    name varchar(50) PRIMARY KEY
);

CREATE TABLE product
(
    product_id    uuid PRIMARY KEY        DEFAULT gen_random_uuid(),
    name          varchar(150)   NOT NULL,
    quantity      integer        NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    price         numeric(10, 2) NOT NULL CHECK (price >= 0),
    points_price  integer CHECK (points_price >= 0), -- NULL : non achetable en points
    earned_points integer        NOT NULL DEFAULT 0 CHECK (earned_points >= 0),
    product_type  varchar(50)    NOT NULL REFERENCES product_type (name) ON UPDATE CASCADE,
    shop_id       uuid           NOT NULL REFERENCES shop (shop_id) ON DELETE CASCADE
);
CREATE INDEX idx_product_shop ON product (shop_id);
CREATE INDEX idx_product_type ON product (product_type);

CREATE TABLE bundle
(
    bundle_id uuid PRIMARY KEY        DEFAULT gen_random_uuid(),
    name      varchar(150)   NOT NULL,
    price     numeric(10, 2) NOT NULL CHECK (price >= 0)
);

-- n–n product / bundle, avec la quantité du produit dans le lot
CREATE TABLE product_bundle
(
    product_id uuid    NOT NULL REFERENCES product (product_id) ON DELETE CASCADE,
    bundle_id  uuid    NOT NULL REFERENCES bundle (bundle_id) ON DELETE CASCADE,
    quantity   integer NOT NULL DEFAULT 1 CHECK (quantity > 0),
    PRIMARY KEY (product_id, bundle_id)
);
CREATE INDEX idx_product_bundle_bundle ON product_bundle (bundle_id);
