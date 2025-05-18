package com.reseetto.application.receipts.services;

import com.reseetto.application.receipts.model.UpdateTotal;
import com.reseetto.application.receipts.model.UploadedFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DbService {

    UploadedFile save(String originalName, String text, String total, String savedFileName);

    List<UploadedFile> getAll();

    Optional<UploadedFile> findById(String id);

    UploadedFile updateTotal(UpdateTotal updateTotal);

    void deleteBySavedFileName(String savedFileName);
}

