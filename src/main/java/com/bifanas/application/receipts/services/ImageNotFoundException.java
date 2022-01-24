package com.bifanas.application.receipts.services;

public class ImageNotFoundException extends RuntimeException {
    ImageNotFoundException(final String message) {
        super(message);
    }
}
