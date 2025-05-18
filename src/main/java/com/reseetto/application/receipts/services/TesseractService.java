package com.reseetto.application.receipts.services;

import com.reseetto.application.receipts.model.OcrResponse;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface TesseractService {
    OcrResponse performOCR(BufferedImage bufferedImage) throws IOException, TesseractException;
}
