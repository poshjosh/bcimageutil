package com.bc.imageutil.retiring;

import com.bc.imageutil.ImageManager;
import com.bc.imageutil.impl.ImageManagerImpl;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @(#)RescaleImages.java   26-May-2015 23:57:35
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public class ImageRescaleManager implements Serializable {
    
    private boolean websafeFilenames;
    
    private boolean renameImagesSerially;
    
    private boolean addUniqueIdentifierToFilenames;
    
    private int rename_index;
    
    private String [] acceptedImageTypes;
    
    public ImageRescaleManager() {
        acceptedImageTypes = new String[]{"jpg", "jpeg", "png", "gif"};
    }
    
    public void rescale(
            final File srcDir, final File tgtDir, 
            Dimension targetDimension, String signature) 
            throws FileNotFoundException, IOException {
        
        rename_index = 1;

        final List<String> list = this.acceptedImageTypes == null ? null : Arrays.asList(acceptedImageTypes);

final Logger logger = Logger.getLogger(this.getClass().getName());

        File [] imageFiles = srcDir.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                boolean accept;
                name = name.toLowerCase();
                int n = name.lastIndexOf('.');
                if(n == -1) {
                    accept = false;
                }else{
                    String extension = name.substring(n+1, name.length());
                    accept = list.contains(extension.toLowerCase());
                }
if(logger.isLoggable(Level.FINE)) {
    logger.log(Level.FINE, "Accepted: {0}, file: {1} of directory: {2}", new Object[]{accept, name, dir});
}
                return accept;
            }
        });

        if(!tgtDir.exists()) {
            tgtDir.mkdirs();
        }

        final ImageManager imageManager = new ImageManagerImpl(tgtDir.toString());

logger.log(Level.INFO, "Selected image files: {0}", imageFiles==null?null:imageFiles.length);
        
        int saved = 0;
        
        for(File imageFile:imageFiles) {

            try(InputStream in = new FileInputStream(imageFile)) {

                String tgtFname = this.getTargetFilename(imageFile);
                
if(logger.isLoggable(Level.INFO)) {                
    logger.log(Level.INFO, "{0}. saving {1} to {2}", new Object[]{(saved + 1), imageFile, imageManager.getPathForFilename(tgtFname)});
}
                imageManager.saveToSuggestedSize(in, targetDimension, tgtFname, signature);
                
                ++saved;
            }
        }
logger.log(Level.INFO, "Successfully saved {0} images", saved);
    }
    
    public String getTargetFilename(File imageSrc) {
        String output;
        if(this.isRenameImagesSerially()) {
// A number was appended instead of char '.'            
//            output = (rename_index++) + '.' + this.getExtension(imageSrc.getName());
            output = (rename_index++) + "." + this.getExtension(imageSrc.getName());
        }else if(this.isWebsafeFilenames()) {
            output = this.getWebsafeFilename(imageSrc.getName());
        }else{
            output = imageSrc.getName();
        }
        if(this.isAddUniqueIdentifierToFilenames()) {
            int n = output.lastIndexOf('.');
            if(n == -1) {
                throw new AssertionError("Extension part not found for filename: "+output);
            }
            StringBuilder builder = new StringBuilder(output.length() + 15);
            builder.append(output.subSequence(0, n));
            builder.append('_').append(Long.toHexString(System.currentTimeMillis()));
            builder.append(output.subSequence(n, output.length()));
            output = builder.toString();
        }
        return output;
    }
    
    /**
     * Formats a name to allow only letters and digits replacing others with an underscore <tt>'_'</tt>.
     * @param fname The name for which a web safe filename will be constructed
     * @return <tt>mamas_name_Mama.xyz</tt> for input <tt>mama's name: (Mama).xyz</tt>
     */
    protected String getWebsafeFilename(String fname) {
        StringBuilder output = new StringBuilder();
        final int extStart = fname.lastIndexOf('.');
        int underscores = 0;
        for(int i=0; i<fname.length(); i++) {
            char ch = fname.charAt(i);
            if(i >= extStart) {
                output.append(ch);
            }else{    
                boolean letterOrDigit = Character.isLetterOrDigit(ch);
                if(letterOrDigit) {
                    output.append(ch);
                    underscores = 0;
                }else{
                    if(underscores == 0) {
                        output.append('_');
                    }
                    ++underscores;
                }
            }
        }
        return output.toString();
    }

    /**
     * @param path The path whose file name extension will be returned
     * @return The file name extension of the input path. <br/><br/>
     * For a path of <tt>/folder/folder/file.xyz</tt> returns <tt>xyz</tt>
     */
    private String getExtension(String path) {
//        This doesn't work for filenames with ':'. e.g http://
//        String ext = Paths.get(path).getFileName().toString();
        int n = path.lastIndexOf('.', path.length()-1); 
        
        String ext = n == -1 ? null : path.substring(n + 1);
        
        return ext;
    }

    public boolean isRenameImagesSerially() {
        return renameImagesSerially;
    }

    public void setRenameImagesSerially(boolean renameImagesSerially) {
        this.renameImagesSerially = renameImagesSerially;
    }

    public String[] getAcceptedImageTypes() {
        return acceptedImageTypes;
    }

    public void setAcceptedImageTypes(String[] acceptedImageTypes) {
        this.acceptedImageTypes = acceptedImageTypes;
    }

    public boolean isWebsafeFilenames() {
        return websafeFilenames;
    }

    public void setWebsafeFilenames(boolean websafeFilenames) {
        this.websafeFilenames = websafeFilenames;
    }

    public boolean isAddUniqueIdentifierToFilenames() {
        return addUniqueIdentifierToFilenames;
    }

    public void setAddUniqueIdentifierToFilenames(boolean addUniqueIdentifierToFilenames) {
        this.addUniqueIdentifierToFilenames = addUniqueIdentifierToFilenames;
    }
}
