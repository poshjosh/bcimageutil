package com.bc.imageutil;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author hp
 */
@FunctionalInterface
public interface ImageRescaler {
    
    BufferedImage scaleImage(Image image, int width, int height, int type);
}
