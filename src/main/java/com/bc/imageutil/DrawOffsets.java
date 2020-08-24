package com.bc.imageutil;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author hp
 */
public final class DrawOffsets {
    
    public static final DrawOffset percent(int percentFromTop, int percentFromLeft) {
        return new OffsetImpl(percentFromTop, percentFromLeft);
    }
    
    public static final DrawOffset centre() {
        return new CentreOffset();
    } 

    private static final class CentreOffset implements DrawOffset{
        @Override
        public int[] get(BufferedImage image, Rectangle2D rec) {
            int x = MathUtil.divide(image.getWidth() - rec.getWidth(), 2).intValue();
            int y = MathUtil.divide(image.getHeight() - rec.getHeight(), 2).intValue();
            return new int[]{x, y};
        }
    }
    
    private static final class OffsetImpl implements DrawOffset{
        
        private final int percentFromTop;
        private final int percentFromLeft;

        public OffsetImpl(int percentFromTop, int percentFromLeft) {
            this.percentFromTop = checkPercent(percentFromTop);
            this.percentFromLeft = checkPercent(percentFromLeft);
        }

        private int checkPercent(int n) {
            if(n < 0 || n > 100) {
                throw new IllegalArgumentException("Invalid percent value: " + 
                        n + ", expected number between 0 and 100 inclusive");
            }
            return n;
        }

        @Override
        public int[] get(BufferedImage image, Rectangle2D rec) {
            int x = image.getWidth() * (percentFromLeft / 100);
            int y = image.getHeight() * (percentFromTop / 100);
            return new int[]{x, y};
        }
    }
}
