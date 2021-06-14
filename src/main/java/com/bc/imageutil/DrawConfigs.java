package com.bc.imageutil;

import com.bc.imageutil.DrawConfig;
import com.bc.imageutil.MathUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author hp
 */
public final class DrawConfigs {
    
    public static final DrawConfig fromTopLeft(float fromTop, float fromLeft, Font font, Color color) {
        return new DrawConfigImpl(fromTop, fromLeft, font, color);
    }
    
    public static final DrawConfig fromBottomRight(float fromBottom, float fromRight, Font font, Color color) {
        return new BottomRight(fromBottom, fromRight, font, color);
    }

    public static final DrawConfig fromBottomLeft(float fromBottom, float fromLeft, Font font, Color color) {
        return new BottomLeft(fromBottom, fromLeft, font, color);
    }
    
    public static final DrawConfig centre() {
        return centre(null, null);
    }
            
    public static final DrawConfig centre(Font font, Color color) {
        return new Centre(font, color);
    } 

    private static final class Centre implements DrawConfig{
        
        private final DrawConfig delegate;

        public Centre(Font font, Color color) {
            this.delegate = new DrawConfigImpl(font, color);
        }
        
        @Override
        public Font getFont(BufferedImage image, String stringToDraw) {
            return delegate.getFont(image, stringToDraw);
        }

        @Override
        public Color getColor(BufferedImage image, String stringToDraw) {
            return delegate.getColor(image, stringToDraw);
        }
        
        @Override
        public int[] getOffset(BufferedImage image, Rectangle2D rec) {
            int x = MathUtil.divide(image.getWidth() - rec.getWidth(), 2).intValue();
            int y = MathUtil.divide(image.getHeight() - rec.getHeight(), 2).intValue();
            return new int[]{x, y};
        }
    }

    private static final class BottomRight implements DrawConfig{
        
        private final float fromBottom;
        private final float fromRight;
        private final DrawConfig delegate;
        
        public BottomRight(float fromBottom, float fromRight, Font font, Color color) {
            this.fromBottom = check(fromBottom);
            this.fromRight = check(fromRight);
            this.delegate = new DrawConfigImpl(font, color);
        }
        
        
        @Override
        public Font getFont(BufferedImage image, String stringToDraw) {
            return delegate.getFont(image, stringToDraw);
        }

        @Override
        public Color getColor(BufferedImage image, String stringToDraw) {
            return delegate.getColor(image, stringToDraw);
        }

        @Override
        public int[] getOffset(BufferedImage image, Rectangle2D rec) {
            int horizontal = (int)(image.getWidth() * fromRight);
//            System.out.println("Horizontal offset: " + horizontal);
            int x = (int)(image.getWidth() - rec.getWidth() - horizontal);
            int vertical = (int)(image.getHeight() * fromBottom);
//            System.out.println("Vertical offset: " + vertical);
            int y = (int)(image.getHeight() - rec.getHeight() - vertical);
            return new int[]{x, y};
        }
    }

    private static final class BottomLeft implements DrawConfig{
        
        private final float fromBottom;
        private final float fromLeft;
        private final DrawConfig delegate;
        
        public BottomLeft(float fromBottom, float fromLeft, Font font, Color color) {
            this.fromBottom = check(fromBottom);
            this.fromLeft = check(fromLeft);
            this.delegate = new DrawConfigImpl(font, color);
        }
        
        @Override
        public Font getFont(BufferedImage image, String stringToDraw) {
            return delegate.getFont(image, stringToDraw);
        }

        @Override
        public Color getColor(BufferedImage image, String stringToDraw) {
            return delegate.getColor(image, stringToDraw);
        }
        
        @Override
        public int[] getOffset(BufferedImage image, Rectangle2D rec) {
            int x = (int)(fromLeft);
            int vertical = (int)(image.getHeight() * fromBottom);
            int y = (int)(image.getHeight() - rec.getHeight() - vertical);
            return new int[]{x, y};
        }
    }
    
    private static final class DrawConfigImpl implements DrawConfig{
        
        private final float fromTop;
        private final float fromLeft;
        private final Font font;
        private final Color color;

        public DrawConfigImpl(Font font, Color color) {
            this(0, 0, font, color);
        }
        
        public DrawConfigImpl(float fromTop, float fromLeft, Font font, Color color) {
            this.fromTop = check(fromTop);
            this.fromLeft = check(fromLeft);
            this.font = font;
            this.color = color;
        }

        @Override
        public Font getFont(BufferedImage image, String stringToDraw) {
            if(this.font != null) {
                return this.font;
            }else{
                return DrawConfig.super.getFont(image, stringToDraw);
            }
        }

        @Override
        public Color getColor(BufferedImage image, String stringToDraw) {
            if(this.color != null) {
                return this.color;
            }else{
                return DrawConfig.super.getColor(image, stringToDraw); 
            }
        }

        @Override
        public int[] getOffset(BufferedImage image, Rectangle2D rec) {
            int x = (int)(image.getWidth() * fromLeft);
            int y = (int)(image.getHeight() * fromTop);
            return new int[]{x, y};
        }
    }

    private static float check(float n) {
        if(n < 0 || n > 1) {
            throw new IllegalArgumentException("Invalid percent value: " + 
                    n + ", expected number between 0 and 100 inclusive");
        }
        return n;
    }
}
