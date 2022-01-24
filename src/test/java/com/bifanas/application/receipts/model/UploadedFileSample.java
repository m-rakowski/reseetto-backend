package com.bifanas.application.receipts.model;

import org.jeasy.random.EasyRandom;

public class UploadedFileSample {
    public static UploadedFile anyUploadedFile() {
        EasyRandom generator = new EasyRandom();
        return generator.nextObject(UploadedFile.class);
    }


}
