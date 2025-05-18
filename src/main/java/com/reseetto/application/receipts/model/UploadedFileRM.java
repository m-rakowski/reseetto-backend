package com.reseetto.application.receipts.model;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;

import java.util.Date;

// TODO constructor that takes in UploadedFile

@Data
public class UploadedFileRM {
    private long id;
    private String originalName;
    private String savedFileName;
    private String text;
    private String total;
    private Date timestamp;

    public UploadedFileRM(UploadedFile uploadedFile) {
        this.id = uploadedFile.getId();
        this.originalName = uploadedFile.getOriginalName();
        this.text = uploadedFile.getText();
        this.total = uploadedFile.getTotal();
        this.timestamp = uploadedFile.getTimestamp();
        this.savedFileName = uploadedFile.getSavedFileName();
    }
}
