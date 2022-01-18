package com.bifanas.services;

import com.bifanas.model.UploadedFile;
import com.bifanas.repository.UploadedFileRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class DbServiceImpl implements DbService {

    private UploadedFileRepository uploadedFileRepository;

    @Override
    @Transactional
    public void save(String originalName, String savedName, String text, String total) {

        this.uploadedFileRepository.save(UploadedFile.builder()
                .originalName(originalName)
                .savedName(savedName)
                .url(savedName)
                .timestamp(new Date())
                .text(text)
                .total(total)
                .build());
    }

    @Override
    public List<UploadedFile> getAll() {
        return this.uploadedFileRepository.findAll();
    }

    @Override
    public UploadedFile findById(String id) throws NotFoundException {

        Optional<UploadedFile> foundById = uploadedFileRepository.findById(id);

        if (foundById.isEmpty()) {
            throw new NotFoundException("File with id= not found");
        }

        return foundById.get();
    }

    @Override
    @Transactional
    public void deleteById(String id) throws NotFoundException {

        UploadedFile foundById = this.findById(id); // TODO this is probably unnecessary
        this.uploadedFileRepository.deleteById(id);
    }
}
