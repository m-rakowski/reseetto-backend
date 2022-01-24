import com.bifanas.BifanasBackendApplication;
import com.bifanas.application.receipts.model.OcrResponseRM;
import com.bifanas.application.receipts.model.UpdateTotal;
import com.bifanas.application.receipts.services.ImageFacade;
import com.bifanas.application.receipts.services.UploadedFileRepository;
import nu.pattern.OpenCV;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BifanasBackendApplication.class)
@Transactional
public class BifanasBackendApplicationTests {

    @Autowired
    ImageFacade imageFacade;

    @Autowired
    UploadedFileRepository uploadedFileRepository;

    @BeforeClass
    public static void beforeClass() throws IOException {
        OpenCV.loadShared();
    }

    @Test
    public void firstStep() throws Exception {

        File file = new File("src/test/resources/800px-Receipt.agr.jpg");

        MockMultipartFile multipartFile = new MockMultipartFile(
                file.getName(),
                file.getName(),
                MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(file.toPath())
        );

        OcrResponseRM ocrResponseRM = imageFacade.saveFileAndPerformOCR(multipartFile);
        assertNotNull(ocrResponseRM.getSavedFileName());
        assertNotNull(ocrResponseRM.getText());
        assertNotNull(ocrResponseRM.getTotal());

        assertTrue(Files.exists(Path.of("public" + "/" + ocrResponseRM.getSavedFileName())));

        imageFacade.updateTotal(new UpdateTotal("999.99", ocrResponseRM.getSavedFileName()));

    }

}
