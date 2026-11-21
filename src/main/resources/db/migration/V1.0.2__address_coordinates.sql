-- Position du commerce : sert à afficher sa distance depuis la position de l'utilisateur.
-- Nullable : un commerce sans coordonnées est listé sans distance.
ALTER TABLE address
    ADD COLUMN latitude  double precision NULL
        CONSTRAINT chk_address_latitude CHECK (latitude BETWEEN -90 AND 90),
    ADD COLUMN longitude double precision NULL
        CONSTRAINT chk_address_longitude CHECK (longitude BETWEEN -180 AND 180);
