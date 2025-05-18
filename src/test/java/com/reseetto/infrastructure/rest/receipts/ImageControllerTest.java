package com.reseetto.infrastructure.rest.receipts;

import com.reseetto.application.receipts.model.OcrResponseRM;
import com.reseetto.application.receipts.services.ImageFacade;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.reseetto.ReseettoBackendApplication.class)
@WebAppConfiguration
public class ImageControllerTest {


    @MockBean
    ImageFacade imageFacade;
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        Mockito.when(imageFacade.saveFileAndPerformOCR(any())).thenReturn(
                OcrResponseRM.builder().total("1.23").text("1.23").build());
    }

    @Test
    public void when_uploading_png_response_should_be_ok() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/api/image/ocr").file(file))
                .andExpect(status().isOk());
    }

    @Test
    public void when_uploading_jpg_response_should_be_ok() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/api/image/ocr").file(file))
                .andExpect(status().isOk());
    }

    @Ignore
    @Test
    public void when_uploading_text_response_should_be_404() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/api/image/ocr").file(file))
                .andExpect(status().isBadRequest());
    }

}
