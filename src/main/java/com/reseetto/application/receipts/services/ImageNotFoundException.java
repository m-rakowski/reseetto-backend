package com.reseetto.application.receipts.services;

public class ImageNotFoundException extends RuntimeException {
    ImageNotFoundException(final String message) {
        super(message);
    }
}
