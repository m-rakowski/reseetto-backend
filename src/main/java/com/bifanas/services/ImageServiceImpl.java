package com.bifanas.services;

import com.bifanas.model.OcrResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private TesseractService tesseractService;

    @Override
    public OcrResponse getTextFromFile(File imagePath) throws IOException, TesseractException {
        Optional<File> fixedImage = ImageOperations.fixPerspective(imagePath);

        if (fixedImage.isPresent()) {
            log.info("image has been fixed successfully, performing OCR on the fixed version");
            return tesseractService.performOCR(ImageIO.read(fixedImage.get()));
        } else {
            log.info("image has Not been fixed, performing OCR on the original");
            return tesseractService.performOCR(ImageIO.read(imagePath));
        }

    }
}
