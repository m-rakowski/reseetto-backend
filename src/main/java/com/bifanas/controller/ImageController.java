package com.bifanas.controller;

import com.bifanas.model.OcrResponse;
import com.bifanas.services.ImageService;
import com.bifanas.services.OpenCVService;
import com.bifanas.services.TesseractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


@CrossOrigin({"http://localhost:3000", "https://bifanas.herokuapp.com"})
@RestController
@Validated
@RequestMapping("/api")
@Slf4j
public class ImageController {

    // TODO fix field injection not recommended
    @Autowired
    ImageService imageService;

    @PostMapping("/image/ocr")
    public ResponseEntity<OcrResponse> ocr(@RequestParam(name = "file") MultipartFile multipartFile) throws Exception {

        File savedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (OutputStream os = new FileOutputStream(savedFile)) {
            os.write(multipartFile.getBytes());
        }

        OcrResponse ocrResponse = imageService.getTextFromFile(savedFile);

        return new ResponseEntity<>(ocrResponse, HttpStatus.OK);
    }
}


