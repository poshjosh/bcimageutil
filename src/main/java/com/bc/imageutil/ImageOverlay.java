package com.bc.imageutil;

import java.awt.image.BufferedImage;

/**
 * @author hp
 */
public interface ImageOverlay {

    void drawString(BufferedImage image, String stringToDraw, DrawConfig config);
}
