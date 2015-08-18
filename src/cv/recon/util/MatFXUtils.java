/*
 * Copyright (C) 2015 Burhanuddin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cv.recon.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.opencv.core.Mat;

/**
 *
 * @author Burhanuddin
 */
public class MatFXUtils {
    
    /**
     * Convert from OpenCV Mat to JavaFX WritableImage to be displayed in
     * ImageView.
     * @param mat Mat to be converted
     * @param writableImage Optional WritableImage, if non-null, the Mat will be
     * written in this WritableImage
     * @return A WritableImage to be used for JavaFX, return null if already
     * supplied with WritableImage
     */
    public static WritableImage toFXImage(Mat mat, WritableImage writableImage) {
        int width = mat.width();
        int height = mat.height();
        int channels = mat.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        mat.get(0, 0, sourcePixels);
        
        BufferedImage bufferedImage;
        if (mat.channels() > 1) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        
        final byte[] targetPixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        
        if (writableImage == null) {
            WritableImage outputImage = SwingFXUtils.toFXImage(bufferedImage, null);
            return outputImage;
        } else {
            SwingFXUtils.toFXImage(bufferedImage, writableImage);
            return null;
        }
    }
}
