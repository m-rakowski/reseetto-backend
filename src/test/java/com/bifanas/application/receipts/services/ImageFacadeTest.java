package com.bifanas.application.receipts.services;

import com.bifanas.application.receipts.model.UpdateTotal;
import com.bifanas.application.receipts.model.UploadedFile;
import com.bifanas.application.receipts.model.UploadedFileRM;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bifanas.application.receipts.model.UpdateTotalSample.anyUpdateTotal;
import static com.bifanas.application.receipts.model.UploadedFileSample.anyUploadedFile;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageFacadeTest {

    private ImageFacade tut;
    private TesseractService tesseractService;
    private FileService fileService;
    private DbService dbService;

    @Before
    public void init() {
        tesseractService = mock(TesseractService.class);
        fileService = mock(FileService.class);
        dbService = mock(DbService.class);
        tut = new ImageFacade(tesseractService, fileService, dbService);
    }

    @Test
    public void findById() {
        // given
        UploadedFile expected = anyUploadedFile();
        UploadedFileRM expectedRM = new UploadedFileRM(expected);

        // when
        when(dbService.findById(anyString())).thenReturn(Optional.of(expected));
        UploadedFileRM byId = tut.findById("123");

        // then
        assertEquals(expectedRM, byId);
    }

    @Test
    public void getAll() {
        // given
        List<UploadedFile> expected = List.of(anyUploadedFile());
        List<UploadedFileRM> expectedRM = expected.stream().map(UploadedFileRM::new).collect(Collectors.toList());

        // when
        when(dbService.getAll()).thenReturn(expected);
        List<UploadedFileRM> all = tut.getAll();

        // then
        assertEquals(expectedRM, all);
    }


    @Test
    public void saveFileAndPerformOCR() {

        // TODO expect
    }

    @Test
    public void updateTotal() {

        // given
        UpdateTotal input = anyUpdateTotal();
        UploadedFile expected = anyUploadedFile();

        // when
        when(dbService.updateTotal(anyUpdateTotal())).thenReturn(expected);
        tut.updateTotal();

        // TODO expect total to be updated

    }

    @Test
    public void deleteFileAndDbLog() {

        // TODO expect file to be deleted
        // TODO expect db row to be deleted

    }
}
