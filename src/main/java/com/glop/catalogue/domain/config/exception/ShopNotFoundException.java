package com.glop.catalogue.domain.config.exception;

public class ShopNotFoundException extends RuntimeException {
    public ShopNotFoundException(String message) {
        super(message);
    }
    public ShopNotFoundException(){super("Le magasin renseigné n'existe pas.");}
}
