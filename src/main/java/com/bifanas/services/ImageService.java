package com.bifanas.services;

import com.bifanas.model.OcrResponseRM;
import javassist.NotFoundException;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void deleteFileAndDbLog(String id) throws NotFoundException, IOException;

    OcrResponseRM saveFileAndPerformOCR(MultipartFile multipartFile) throws IOException, TesseractException;
}
