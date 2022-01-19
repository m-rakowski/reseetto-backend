package com.bifanas.services;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class TesseractUtils {

    public static String getTotalFromText(String text) {

        DoubleStream allMatches = getAllMatches(text);
        Double max = allMatches.max().orElse(-1);

        if (max == -1) {
            return "";
        } else {
            return String.format("%.2f", max);
        }
    }

    public static DoubleStream getAllMatches(String text) {
        var matches = Pattern.compile("[0-9]+[,.][0-9][0-9](?![0-9])")
                .matcher(text)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
        return matches.stream().map(o -> o.replace(",", ".")).mapToDouble(Double::parseDouble);
    }
}
