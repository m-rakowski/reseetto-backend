package com.bifanas.services;

import com.bifanas.model.OcrResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


@Slf4j
@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private OpenCVService openCVService;
    private TesseractService tesseractService;

    @Override
    public OcrResponse getTextFromFile(File imagePath) throws IOException, TesseractException {
        File fixedImage = openCVService.fixImagePerspective(imagePath);
        return tesseractService.calculate(ImageIO.read(fixedImage));
    }
}
