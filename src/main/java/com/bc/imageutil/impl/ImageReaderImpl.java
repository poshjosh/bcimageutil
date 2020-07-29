package com.bc.imageutil.impl;

import com.bc.imageutil.ImageInfo;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Iterator;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * @author hp
 */
public class ImageReaderImpl implements com.bc.imageutil.ImageReader{
    
    @Override
    public BufferedImage read(InputStream in, String contentType) throws IOException {

        if(contentType == null) {
            contentType = URLConnection.guessContentTypeFromStream(in);
            if(contentType == null) {
                ImageInfo imgInfo = this.getInfo(in);
                if(imgInfo != null) {
                    contentType = imgInfo.getMimeType();
                }
            }
        }
        
        ImageInputStream iis = javax.imageio.ImageIO.createImageInputStream(in);
        
        Iterator<ImageReader> readers = javax.imageio.ImageIO.getImageReaders(iis);
        
        if(readers == null || !readers.hasNext()) {
            
            // We resort to using this because, some image are not properly
            // formated and no registered image reader will be returned for them
            readers = javax.imageio.ImageIO.getImageReadersByMIMEType(contentType);
        }
        
        return getImage(iis, readers);
    }

    @Override
    public ImageInfo getInfo(InputStream in) {
        final ImageInfo imgInfo = new ImageInfo();
        imgInfo.setInput(in);
        imgInfo.check();
        return imgInfo;
    }
    
    /**
     * This method was simply adapted from
     * {@link javax.imageio.ImageIO#read(javax.imageio.stream.ImageInputStream)}
     * @see javax.imageio.ImageIO#read(javax.imageio.stream.ImageInputStream) 
     */
    private BufferedImage getImage(ImageInputStream stream, 
            Iterator<ImageReader> iter) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("stream == null!");
        }
        if (!iter.hasNext()) {
            return null;
        }
        ImageReader reader = (ImageReader)iter.next();
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(stream, true, true);
        BufferedImage bi;
        try {
            bi = reader.read(0, param);
        } finally {
            reader.dispose();
            stream.close();
        }
        return bi;
    }
}
