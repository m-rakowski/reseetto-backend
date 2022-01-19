package com.bifanas.controller;

import com.bifanas.model.OcrResponse;
import com.bifanas.model.OcrResponseRM;
import com.bifanas.model.UpdateTotal;
import com.bifanas.model.UploadedFile;
import com.bifanas.services.DbService;
import com.bifanas.services.ImageService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;


@CrossOrigin({"http://localhost:3000", "https://bifanas.herokuapp.com"})
@RestController
@Validated
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;
    private DbService dbService;

    @PostMapping("/image/ocr")
    public ResponseEntity<OcrResponseRM> ocr(@RequestParam(name = "file") MultipartFile multipartFile) throws Exception {

        UUID uuid = UUID.randomUUID();
        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String savedName = uuid + "." + ext;
        File savedFile = new File("public" + "/" + savedName);

        try (OutputStream os = new FileOutputStream(savedFile)) {
            os.write(multipartFile.getBytes());
        }

        // TODO while testing assert db and ocr are one transaction
        OcrResponse ocrResponse = imageService.getTextFromFile(savedFile);
        dbService.save(multipartFile.getOriginalFilename(), savedName, ocrResponse.getText(), ocrResponse.getTotal());
        OcrResponseRM ocrResponseRM = new OcrResponseRM(ocrResponse.getText(), ocrResponse.getTotal(), savedName);
        return new ResponseEntity<>(ocrResponseRM, HttpStatus.OK);
    }


    @GetMapping("/images")
    public ResponseEntity<List<UploadedFile>> getAll() {
        return new ResponseEntity<>(dbService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<UploadedFile> getById(@PathVariable("id") String id) throws NotFoundException {
        return new ResponseEntity<>(dbService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/image/update-total")
    public ResponseEntity<OcrResponseRM> updateTotal(@Valid @RequestBody UpdateTotal updateTotal) throws NotFoundException {
        UploadedFile uploadedFile = this.dbService.updateTotal(updateTotal);
        return new ResponseEntity<>(
                new OcrResponseRM(
                        uploadedFile.getText(), updateTotal.getTotal(), uploadedFile.getSavedName()),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") String id) throws NotFoundException {
        dbService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}


