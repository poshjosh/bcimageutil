package com.bc.imageutil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author hp
 */
public interface ImageWriter {
    
    boolean write(BufferedImage scaledImage, String fileExtension, Path target)
            throws IOException;
}
