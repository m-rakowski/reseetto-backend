package com.bifanas.infrastructure.rest.receipts;

import com.bifanas.application.receipts.model.OcrResponseRM;
import com.bifanas.application.receipts.model.UpdateTotal;
import com.bifanas.application.receipts.model.UploadedFileRM;
import com.bifanas.application.receipts.services.ImageFacade;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@CrossOrigin({"http://localhost:3000", "https://bifanas.herokuapp.com"})
@RestController
@Validated
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class ImageController {

    private ImageFacade imageFacade;

    @PostMapping("/image/ocr")
    public ResponseEntity<OcrResponseRM> saveFileAndPerformOCR(@RequestParam(name = "file") MultipartFile multipartFile) throws IOException, TesseractException {
        OcrResponseRM ocrResponseRM = imageFacade.saveFileAndPerformOCR(multipartFile);
        return new ResponseEntity<>(ocrResponseRM, HttpStatus.OK);
    }

    @GetMapping("/images")
    public ResponseEntity<List<UploadedFileRM>> getAll() {
        return new ResponseEntity<>(imageFacade.getAll(), HttpStatus.OK);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<UploadedFileRM> getById(@PathVariable("id") String id) throws NotFoundException {
        return new ResponseEntity<>(imageFacade.findById(id), HttpStatus.OK);
    }

    @PutMapping("/image/update-total")
    public ResponseEntity<OcrResponseRM> updateTotal(@Valid @RequestBody UpdateTotal updateTotal) throws NotFoundException {
        UploadedFileRM uploadedFile = this.imageFacade.updateTotal(updateTotal);
        return new ResponseEntity<>(OcrResponseRM
                .builder()
                .text(uploadedFile.getText())
                .total(updateTotal.getTotal())
                .savedFileName(uploadedFile.getSavedFileName())
                .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/images/{savedFileName}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("savedFileName") String savedFileName) throws NotFoundException, IOException {
        imageFacade.deleteFileAndDbLog(savedFileName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}


