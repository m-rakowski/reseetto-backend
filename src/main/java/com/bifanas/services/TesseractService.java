package com.bifanas.services;

import com.bifanas.model.OcrResponse;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface TesseractService {
    OcrResponse calculate(BufferedImage bufferedImage) throws IOException, TesseractException;
}
