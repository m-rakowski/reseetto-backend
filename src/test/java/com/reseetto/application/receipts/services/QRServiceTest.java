package com.reseetto.application.receipts.services;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QRServiceTest {

    String text = "A:500829993" +
            "*B:999999990" +
            "*C:PT" +
            "*D:FS" +
            "*E:N" +
            "*F:20220201" +
            "*G:FS 08360022201010625/006689" +
            "*H:0-006689" +
            "*I1:PT" +
            "*I3:10.83" +
            "*I4:0.65" +
            "*N:0.65" +
            "*O:11.48" +
            "*Q:Y6+9" +
            "*R:369";


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getAmount() {
        QRService qrService = new QRServiceImpl();

        assertEquals("11.48", qrService.getAmount(text));

    }

}
