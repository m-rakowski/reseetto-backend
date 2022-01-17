package com.bifanas.services;

import com.bifanas.model.OcrResponse;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Slf4j
@Service
public class TesseractServiceImpl implements TesseractService {
    @Override
    public OcrResponse calculate(BufferedImage bufferedImage) throws TesseractException {

        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("por");
        tesseract.setDatapath("tessdata");

        String text = tesseract.doOCR(bufferedImage);
        String total = TesseractUtils.getTotalFromText(text);

        return new OcrResponse(text, total);
    }

}
