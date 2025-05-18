package com.reseetto.application.receipts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OcrResponseRM {
    String text;
    String total;
    String savedFileName;
}
