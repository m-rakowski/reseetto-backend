package com.bifanas.services;

import com.bifanas.model.UpdateTotal;
import com.bifanas.model.UploadedFile;
import javassist.NotFoundException;

import java.util.List;

public interface DbService {
    void save(String originalName, String savedName, String text, String total);

    List<UploadedFile> getAll();

    UploadedFile findById(String id) throws NotFoundException;

    void deleteById(String id) throws NotFoundException;

    UploadedFile updateTotal(UpdateTotal updateTotal) throws NotFoundException;

}

