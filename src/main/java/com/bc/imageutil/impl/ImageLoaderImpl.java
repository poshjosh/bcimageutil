/*
 * ImageLoaderImpl.java
 *
 * Created on April 27, 2007, 10:25 AM
 */

package com.bc.imageutil.impl;

import com.bc.imageutil.ImageLoader;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author nonso
 */
public class ImageLoaderImpl implements ImageLoader {
    
    private static final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
    private static transient final ColorModel RGB_OPAQUE =
        new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
    
    static transient int loadStatus = 0;

    protected final Component component = new Component() {};

    protected final MediaTracker tracker = new MediaTracker(component);

    /**
     * Id used in loading images from MediaTracker.
     */
    private int mediaTrackerID;
    
    public ImageLoaderImpl() { }

    @Override
    public void loadImage(Image image) {

        synchronized(tracker) {
            
            int id = getNextID();

            tracker.addImage(image, id);

            try {
                tracker.waitForID(id, 0);
            } catch (InterruptedException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "", e);
            }
            
            loadStatus = tracker.statusID(id, false);

            tracker.removeImage(image, id);
        }
    }
    
    /**
     * Returns an ID to use with the MediaTracker in loading an image.
     */
    private int getNextID() {
        synchronized(tracker) {
            return ++mediaTrackerID;
        }
    }

    @Override
    public BufferedImage grabImage(URL url) throws IOException, InterruptedException {
        Image img = Toolkit.getDefaultToolkit().createImage(url);
        return grabImage(img);
    }    
        
    @Override
    public BufferedImage grabImage(byte [] bytes) throws IOException, InterruptedException {
        final Image img = Toolkit.getDefaultToolkit().createImage(bytes);
        return grabImage(img);
    }    
        
    private BufferedImage grabImage(Image img) throws IOException, InterruptedException {

        ImageLoaderImpl loader = new ImageLoaderImpl();
        loader.loadImage(img);
        
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        
        if(width == -1 || height == -1) {
            return null;
        }
        
        PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, true);
        
        pg.grabPixels();
        
        if(pg.getPixels() == null) {
            return null;
        }

        DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
        
        WritableRaster raster = Raster.createPackedRaster(buffer, pg.getWidth(), pg.getHeight(), pg.getWidth(), RGB_MASKS, null);
        
        return new BufferedImage(RGB_OPAQUE, raster, false, null);
    }
}
