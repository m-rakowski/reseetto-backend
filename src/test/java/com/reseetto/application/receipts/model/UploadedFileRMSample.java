package com.reseetto.application.receipts.model;

import static com.reseetto.application.receipts.model.UploadedFileSample.anyUploadedFile;

public class UploadedFileRMSample {
    public static UploadedFileRM anyUploadedFileRM() {
        return new UploadedFileRM(anyUploadedFile());
    }

}
