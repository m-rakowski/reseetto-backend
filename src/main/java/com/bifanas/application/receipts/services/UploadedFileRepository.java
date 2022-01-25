package com.bifanas.application.receipts.services;


import com.bifanas.application.receipts.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, String> {
    Optional<UploadedFile> findBySavedFileName(String savedFileName);
    void deleteBySavedFileName(String savedFileName);
}
