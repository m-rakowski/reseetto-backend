package com.bifanas.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    File saveMultipartInPublic(MultipartFile multipartFile) throws IOException;

    void deleteById(String id) throws IOException;
}
