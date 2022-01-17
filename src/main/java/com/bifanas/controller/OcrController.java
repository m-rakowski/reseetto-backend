package com.bifanas.controller;

import com.bifanas.model.OcrResponse;
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
public class OcrController {

    @Autowired
    TesseractService tesseractService;

    @Autowired
    OpenCVService openCVService;

    @PostMapping("/image/ocr")
    public ResponseEntity<OcrResponse> translate(@RequestParam(name = "file") MultipartFile file) throws Exception {
        BufferedImage img = ImageIO.read(file.getInputStream());
        OcrResponse ocrResponse = tesseractService.calculate(img);

        return new ResponseEntity<>(ocrResponse, HttpStatus.OK);
    }


    @PostMapping(value = "/image/fix-perspective",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] faceRecognition(@RequestParam(name = "file") MultipartFile multipartFile) throws IOException {
        File savedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        try (OutputStream os = new FileOutputStream(savedFile)) {
            os.write(multipartFile.getBytes());
        }

        File output = openCVService.fixImage(savedFile);
        return FileUtils.readFileToByteArray(output);
    }
}


