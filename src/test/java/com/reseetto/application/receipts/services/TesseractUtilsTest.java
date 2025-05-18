package com.reseetto.application.receipts.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TesseractUtilsTest {

    private final String expected;
    private final String input;

    public TesseractUtilsTest(String expected, String input) {
        this.expected = expected;
        this.input = input;
    }

    @Parameterized.Parameters(name = "{index}: TesseractUtils.getTotalFromText({1})={0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"1.00", "1,00"},
                {"1.00", "1.00EUR"},
                {"1.00", "1.00"},
                {"", "1.000EUR"},
                {"1.00", "abc 01,00"},
                {"1.00", "abc 01,00"},
                {"1.00", "abc 01,00 2.250"},
        });
    }

    @Test
    public void getTotalFromText() {
        assertEquals(expected, TesseractUtils.getTotalFromText(input));
    }
}
