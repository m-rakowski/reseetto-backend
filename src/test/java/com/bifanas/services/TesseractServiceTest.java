package com.bifanas.services;

import com.bifanas.model.OcrResponse;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;

import static org.junit.Assert.assertEquals;

public class TesseractServiceTest {
    @Test
    public void calculate() throws Exception {
        TesseractService tesseractService = new TesseractServiceImpl();

        var image = ImageIO.read(new File("src/test/resources/264770293_1317122782058374_8632100671026656291_n.jpg"));
        OcrResponse result = tesseractService.performOCR(image);

        assertEquals("4.50 EUR", result.getTotal());
    }
}
