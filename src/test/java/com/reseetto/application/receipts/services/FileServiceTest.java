package com.reseetto.application.receipts.services;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class FileServiceTest {

    @Test
    public void saveMultipartInPublic() throws IOException {
        FileService fileService = new FileServiceImpl();
        File file = new File("src/test/resources/800px-Receipt.agr.jpg");

        MockMultipartFile multipartFile = new MockMultipartFile(
                file.getName(),
                file.getName(),
                MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(file.toPath())
        );
        String savedFileName = UUID.randomUUID() + ".jpg";

        fileService.saveMultipartInPublic(multipartFile, savedFileName);

        File expected = new File("public" + "/" + savedFileName);
        assertTrue(expected.exists());
    }

    @Test
    public void deleteById() {
    }
}
