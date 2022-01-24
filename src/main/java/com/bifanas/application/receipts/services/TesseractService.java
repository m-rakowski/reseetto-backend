package com.bifanas.application.receipts.services;

import com.bifanas.application.receipts.model.OcrResponse;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface TesseractService {
    OcrResponse performOCR(BufferedImage bufferedImage) throws IOException, TesseractException;
}
