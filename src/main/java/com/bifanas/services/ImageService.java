package com.bifanas.services;

import com.bifanas.model.OcrResponse;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

public interface ImageService {
    OcrResponse getTextFromFile(File imagePath) throws IOException, TesseractException;
}
