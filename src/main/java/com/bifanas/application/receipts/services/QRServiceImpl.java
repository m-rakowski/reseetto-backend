package com.bifanas.application.receipts.services;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Slf4j
@Service
@AllArgsConstructor
public class QRServiceImpl implements QRService {


    @Override
    public String getAmountFromFile(String path) throws IOException {
        BinaryBitmap binaryBitmap
                = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(
                                new FileInputStream(path)))));

        Result result;
        try {
            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (NotFoundException e) {
            return "";
        }

        String text = result.getText();

        return getAmount(text);
    }

    @Override
    public String getAmount(String qrText) {

        DoubleStream allMatches = getAllMatches(qrText);
        double max = allMatches.max().orElse(-1);

        if (max == -1) {
            return "";
        } else {
            return String.format("%.2f", max);
        }
    }


    public static DoubleStream getAllMatches(String text) {
        var matches = Pattern.compile("(?<=O:)[0-9]+[,.][0-9][0-9]")
                .matcher(text)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
        return matches.stream().map(o -> o.replace(",", ".")).mapToDouble(Double::parseDouble);
    }

}
