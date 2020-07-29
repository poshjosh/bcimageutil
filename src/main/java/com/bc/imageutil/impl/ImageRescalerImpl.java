package com.bc.imageutil.impl;

import com.bc.imageutil.ImageRescaler;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * @author hp
 */
public class ImageRescalerImpl implements ImageRescaler{
    
    @Override
    public BufferedImage scaleImage(Image image, int width, int height, int type) {

        if(width == -1 || height == -1) return null;

        BufferedImage bufferedImage = new BufferedImage(width, height, type);

        Graphics2D g = bufferedImage.createGraphics();

        // Note the use of BILNEAR filtering to enable smooth scaling
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // These are suppposed to improve image quality
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                                    RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

        g.setComposite(java.awt.AlphaComposite.Src);

//        g.setColor(Color.white);
//        g.fillRect(0,0,width,height);
//        g.drawImage(image,0,0,null);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        return bufferedImage;
    }
}
