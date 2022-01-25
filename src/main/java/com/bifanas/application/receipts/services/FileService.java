package com.bifanas.application.receipts.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public interface FileService {
    File saveMultipartInPublic(MultipartFile multipartFile, String savedFileName) throws IOException;

    void deleteBySavedFileName(String savedFileName) throws IOException;
}
