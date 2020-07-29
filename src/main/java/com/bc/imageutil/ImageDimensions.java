/**
 * @(#)ImageDimensions.java   03-May-2010 13:03:47
 *
 * Copyright 2009 BC Enterprise, Inc. All rights reserved.
 * BCE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.bc.imageutil;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.math.MathContext;

/**
 * @author  chinomso bassey ikwuagwu
 * @version 1.0
 * @since   1.0
 */
// Image sizes are not properties because once the sizes are decided
// they must never be changed, otherwise all images have to be resized.
public class ImageDimensions {
    
    public static Dimension small() {
//@related_1 - SMALL is related to style '.icon' 
        return new Dimension(75, 75);
    }

    public static Dimension medium() {
//@related_1 - MEDIUM is related to style '.mediumImage' 
        return new Dimension(424, 424);
    }

    public static Dimension large() {
        return new Dimension(1024, 1024);
    }

    public static Dimension getSuggestedDimension(BufferedImage buffImage, Dimension size) {
        int width;
        int height;
        if(size == null) {
            width = buffImage.getWidth();
            height = buffImage.getHeight();
        }else{
            Dimension newSize = rescale(
                    buffImage.getWidth(), buffImage.getHeight(), size.width, size.height);
            width = newSize.width;
            height = newSize.height;
        }
        return new Dimension(width, height);
    }

    public static Dimension rescale(Dimension source, Dimension target) {

        return rescale(source.width, source.height, target.width, target.height);
    }

    public static Dimension rescale(int srcW,  int srcH, int tgtW, int tgtH) {

        boolean isWidth = srcW > srcH;

        final MathContext ctx = ImageManager.getDefaultMathContext();
        
        double ratio = MathUtil.divide(ctx, srcW, srcH).doubleValue();
//System.out.println("Source width: "+srcW+",  Source height: "+srcH+",  Target width: "+tgtW+",  Target height: "+tgtH);
//System.out.println("Ratio: "+ratio);
        if(isWidth) {
            int height = MathUtil.divide(ctx, tgtW, ratio).intValue();
            return new Dimension(tgtW, height);
        }else{
            int width = MathUtil.multiply(ctx, ratio, tgtH).intValue();
            return new Dimension(width, tgtH);
        }
    }
}//~END
