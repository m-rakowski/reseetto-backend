package com.bifanas.application.receipts.services;

import nu.pattern.OpenCV;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.opencv.core.Point;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class ImageOperationsServiceTest {

    private final List<Point> expected;
    private final List<Point> input;
    private ImageOperationsService imageOperationsService;

    public ImageOperationsServiceTest(List<Point> expected, List<Point> input) {
        this.expected = expected;
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

    @Parameterized.Parameters(name = "{index}: TesseractUtils.getTotalFromText({1})={0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        List.of(new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1)),
                        List.of(new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(0, 0))
                },
                {
                        List.of(new Point(0, 1), new Point(1, 1), new Point(0, 0), new Point(1, 0)),
                        List.of(new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(0, 0))
                },
        });
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNot4Points() {
        imageOperationsService.orderPointsTopLeftTopRightBottomRightBottomLeft(List.of());
    }

    @Ignore
    @Test
    public void orderPointsTopLeftTopRightBottomRightBottomLeft() {
        assertEquals(expected, imageOperationsService.orderPointsTopLeftTopRightBottomRightBottomLeft(input));
    }
}
