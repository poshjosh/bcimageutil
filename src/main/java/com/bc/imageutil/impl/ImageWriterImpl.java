package com.bc.imageutil.impl;

import com.bc.imageutil.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author hp
 */
public class ImageWriterImpl implements ImageWriter{

    @Override
    public boolean write(BufferedImage scaledImage, String fileExtension, Path target) throws IOException {
        return javax.imageio.ImageIO.write(scaledImage, fileExtension, target.toFile());
    }
}
