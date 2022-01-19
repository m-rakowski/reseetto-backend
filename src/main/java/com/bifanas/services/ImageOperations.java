package com.bifanas.services;

import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

@Slf4j
public class ImageOperations {
    public static Double resize(Mat imageMat) {
        final Size size = imageMat.size();
        final double height = size.height;
        final double width = size.width;
        final double ratio = height / width;
        final double newWidth = 500;
        final double newHeight = ratio * newWidth;
        Imgproc.resize(imageMat, imageMat, new Size(newWidth, newHeight));
        final double scale = width / newWidth;
        return scale;
    }

    public static void grayscale(Mat imageMat) {
        Imgproc.cvtColor(imageMat, imageMat, COLOR_BGR2GRAY);
    }

    public static Optional<MatOfPoint> getFourPointsInScale(Mat imageMat, double scale) {
        Optional<MatOfPoint> biggest4EdgedContour = getBiggest4EdgedContour(getContours(getEdges(imageMat)));

        MatOfPoint target = new MatOfPoint();
        if (biggest4EdgedContour.isPresent()) {
            Core.multiply(biggest4EdgedContour.get(), new Scalar(scale, scale), target);
            return Optional.of(target);
        } else {
            return Optional.empty();
        }
    }

    public static void gaussianBlur(Mat imageMat) {
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(5, 5), 0);
    }

    public static void warpPerspective(Mat copyOfOriginalImage, MatOfPoint fourPoints) {
        Imgproc.drawContours(copyOfOriginalImage, List.of(fourPoints), -1, new Scalar(0, 255, 0), 3);

        MatOfPoint2f src = new MatOfPoint2f(
                fourPoints.toList().get(0),
                fourPoints.toList().get(1),
                fourPoints.toList().get(2),
                fourPoints.toList().get(3)
        );

        MatOfPoint2f dst = getMaxBoxOfPoints(fourPoints);

        Mat warpMat = Imgproc.getPerspectiveTransform(src, dst);
        Imgproc.warpPerspective(copyOfOriginalImage, copyOfOriginalImage, warpMat, copyOfOriginalImage.size());
    }

    public static Mat getEdges(Mat imageMat) {
        Mat edges = new Mat();
        Imgproc.Canny(imageMat, edges, 75, 200);

        return edges;
    }

    public static List<MatOfPoint> getContours(Mat edges) {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        return contours;
    }

    public static Optional<MatOfPoint> getBiggest4EdgedContour(List<MatOfPoint> contours) {
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
            return Optional.empty();
        }

        MatOfPoint biggest4EdgedContourAsMatOfPoint = new MatOfPoint();
        biggest4EdgedContour.convertTo(biggest4EdgedContourAsMatOfPoint, CvType.CV_32S);

        return Optional.of(biggest4EdgedContourAsMatOfPoint);
    }

    public static Mat loadImage(String imagePath) {
        return Imgcodecs.imread(imagePath);
    }

    public static void saveImage(Mat imageMatrix, String targetPath) {
        Imgcodecs.imwrite(targetPath, imageMatrix);
    }

    // TODO
    public static List<Point> orderPointsTopLeftTopRightBottomRightBottomLeft(List<Point> fourPoints) {

        if (fourPoints.size() != 4) {
            throw new IllegalArgumentException("there should be exactly 4 points in the list");
        }

        return List.of();
    }

    public static Optional<File> fixPerspective(File imagePath) {
        Mat imageMat = loadImage(imagePath.getAbsolutePath());
        Mat copyOfOriginalImage = imageMat.clone();

//        saveImage(imageMat, "0_original" + "_" + imagePath.getName());

        double scale = resize(imageMat);
//        saveImage(imageMat, "1_resized" + "_" + imagePath.getName());

        grayscale(imageMat);
//        saveImage(imageMat, "2_grayscaled" + "_" + imagePath.getName());

        gaussianBlur(imageMat);
//        saveImage(imageMat, "3_blurred" + "_" + imagePath.getName());

        Optional<MatOfPoint> fourPoints = getFourPointsInScale(imageMat, scale);

        if (fourPoints.isPresent()) {
            warpPerspective(copyOfOriginalImage, fourPoints.get());
            saveImage(copyOfOriginalImage, "5_after_perspective_warp" + "_" + imagePath.getName());
            return Optional.of(new File("5_after_perspective_warp" + "_" + imagePath.getName()));
        } else {
            return Optional.empty();
        }
    }

    private static MatOfPoint2f getMaxBoxOfPoints(MatOfPoint points) {
        // get the max box the points fit in
        double minX = points.toList().stream().map(p -> p.x).min(Double::compare).orElse(Double.MIN_VALUE);
        double maxX = points.toList().stream().map(p -> p.x).max(Double::compare).orElse(Double.MAX_VALUE);
        double minY = points.toList().stream().map(p -> p.y).min(Double::compare).orElse(Double.MIN_VALUE);
        double maxY = points.toList().stream().map(p -> p.y).max(Double::compare).orElse(Double.MAX_VALUE);

        Point borderTopLeft = new Point(minX, minY);
        Point borderTopRight = new Point(maxX, minY);
        Point borderBottomRight = new Point(maxX, maxY);
        Point borderBottomLeft = new Point(minX, maxY);

        return new MatOfPoint2f(
                borderTopLeft,
                borderBottomLeft,
                borderBottomRight,
                borderTopRight
        );
    }
}
