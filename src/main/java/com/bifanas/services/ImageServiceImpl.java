package com.bifanas.services;

import com.bifanas.model.OcrResponse;
import com.bifanas.model.OcrResponseRM;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private TesseractService tesseractService;
    private FileService fileService;
    private DbService dbService;

    @Override
    public void deleteFileAndDbLog(String id) throws NotFoundException, IOException {

        fileService.deleteById(id);
        dbService.deleteById(id);
    }

    @Override
    public OcrResponseRM saveFileAndPerformOCR(MultipartFile multipartFile) throws IOException, TesseractException {

        final File savedFile = fileService.saveMultipartInPublic(multipartFile);

        Optional<File> fixedImage = ImageOperations.fixPerspective(savedFile);

        OcrResponse ocrResponse =
                fixedImage.isPresent()
                        ? tesseractService.performOCR(ImageIO.read(fixedImage.get()))
                        : tesseractService.performOCR(ImageIO.read(savedFile));

        String fileId = dbService.save(
                multipartFile.getOriginalFilename(), ocrResponse.getText(), ocrResponse.getTotal());

        return OcrResponseRM.builder()
                .text(ocrResponse.getText())
                .total(ocrResponse.getTotal())
                .fileId(fileId)
                .build();
    }


}
