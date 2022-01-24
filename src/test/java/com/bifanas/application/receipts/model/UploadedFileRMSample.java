package com.bifanas.application.receipts.model;

import static com.bifanas.application.receipts.model.UploadedFileSample.anyUploadedFile;

public class UploadedFileRMSample {
    public static UploadedFileRM anyUploadedFileRM() {
        return new UploadedFileRM(anyUploadedFile());
    }

}
