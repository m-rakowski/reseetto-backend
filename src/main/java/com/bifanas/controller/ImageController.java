package com.bifanas.controller;

import com.bifanas.model.OcrResponseRM;
import com.bifanas.model.UpdateTotal;
import com.bifanas.model.UploadedFile;
import com.bifanas.services.DbService;
import com.bifanas.services.ImageService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

//TODO move any logic to ImageController
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
        OcrResponseRM ocrResponseRM = imageService.saveFileAndPerformOCR(multipartFile);
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
        return new ResponseEntity<>(OcrResponseRM
                .builder()
                .text(uploadedFile.getText())
                .total(updateTotal.getTotal())
                .fileId(uploadedFile.getId()).build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") String id) throws NotFoundException, IOException {

        imageService.deleteFileAndDbLog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}


