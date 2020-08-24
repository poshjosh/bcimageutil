package com.bc.imageutil;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author hp
 */
public interface DrawConfig {
    
    default int [] getOffset(BufferedImage image, Rectangle2D rec) {
        return new int[]{0, 0};
    }
    
    default Font getFont(BufferedImage image, String stringToDraw) {
     
        double factor = stringToDraw.length() * 0.66;
        
        int fontSize = MathUtil.divide(image.getWidth(), factor).intValue();
        
        Font font = new Font(Font.SERIF, Font.BOLD, fontSize);

        return font;
    }
    
    default Color getColor(BufferedImage image, String stringToDraw) {
        return Color.BLACK;
    }
}
