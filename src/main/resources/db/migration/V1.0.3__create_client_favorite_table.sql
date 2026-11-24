CREATE TABLE client_shop_favorite
(
    client_shop_favorite_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id        uuid NOT NULL REFERENCES shop (shop_id),
    client_id        uuid NOT NULL ,
    UNIQUE(shop_id,client_id)

);