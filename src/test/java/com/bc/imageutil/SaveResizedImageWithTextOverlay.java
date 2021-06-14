package com.bc.imageutil;

import com.bc.imageutil.impl.ImageReaderImpl;
import com.bc.imageutil.impl.ImageRescalerImpl;
import com.bc.imageutil.impl.OverlayImageWithText;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author chinomso ikwuagwu
 */
public class SaveResizedImageWithTextOverlay{
    
    private static final Logger LOG = Logger.getLogger(SaveResizedImageWithTextOverlay.class.getName());
    
    public static void main(String... args) {
        
        final String imageName = "menu-icon-light";
        final String imageExtension = "png";
        
        SaveResizedImageWithTextOverlay demo = new SaveResizedImageWithTextOverlay();
        
        Path sourcePath = Paths.get(System.getProperty("user.home"), "Desktop", 
                "images", imageName + '.' + imageExtension);
        
        Path targetPath = sourcePath.getParent()
                .resolve(Paths.get(imageName + "-formatted." + imageExtension));
        
        try(InputStream from = Files.newInputStream(sourcePath)) {
            
            demo.saveImage(from, targetPath);
            
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveImage(InputStream source, Path target) throws IOException {
        
        final Path filename = target.getFileName();
        
        final String fileExtension = Util.getExtension(filename.toString(), null);
        Objects.requireNonNull(fileExtension);
        
        LOG.fine(() -> MessageFormat.format("File, extension: {0}, name: {1}", fileExtension, filename));

        // Read the image
        //
        final ImageReader imageReader = new ImageReaderImpl();
        final BufferedImage image = imageReader.read(source, fileExtension);
        LOG.log(Level.FINE, "Read image: {0}", image);

        // Re-size the image
        //
        final Dimension preferredSize = new Dimension(512, 512);
        final ImageRescaler imageRescaler = new ImageRescalerImpl();        
        final Dimension targetSize = ImageDimensions
                .getSuggestedDimension(image, preferredSize);
        final int width = targetSize.width;
        final int height = targetSize.height;
        final BufferedImage resizedImage = imageRescaler
                .scaleImage(image, width, height, BufferedImage.TYPE_INT_RGB);

        // Overlay the image with text
        //
        final String signature = "MyApp.com";
        final ImageOverlay imageOverlay = new OverlayImageWithText();
        final DrawConfig drawConfig = DrawConfigs
                .fromBottomRight(0, 0.1f, Font.decode("MONOSPACE-PLAIN-24"), Color.GRAY);
        imageOverlay.drawString(resizedImage, signature, drawConfig);

        // Write the image to the targe path
        //
        ImageIO.write(resizedImage, fileExtension, target.toFile());
    }
}
