package com.reseetto.application.receipts.services;

import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

@Slf4j
@Service
public class ImageOperationsServiceImpl implements ImageOperationsService {
    @Override
    public Double resize(Mat imageMat) {
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

    @Override
    public void grayscale(Mat imageMat) {
        Imgproc.cvtColor(imageMat, imageMat, COLOR_BGR2GRAY);
    }

    @Override
    public Optional<MatOfPoint> getFourPointsInScale(Mat imageMat, double scale) {
        Optional<MatOfPoint> biggest4EdgedContour = getBiggest4EdgedContour(getContours(getEdges(imageMat)), imageMat.size().area());

        MatOfPoint target = new MatOfPoint();
        if (biggest4EdgedContour.isPresent()) {
            Core.multiply(biggest4EdgedContour.get(), new Scalar(scale, scale), target);
            return Optional.of(target);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void gaussianBlur(Mat imageMat) {
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(5, 5), 0);
    }

    @Override
    public void warpPerspective(Mat copyOfOriginalImage, MatOfPoint fourPoints) {
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

    @Override
    public Mat getEdges(Mat imageMat) {
        Mat edges = new Mat();
        Imgproc.Canny(imageMat, edges, 75, 200);

        return edges;
    }

    @Override
    public List<MatOfPoint> getContours(Mat edges) {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        return contours;
    }

    @Override
    public Optional<MatOfPoint> getBiggest4EdgedContour(List<MatOfPoint> contours, double wholeImageArea) {

        // TODO this sorting works, the one below does not
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < contours.size(); i++) {
            var contourArea = Imgproc.contourArea(contours.get(i));

            // don't take ridiculously small contours into account
            if (contourArea > 0.25 * wholeImageArea) {
                map.put(i, contourArea);
            }
        }

        LinkedHashMap<Integer, Double> collect = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        List<MatOfPoint> sortedContours = new ArrayList<>();
        for (var entry : collect.entrySet()) {
            sortedContours.add(contours.get(entry.getKey()));
        }

        Collections.reverse(sortedContours);

        // TODO why does this not work
//        contours.sort((a, b) -> (int) (Imgproc.contourArea(b) - Imgproc.contourArea(a)));

        MatOfPoint2f biggest4EdgedContour = null;

        for (MatOfPoint contour : sortedContours) {
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

    @Override
    public Mat loadImage(String imagePath) {
        return Imgcodecs.imread(imagePath);
    }

    @Override
    public void saveImage(Mat imageMatrix, String targetPath) {
        Imgcodecs.imwrite(targetPath, imageMatrix);
    }

    // TODO
    @Override
    public List<Point> orderPointsTopLeftTopRightBottomRightBottomLeft(List<Point> fourPoints) {

        if (fourPoints.size() != 4) {
            throw new IllegalArgumentException("there should be exactly 4 points in the list");
        }

        return List.of();
    }

    @Override
    public Optional<File> fixPerspective(File file) {
        Mat imageMat = loadImage(file.getAbsolutePath());
        Mat copyOfOriginalImage = imageMat.clone();

//        saveImage(imageMat, "public/original" + "_" + file.getName());

        double scale = resize(imageMat);
//        saveImage(imageMat, "public/resized" + "_" + file.getName());

        grayscale(imageMat);
//        saveImage(imageMat, "public/grayscale" + "_" + file.getName());

        gaussianBlur(imageMat);
//        saveImage(imageMat, "public/blurred" + "_" + file.getName());

        Optional<MatOfPoint> fourPoints = getFourPointsInScale(imageMat, scale);

        if (fourPoints.isPresent()) {
            warpPerspective(copyOfOriginalImage, fourPoints.get());
            saveImage(copyOfOriginalImage, "public/perspective_warp" + "_" + file.getName());
            return Optional.of(new File("public/perspective_warp" + "_" + file.getName()));
        } else {
            return Optional.empty();
        }
    }

    private MatOfPoint2f getMaxBoxOfPoints(MatOfPoint points) {
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
