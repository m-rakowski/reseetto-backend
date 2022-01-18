package com.bifanas.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;


@Slf4j
@Service
public class OpenCVServiceImpl implements OpenCVService {

    @Override
    public File fixImagePerspective(File imagePath) {
        return ImageOperations.fixPerspective(imagePath);
    }
}
