package com.bifanas.services;

import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.List;

import static com.bifanas.services.ImageOperations.*;

@Slf4j
@Service
public class OpenCVServiceImpl implements OpenCVService {

    @Override
    public File fixImage(File imagePath) {

        Mat imageMat = loadImage(imagePath.getAbsolutePath());
        Mat copyOfLoadedImage = imageMat.clone();

        Double scale = resize(imageMat);
        saveImage(imageMat, "0_resized" + "_" + imagePath.getName());

        Mat copyOfResized = imageMat.clone();
        Mat copyOfResized2 = imageMat.clone();

        grayscale(imageMat);
        saveImage(imageMat, "1_grayscaled" + "_" + imagePath.getName());

        gaussianBlur(imageMat);
        saveImage(imageMat, "2_blurred" + "_" + imagePath.getName());

        MatOfPoint fourPoints = getBiggest4EdgedContour(getContours(getEdges(imageMat)));
        Core.multiply( fourPoints, new Scalar(scale, scale), fourPoints );

        Imgproc.drawContours(copyOfLoadedImage, List.of(fourPoints), -1, new Scalar(0, 255, 0), 3);
        saveImage(copyOfLoadedImage, "3_four_points" + "_" + imagePath.getName());

        return new File("3_four_points" + "_" + imagePath.getName());

    }
}
