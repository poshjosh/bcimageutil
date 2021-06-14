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
    public BufferedImage scaleImage(Image source, int width, int height, int type) {

        if(width == -1 || height == -1) return null;

        BufferedImage targetImage = new BufferedImage(width, height, type);

        Graphics2D targetGraphics = targetImage.createGraphics();

        // Note the use of BILNEAR filtering to enable smooth scaling
        targetGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // These are suppposed to improve image quality
        targetGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,
                                    RenderingHints.VALUE_RENDER_QUALITY);
        targetGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

        targetGraphics.setComposite(java.awt.AlphaComposite.Src);

//        targetGraphics.setColor(Color.white);
//        targetGraphics.fillRect(0,0,width,height);
//        targetGraphics.drawImage(image,0,0,null);
        targetGraphics.drawImage(source, 0, 0, width, height, null);

        targetGraphics.dispose();

        return targetImage;
    }
}
