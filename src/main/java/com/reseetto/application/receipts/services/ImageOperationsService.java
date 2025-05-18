package com.reseetto.application.receipts.services;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface ImageOperationsService {
    Double resize(Mat imageMat);

    void grayscale(Mat imageMat);

    Optional<MatOfPoint> getFourPointsInScale(Mat imageMat, double scale);

    void gaussianBlur(Mat imageMat);

    void warpPerspective(Mat copyOfOriginalImage, MatOfPoint fourPoints);

    Mat getEdges(Mat imageMat);

    List<MatOfPoint> getContours(Mat edges);

    Optional<MatOfPoint> getBiggest4EdgedContour(List<MatOfPoint> contours, double wholeImageArea);

    Mat loadImage(String imagePath);

    void saveImage(Mat imageMatrix, String targetPath);

    // TODO
    List<Point> orderPointsTopLeftTopRightBottomRightBottomLeft(List<Point> fourPoints);

    Optional<File> fixPerspective(File file);
}
