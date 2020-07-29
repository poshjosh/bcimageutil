/**
 * @(#)ImageManager.java   23-Apr-2010 07:00:24
 *
 * Copyright 2009 BC Enterprise, Inc. All rights reserved.
 * BCE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.bc.imageutil.impl;

import com.bc.imageutil.ImageDimensions;
import com.bc.imageutil.ImageInfo;
import com.bc.imageutil.ImageManager;
import com.bc.imageutil.ImageOverlay;
import com.bc.imageutil.ImageReader;
import com.bc.imageutil.ImageRescaler;
import com.bc.imageutil.Util;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import javax.activation.MimetypesFileTypeMap;
import javax.swing.filechooser.FileSystemView;
import java.awt.Dimension;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * @author  chinomso bassey ikwuagwu
 * @version 1.0
 * @since   1.0
 */
public class ImageManagerImpl implements Serializable, ImageManager {
    
    private final Function<String, String> getPathForFilename;
    
    private final com.bc.imageutil.ImageReader imageReader;
    
    private final ImageOverlay imageOverlay;

    private final ImageRescaler imageRescaler;

    public ImageManagerImpl() {
        this((relativePath) -> relativePath);
    }
    
    public ImageManagerImpl(String dirPath) {
        this((relativePath) -> Paths.get(dirPath, relativePath).toString());
    }
    
    public ImageManagerImpl(Function<String, String> getPathForFileName) {
        this(getPathForFileName, new com.bc.imageutil.impl.ImageReaderImpl(), 
                new OverlayImageWithText(), new ImageRescalerImpl());
    }

    public ImageManagerImpl(
            Function<String, String> getPathForFilename, ImageOverlay imageOverlay) {
        this(getPathForFilename, new com.bc.imageutil.impl.ImageReaderImpl(), 
                imageOverlay, new ImageRescalerImpl());
    }
    
    public ImageManagerImpl(
            Function<String, String> getPathForFilename, ImageReader imageReader, 
            ImageOverlay imageOverlay, ImageRescaler imageRescaler) {
        this.getPathForFilename = getPathForFilename;
        this.imageReader = imageReader;
        this.imageOverlay = imageOverlay;
        this.imageRescaler = imageRescaler;
    }
    
    @Override
    public ImageInfo getInfo(InputStream in) {
        return imageReader.getInfo(in);
    }

    @Override
    public BufferedImage read(InputStream in, String contentType) throws IOException {
        return imageReader.read(in, contentType);
    }

    @Override
    public void drawString(BufferedImage image, String stringToDraw) {
        imageOverlay.drawString(image, stringToDraw);
    }
    
    @Override
    public String getPath(String relativePath) {
        return this.getPathForFilename.apply(relativePath);
    }

    @Override
    public Dimension getSuggestedDimension(BufferedImage buffImage, Dimension size) {
        return ImageDimensions.getSuggestedDimension(buffImage, size);
    }

    @Override
    public String getPathForFilename(String filename) {
        String relativePath = this.buildRelativePath(filename);
        return this.getPath(relativePath);
    }
    
    @Override
    public String buildRelativePath(String filename) { 
        if(!filename.startsWith("/") && !filename.startsWith("\\")) {
            filename = File.separatorChar + filename;
        }
        return filename;
    }
    
    private String p_accessViaGetter;
    /**
     * @return Text of form '/yyyy/mm' e.g '/2015/07'
     */
    @Override
    public String getYearMonthPathPrefix() {
        if(p_accessViaGetter == null) {
            Calendar cal = Calendar.getInstance(); 
            StringBuilder builder = new StringBuilder();
            builder.append(File.separatorChar).append(cal.get(Calendar.YEAR));
            builder.append(File.separatorChar);
            final int MM = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH is 'zero' based so we add 1
            if(MM < 10) {
                // Using char, i.e '0' + month often yields inconsistent results 
                // So we use string, i.e "0" + month or THE int literal
                //
                builder.append(0);
            }
            builder.append(MM); 
            p_accessViaGetter = builder.toString();
        }
        return p_accessViaGetter;
    }
    
    public String getFilename(Object id, String fname, String columnName) {
        String output;
        String ext = Util.getExtension(fname, null);
        if(ext == null) {
            output = fname;
        }else{
            output = this.getFilenameForExtension(id, columnName, ext);
        }
        
if(Logger.getLogger(this.getClass().getName()).isLoggable(Level.FINER)) {       
    Logger.getLogger(this.getClass().getName()).log(Level.FINER, 
    "Column: {0}, Part.name: {1}, filename: {2}", new Object[]{columnName, fname, output});
}

        return output;
    }
    
    public String getFilenameForExtension(Object id, String columnName, String ext) {
        StringBuilder builder = new StringBuilder();
        if(id == null) {
            id = generateId();
        }
        builder.append(id).append('_');
        builder.append(columnName).append('.').append(ext);
        return builder.toString();
    }
    
    private static volatile int unique;
    public Object generateId() {
        synchronized(this) {
            return Long.toHexString(System.currentTimeMillis()) + '_' + unique++;
        }
    }
    
    public String getIconPathForImage(String imagePath) {

        int n = imagePath.lastIndexOf('_');

        if(n == -1) {
            
            return null;
            
        }else{

            int index = n + 1;

            return imagePath.substring(0, index) + "icon" + imagePath.substring(index);
        }
    }

    public int getImageLimit() {
        return 100;
    }
    
    public String getImageName(int i) {
        return "image" + (i+1);
    }

    public String getIconName(int i) {
        return getImageName(i) + "_icon";
    }
    
    public int getImageIndex(String imageName) {
        // Remove all non-digits
        String digits = imageName.replaceAll("\\D", "");
        return Integer.parseInt(digits);
    }
    
    @Override
    public void deleteReplacedImages(Map oldDetails, Map newDetails) {

        // We extract only the details of image inputs for which a new
        // image path has been provided.
        Map imagesForDelete = new HashMap(){
            @Override
            public Object put(Object key, Object value) {
                if(key == null || value == null) {
                    throw new NullPointerException();
                }
                return super.put(key, value); 
            }
        };

        int max = getImageLimit();
        for(int i=0; i<max; i++) {

            String imageName = getImageName(i);

            String oldImagePath = (String)oldDetails.get(imageName);
            boolean hasOldImage = (oldImagePath != null && oldImagePath.length() > 0);

            String newImagePath = (String)newDetails.get(imageName);
            boolean hasNewImage = (newImagePath != null && newImagePath.length() > 0);
//Logger.getLogger(this.getClass().getName()).fine("Old image path: "+oldImagePath+",  New image path: "+newImagePath);

            if(hasOldImage && hasNewImage) {
                imagesForDelete.put(imageName, oldImagePath);
            }
        }

        Iterator<String> forDelete = imagesForDelete.values().iterator();
        while(forDelete.hasNext()) {
            deleteFile(forDelete.next());
        }
    }

    @Override
    public void deleteImages(Map parameters) {

        // Delete local images and icons
        Map<String, String> images = getImagePaths(parameters, true, false, true);
        
        Iterator<String> iter = images.values().iterator();

        while(iter.hasNext()) {
            deleteFile(iter.next());
        }
    }
    
    @Override
    public String saveToSuggestedSize(InputStream in, Dimension size, String filename, String signature) throws IOException {
        
        BufferedImage buffImage = this.read(in, Util.getExtension(filename, null));

        if(signature != null) {
            this.drawString(buffImage, signature);
        }

        Dimension suggested = ImageDimensions.getSuggestedDimension(buffImage, size);
        
        int width = suggested.width;
        int height = suggested.height;

        String saveTo = this.getPathForFilename(filename);
        
        boolean saved = this.saveImage(buffImage, width, height, saveTo);
        
        return saved ? saveTo : null;
    }
    
    @Override
    public boolean saveImage(Image img, int width, int height, String destPath) throws IOException {

        BufferedImage scaledImg = scaleImage(img, width, height, BufferedImage.TYPE_INT_RGB);

        int start = destPath.lastIndexOf('.') + 1;
        String extension = destPath.substring(start).toLowerCase();

        File file = new File(destPath);
        
        File dir = file.getParentFile();
        
        if(!dir.exists()) {

Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Making dirs: {0}", dir);
            
            dir.mkdirs();
        }

        // Save the scaled version out to the file
        //
        boolean saved = javax.imageio.ImageIO.write(scaledImg, extension, file);
        
if(Logger.getLogger(this.getClass().getName()).isLoggable(Level.FINE)) {       
    Logger.getLogger(this.getClass().getName()).log(Level.FINE, 
    "Image saved: {0}, to file: {1}", new Object[]{saved, file});
}

        return saved;
    }
    
    public String getDescripion(File input) {
        return FileSystemView.getFileSystemView().getSystemTypeDescription(input);
    }
    
    @Override
    public String getMimeType(File input) {
        return new MimetypesFileTypeMap().getContentType(input);
    }
    
    @Override
    public BufferedImage scaleImage(Image image, int width, int height, int type) {
        return this.imageRescaler.scaleImage(image, width, height, type);
    }

//    public Image scaleImage(Image sourceImage, int width, int height) {

//        ImageFilter   filter   = new ReplicateScaleFilter(width,height);
//        ImageProducer producer = new FilteredImageSource(sourceImage.getSource(),filter);

//        return Toolkit.getDefaultToolkit().createImage(producer);
//    }

    public Map<String, String> getImagePaths(
            Map<String, String> parameters, boolean local, boolean remote, boolean icon) {

        HashMap<String, String> images = new HashMap<>();
        
        int max = getImageLimit();
        
        for(int i=0; i<max; i++) {

            String imageName = getImageName(i);

            String path = (String)parameters.get(imageName);

            if(path == null || path.length() == 0) {
                continue;
            }
            
            boolean add = (local && !isHttpUrl(path)) || 
                    (remote && isHttpUrl(path));
            
            if(add) {
                
                if(path.startsWith("/")) {
                    //@related_images
                    path = this.getPathForFilename(path); 
                }
                
                images.put(imageName, path);
            }

            if(icon) {
                
                //@related_images
                // Delete any icons associated with this image
                String iconPath = getIconPathForImage(path);

                if(iconPath != null && new File(iconPath).exists()) {
                    images.put(getIconName(i), iconPath);
                }
            }
        }

        return Collections.unmodifiableMap(images);
    }

    private boolean isHttpUrl(String link) {
        return link.toLowerCase().trim().startsWith("http:");
    }
    
    /**
     * @param pathStr The path to the file to be deleted
     * @return true if the file was deleted, false if the file was not deleted 
     * or does not exist
     */
    private boolean deleteFile(String pathStr) throws NullPointerException {
        
        if(pathStr == null) throw new NullPointerException();
        
        try{
            Path path = Paths.get(pathStr);
            if(Files.exists(path, (LinkOption)null)) {
                Files.delete(path);
                return true;
            }else{
                return false;
            }
        }catch(Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Failed to delete file", e);
            return false;
        }
    }
}//~END
