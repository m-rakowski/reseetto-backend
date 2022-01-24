package com.bifanas.application.receipts.services;

import com.bifanas.application.receipts.model.UpdateTotal;
import com.bifanas.application.receipts.model.UploadedFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class DbServiceImpl implements DbService {

    private UploadedFileRepository uploadedFileRepository;

    @Override
    public UploadedFile save(String originalName, String text, String total, String savedFileName) {
        var uploadedFile = UploadedFile.builder()
                .originalName(originalName)
                .timestamp(new Date())
                .text(text)
                .total(total)
                .savedFileName(savedFileName)
                .build();
        return this.uploadedFileRepository.save(uploadedFile);
    }

    @Override
    public void deleteById(String id) {
        this.uploadedFileRepository.deleteById(id);
    }

    @Override
    public List<UploadedFile> getAll() {
        return this.uploadedFileRepository.findAll();
    }

    @Override
    public Optional<UploadedFile> findById(String id) {
        return uploadedFileRepository.findById(id);
    }

    @Override
    public UploadedFile updateTotal(UpdateTotal updateTotal) {
        Optional<UploadedFile> bySavedFileName = uploadedFileRepository.findBySavedFileName(updateTotal.getSavedFileName());

        UploadedFile uploadedFile = bySavedFileName.orElseThrow(() -> new ImageNotFoundException(""));
        uploadedFile.setTotal(updateTotal.getTotal());
        return uploadedFileRepository.save(uploadedFile);
    }

}

