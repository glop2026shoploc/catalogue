package com.glop.catalogue.in.restapi.config.error;

import com.glop.catalogue.domain.config.exception.AskedErrorException;
import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(AskedErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConversion(RuntimeException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShopAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleShopAlreadyExists(ShopAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().message(ex.getMessage()).build(), HttpStatus.CONFLICT);
    }

    /** Données en double en base (ex : même SIRET envoyé deux fois en même temps) */    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateData(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().message("Donnée en conflit avec une donnée existante.").build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .reduce((first, second) -> first + ", " + second)
                .orElse("Requête invalide.");
        return new ResponseEntity<>(ErrorResponse.builder().message(message).build(), HttpStatus.BAD_REQUEST);
    }
}
