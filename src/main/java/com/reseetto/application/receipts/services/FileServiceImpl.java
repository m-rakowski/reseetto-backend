package com.reseetto.application.receipts.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    @Override
    public File saveMultipartInPublic(MultipartFile multipartFile, String savedFileName) throws IOException {

        File file = new File("public"+"/"+savedFileName);

        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }

        return file;
    }

    @Override
    public void deleteBySavedFileName(String savedFileName) throws IOException {
        String fileName = "public/" + savedFileName;
        Files.delete(Paths.get(fileName));
    }
}
