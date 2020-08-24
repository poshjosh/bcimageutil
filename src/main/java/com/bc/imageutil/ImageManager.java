/*
 * Copyright 2017 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bc.imageutil;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.MathContext;
import java.util.Map;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 4, 2017 8:11:14 PM
 * @deprecated 
 */
@Deprecated
public interface ImageManager extends ImageRescaler, com.bc.imageutil.ImageReader, ImageOverlay{
    
    static MathContext getDefaultMathContext() {
        return MathUtil.getDefaultContext();
    }
    
    default void drawString(BufferedImage image, String stringToDraw) {
        this.drawString(image, stringToDraw, DrawConfigs.centre());
    }

    String buildRelativePath(String filename);

    void deleteImages(Map parameters);

    void deleteReplacedImages(Map oldDetails, Map newDetails);

    String getMimeType(File input);

    String getPath(String relativePath);

    String getPathForFilename(String filename);

    Dimension getSuggestedDimension(BufferedImage buffImage, Dimension size);

    /**
     * @return Text of form '/yyyy/mm' e.g '/2015/07'
     */
    String getYearMonthPathPrefix();

    boolean saveImage(Image img, int width, int height, String destPath) throws IOException;

    String saveToSuggestedSize(InputStream in, Dimension size, String filename, String signature) throws IOException;
}
