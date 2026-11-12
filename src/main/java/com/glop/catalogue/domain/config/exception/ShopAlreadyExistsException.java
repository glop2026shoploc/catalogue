package com.glop.catalogue.domain.config.exception;

public class ShopAlreadyExistsException extends RuntimeException {
    public ShopAlreadyExistsException(String message) {
        super(message);
    }
}
