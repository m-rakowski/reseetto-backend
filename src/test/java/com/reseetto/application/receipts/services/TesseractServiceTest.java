package com.reseetto.application.receipts.services;

import com.reseetto.application.receipts.model.OcrResponse;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TesseractServiceTest {
    @Test
    @Ignore
    public void calculate() throws Exception {
        TesseractService tesseractService = new TesseractServiceImpl();

        var image = ImageIO.read(new File("src/test/resources/264770293_1317122782058374_8632100671026656291_n.jpg"));
        OcrResponse result = tesseractService.performOCR(image);

        assertEquals("4.50", result.getTotal());
    }
}
