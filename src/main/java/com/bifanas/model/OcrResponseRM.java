package com.bifanas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OcrResponseRM {
    String text;
    String total;
    String savedName;
}
