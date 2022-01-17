package com.bifanas.services;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

public class ImageOperations {
    public static Double resize(Mat matImage) {
        final Size size = matImage.size();
        final double height = size.height;
        final double width = size.width;
        final double ratio = height / width;
        final double newWidth = 500;
        final double newHeight = ratio * newWidth;
        Imgproc.resize(matImage, matImage, new Size(newWidth, newHeight));
        final double scale = width / newWidth;
        return scale;
    }

    public static void grayscale(Mat matImage) {
        Imgproc.cvtColor(matImage, matImage, COLOR_BGR2GRAY);
    }

    public static void gaussianBlur(Mat matImage) {
        Imgproc.GaussianBlur(matImage, matImage, new Size(5, 5), 0);
    }

    public static Mat getEdges(Mat matImage) {
        Mat edges = new Mat();
        Imgproc.Canny(matImage, edges, 75, 200);

        return edges;
    }

    public static List<MatOfPoint> getContours(Mat edges) {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        return contours;
    }

    public static MatOfPoint getBiggest4EdgedContour(List<MatOfPoint> contours) {
        contours.sort((a, b) -> (int) (Imgproc.contourArea(b) - Imgproc.contourArea(a)));

        MatOfPoint2f biggest4EdgedContour = null;

        for (MatOfPoint contour : contours) {
            MatOfPoint2f contourButMatOfPoint2f = new MatOfPoint2f();
            contour.convertTo(contourButMatOfPoint2f, CvType.CV_32F);
            MatOfPoint2f res = new MatOfPoint2f();
            double v = Imgproc.arcLength(contourButMatOfPoint2f, true);
            Imgproc.approxPolyDP(contourButMatOfPoint2f, res, 0.02 * v, true);
            if (res.toList().size() == 4) {
                biggest4EdgedContour = res;
                break;
            }
        }

        if (biggest4EdgedContour == null) {
            throw new RuntimeException("no biggest4EdgedContour");
        }

        MatOfPoint biggest4EdgedContourAsMatOfPoint = new MatOfPoint();
        biggest4EdgedContour.convertTo(biggest4EdgedContourAsMatOfPoint, CvType.CV_32S);

        return biggest4EdgedContourAsMatOfPoint;
    }

    public static Mat loadImage(String imagePath) {
        return Imgcodecs.imread(imagePath);
    }

    public static void saveImage(Mat imageMatrix, String targetPath) {
        Imgcodecs.imwrite(targetPath, imageMatrix);
    }

    public static Mat fixPerspective(Mat imageMat, MatOfPoint fourPoints) {
        return imageMat;//TODO
    }

    public static List<Point> orderPointsTopLeftTopRightBottomRightBottomLeft(List<Point> fourPoints) {

        if (fourPoints.size() != 4) {
            throw new IllegalArgumentException("there should be exactly 4 points in the list");
        }

        // get the max box the points fit in
        double minX = fourPoints.stream().map(p -> p.x).min(Double::compare).orElse(Double.MIN_VALUE);
        double maxX = fourPoints.stream().map(p -> p.x).max(Double::compare).orElse(Double.MAX_VALUE);
        double minY = fourPoints.stream().map(p -> p.y).min(Double::compare).orElse(Double.MIN_VALUE);
        double maxY = fourPoints.stream().map(p -> p.y).max(Double::compare).orElse(Double.MAX_VALUE);

        Point borderTopLeft = new Point(minX, maxY);
        Point borderTopRight = new Point(maxX, maxY);
        Point borderBottomRight = new Point(maxX, minY);
        Point borderBottomLeft = new Point(minX, minY);

        return List.of();
    }
}
