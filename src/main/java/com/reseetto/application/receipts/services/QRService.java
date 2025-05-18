package com.reseetto.application.receipts.services;

import java.io.IOException;

public interface QRService {

    String getAmountFromFile(String path) throws IOException;

    String getAmount(String qrText);
}
