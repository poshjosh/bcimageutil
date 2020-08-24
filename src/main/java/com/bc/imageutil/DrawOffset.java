package com.bc.imageutil;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author hp
 */
public interface DrawOffset {
    int [] get(BufferedImage image, Rectangle2D rec);
}
