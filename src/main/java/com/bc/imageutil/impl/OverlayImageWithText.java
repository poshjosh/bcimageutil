package com.bc.imageutil.impl;

import com.bc.imageutil.ImageOverlay;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import com.bc.imageutil.DrawConfig;

/**
 * @author hp
 */
public class OverlayImageWithText implements ImageOverlay {
    
    public OverlayImageWithText() { }
    
    @Override
    public void drawString(BufferedImage image, String stringToDraw, DrawConfig config) {
        
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setFont(config.getFont(image, stringToDraw));
        g.setColor(config.getColor(image, stringToDraw));

        Rectangle2D rec = g.getFontMetrics().getStringBounds(stringToDraw, g);
        
//        System.out.println("Image["+image.getWidth()+", "+image.getHeight()+"]");
//        System.out.println("Rectangle2D["+rec.getWidth()+", "+rec.getHeight()+"]");
        
        final int [] xy = config.getOffset(image, rec);
        
//        System.out.println("DrawOffset"+Arrays.toString(xy)+"");
        
        g.drawString(stringToDraw, xy[0], xy[1]);

        g.dispose();
    }
}
/**
 * 
 * 
    
    public double [] getTopLeftStart(BufferedImage image, String stringToDraw) {
        Graphics g = image.getGraphics();
        Rectangle2D rec = g.getFontMetrics().getStringBounds(stringToDraw, g);
        final double x_off = this.getXOffset(image.getWidth(), rec.getWidth());
        final double y_off = this.getYOffset(image.getHeight(), rec.getHeight());
        return new double[]{x_off, y_off};
    }

    public double [] getTopRightStart(BufferedImage image, String stringToDraw) {
        Graphics g = image.getGraphics();
        Rectangle2D rec = g.getFontMetrics().getStringBounds(stringToDraw, g);
        final double x_off = this.getXOffset(image.getWidth(), rec.getWidth());
        final double y_off = this.getYOffset(image.getHeight(), rec.getHeight());
        return new double[]{x_off + rec.getWidth(), y_off};
    }

    public double [] getBottomRightStart(BufferedImage image, String stringToDraw) {
        float wx = 7.5f;
        float hx = 0.75f;
        Graphics g = image.getGraphics();
        Rectangle2D rec = g.getFontMetrics().getStringBounds(stringToDraw, g);
System.out.println("Image: ["+image.getWidth()+":"+image.getHeight()+"]");        
System.out.println("Rectangle: ["+rec.getWidth()+":"+rec.getHeight()+"]");        
        double x_edge = this.getXOffset(image.getWidth(), rec.getWidth() * wx);
        double y_edge = this.getYOffset(image.getHeight(), rec.getHeight() * hx);
System.out.println("X edge: "+x_edge+", Y edge:"+y_edge);        

        double x = image.getWidth() - (x_edge + rec.getWidth() * wx);
        double y = image.getHeight() - (y_edge + rec.getHeight() * hx);
        
        return new double[]{x, y};
    }

    public double [] getBottomLeftStart(BufferedImage image, String stringToDraw) {
        Graphics g = image.getGraphics();
        Rectangle2D rec = g.getFontMetrics().getStringBounds(stringToDraw, g);
        final double x_off = this.getXOffset(image.getWidth(), rec.getWidth());
        final double y_off = this.getYOffset(image.getHeight(), rec.getHeight());
        return new double[]{x_off, y_off + rec.getHeight()};
    }
    
    public double getXOffset(double parentWidth, double childWidth) {
        double x = getOffset(parentWidth, childWidth);
        return x;
    }
    
    public double getYOffset(double parentHeight, double childHeight) {
        double y = getOffset(parentHeight, childHeight);
        return y;
    }
    
    private double getOffset(double parent, double child) {
        double min = MathUtil.divide(parent, 10).doubleValue();
        if(min + child > parent) {
            min = min / 2;
            if(min + child > parent) {
                min = 0;
            }
        }
        return min;
    }

    public void drawString(BufferedImage image, String stringToDraw, Font font, Color color, int x, int y) {
        
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setFont(font);
        g.setColor(color);

        g.drawString(stringToDraw, x, y);

        g.dispose();
    }
 * 
 */