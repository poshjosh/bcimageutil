package com.bc.imageutil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hp
 */
public interface ImageReader {
 
    BufferedImage read(InputStream in, String contentType) throws IOException;

    ImageInfo getInfo(InputStream in);
}
