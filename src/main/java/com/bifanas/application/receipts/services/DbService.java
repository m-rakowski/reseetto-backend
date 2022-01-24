package com.bifanas.application.receipts.services;

import com.bifanas.application.receipts.model.UpdateTotal;
import com.bifanas.application.receipts.model.UploadedFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DbService {

    UploadedFile save(String originalName, String text, String total, String savedFileName);

    void deleteById(String id);

    List<UploadedFile> getAll();

    Optional<UploadedFile> findById(String id);

    UploadedFile updateTotal(UpdateTotal updateTotal);

}

