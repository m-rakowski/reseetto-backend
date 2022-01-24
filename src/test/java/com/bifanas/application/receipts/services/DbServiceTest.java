package com.bifanas.application.receipts.services;

import com.bifanas.application.receipts.model.UploadedFile;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.bifanas.application.receipts.model.UploadedFileSample.anyUploadedFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DbServiceTest {

    private DbService dbService;
    private UploadedFileRepository uploadedFileRepository;


    @Before
    public void init() {
        uploadedFileRepository = mock(UploadedFileRepository.class);
        dbService = new DbServiceImpl(uploadedFileRepository);
    }

    @Test
    public void save() {
        UploadedFile uploadedFile = anyUploadedFile();
        String savedFileName = UUID.randomUUID() + ".jpg";

        when(uploadedFileRepository.save(any())).thenReturn(anyUploadedFile());


        UploadedFile saved = dbService.save(
                uploadedFile.getOriginalName(), uploadedFile.getText(), uploadedFile.getTotal(),
                savedFileName);

    }

    @Test
    public void deleteById() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void updateTotal() {
    }
}
