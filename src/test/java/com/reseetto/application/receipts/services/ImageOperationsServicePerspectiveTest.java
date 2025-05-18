package com.reseetto.application.receipts.services;

import nu.pattern.OpenCV;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Arrays;


@RunWith(Parameterized.class)
public class ImageOperationsServicePerspectiveTest {

    private final File input;

    private ImageOperationsService imageOperationsService;
    public ImageOperationsServicePerspectiveTest(File input) {
        this.input = input;
    }

    @BeforeClass
    public static void initOpenCv() {
        OpenCV.loadShared();
    }

    @Before
    public void init() {
        this.imageOperationsService = new ImageOperationsServiceImpl();
    }

    @Parameterized.Parameters(name = "{index} {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        new File("src/test/resources/800px-Receipt.agr.jpg")
                },
                {
                        new File("src/test/resources/IMG_6182.jpeg")
                },
                {
                        new File("src/test/resources/IMG_6185.jpeg")
                }
        });
    }

    @Test
    public void orderPointsTopLeftTopRightBottomRightBottomLeft() {
        imageOperationsService.fixPerspective(input);
    }
}
