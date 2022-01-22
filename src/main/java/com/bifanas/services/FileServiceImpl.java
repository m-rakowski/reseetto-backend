package com.bifanas.services;

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
    public File saveMultipartInPublic(MultipartFile multipartFile) throws IOException {
        UUID uuid = UUID.randomUUID();
        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String savedName = uuid + "." + ext;
        File savedFile = new File("public" + "/" + savedName);

        OutputStream os = new FileOutputStream(savedFile);
        os.write(multipartFile.getBytes());

        return savedFile;
    }

    @Override
    public void deleteById(String id) throws IOException {
        String fileName = "public/" + id;
        Files.delete(Paths.get(fileName));
    }
}
