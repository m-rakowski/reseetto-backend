package com.bifanas.controller;

import com.bifanas.BifanasBackendApplication;
import com.bifanas.model.OcrResponse;
import com.bifanas.services.ImageService;
import com.bifanas.services.TesseractService;
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
@SpringBootTest(classes = BifanasBackendApplication.class)
@WebAppConfiguration
public class ImageControllerTest {


    @MockBean
    ImageService imageService;
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        Mockito.when(imageService.getTextFromFile(any())).thenReturn(new OcrResponse("1.23 abc", "1.23"));
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
