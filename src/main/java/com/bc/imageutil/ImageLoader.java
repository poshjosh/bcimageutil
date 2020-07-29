package com.bc.imageutil;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author hp
 */
public interface ImageLoader {

    BufferedImage grabImage(URL url) throws IOException, InterruptedException;

    BufferedImage grabImage(byte[] bytes) throws IOException, InterruptedException;

    void loadImage(Image image);
    
}
