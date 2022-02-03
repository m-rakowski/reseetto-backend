package com.bifanas.application.receipts.services;

import com.bifanas.application.receipts.model.*;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ImageFacade {

    @Autowired
    private TesseractService tesseractService;

    @Autowired
    private FileService fileService;

    @Autowired
    private DbService dbService;

    @Autowired
    private QRService qrService;

    @Autowired
    private ImageOperationsService imageOperationsService;

    public UploadedFileRM findById(String id) {
        return this.dbService.findById(id)
                .map(UploadedFileRM::new)
                .orElseThrow(() -> new ImageNotFoundException("Image could not be found"));
    }

    public List<UploadedFileRM> getAll() {
        return this.dbService.getAll().stream()
                .map(UploadedFileRM::new)
                .collect(Collectors.toList());
    }

    public UploadedFileRM updateTotal(UpdateTotal updateTotal) {
        return new UploadedFileRM(dbService.updateTotal(updateTotal));
    }

    public void deleteFileAndDbLog(String savedFileName) throws IOException {
        fileService.deleteBySavedFileName(savedFileName);
        dbService.deleteBySavedFileName(savedFileName);
    }

    public OcrResponseRM saveFileAndPerformOCR(MultipartFile multipartFile) throws IOException, TesseractException {

        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String savedFileName = UUID.randomUUID() + "." + ext;

        final File savedFile = fileService.saveMultipartInPublic(multipartFile, savedFileName);

        OcrResponse ocrResponse = new OcrResponse("ocr turned off", "");
//        Optional<File> fixedImage = imageOperationsService.fixPerspective(savedFile);
//
//        OcrResponse ocrResponse =
//                fixedImage.isPresent()
//                        ? tesseractService.performOCR(ImageIO.read(fixedImage.get()))
//                        : tesseractService.performOCR(ImageIO.read(savedFile));
//
        String readAmount = qrService.getAmountFromFile(savedFile.getAbsolutePath());

        if (!readAmount.equals("")) {
            ocrResponse.setTotal(readAmount);
        }

        UploadedFile savedInDb = dbService.save(
                multipartFile.getOriginalFilename(),
                ocrResponse.getText(),
                ocrResponse.getTotal(),
                savedFileName);

        return new OcrResponseRM(
                savedInDb.getText(), savedInDb.getTotal(), savedInDb.getSavedFileName());
    }


}
