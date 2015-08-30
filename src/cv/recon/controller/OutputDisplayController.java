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
package cv.recon.controller;

import cv.recon.util.MatFXUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;

/**
 * FXML Controller class
 *
 * @author Burhanuddin
 */
public class OutputDisplayController implements Initializable {

    @FXML
    private ImageView outputView;
    @FXML
    private Label nonZeroLabel;
    
    private BackgroundSubtractorMOG2 bsmog;
    private Mat fgMask;
    private Mat kernel;
    private Mat output;
    private WritableImage writableImage;
    private int nonZeroCount;
    private boolean isFirstFrame;
    
    /**
     * Update output view after image processing.
     * @param src Original Mat
     */
    public void updateView(Mat src) {
        if (!src.empty()) {
            processImage(src);
        }

        if (!output.empty()) {
            if (writableImage == null) {
                writableImage = MatFXUtils.toFXImage(output, null);
            } else {
                MatFXUtils.toFXImage(output, writableImage);
            }
            outputView.setImage(writableImage);
        }
    }
    
    /**
     * Image processing.
     * @param src Source Mat
     */
    private void processImage(Mat src) {
        subtractBackground(src);
    }
    
    /**
     * Subtract background using BackgroundSubtractorMOG2
     * @param src Source Mat
     */
    private void subtractBackground(Mat src) {
        if (bsmog != null) {
            bsmog.apply(src, fgMask);
            
            Imgproc.erode(fgMask, fgMask, kernel);
            Imgproc.dilate(fgMask, fgMask, kernel);
            
            output.setTo(new Scalar(0));
            src.copyTo(output, fgMask);
            
            if (isFirstFrame) {
                nonZeroCount = 0;
                isFirstFrame = false;
            } else {
                nonZeroCount = Core.countNonZero(fgMask);
            }
            nonZeroLabel.setText("" + nonZeroCount);
        }
    }
    
    /**
     * Combine overlapping rectangles.
     * @param rectangles A list of rectangles
     */
    private void combineOverlappingRectangles(List<Rect> rectangles) {
        boolean stillOverlap;
        
        do{
            stillOverlap = false;
            
            for (int i = 0; i < rectangles.size(); i++) {
                Rect rectA = rectangles.get(i);
                
                for (int j = 0; j < rectangles.size(); j++) {
                    Rect rectAClone = rectA.clone();
                    Rect rectB = rectangles.get(j);
                    
                    if (rectA.equals(rectB)) {
                        continue;
                    }
                    
                    if (isOverlap(rectA, rectB)) {
                        rectA.x = Math.min(rectA.x, rectB.x);
                        rectA.y = Math.min(rectA.y, rectB.y);
                        rectA.width = Math.max(rectAClone.x + rectAClone.width,
                                rectB.x + rectB.width);
                        rectA.width -= rectA.x;
                        rectA.height = Math.max(rectAClone.y + rectAClone.height,
                                rectB.y + rectB.height);
                        rectA.height -= rectA.y;
                        
                        rectangles.remove(rectB);
                        
                        stillOverlap = true;
                    }
                }
            }
        } while (stillOverlap);
    }
    
    /**
     * Check if two rectangles overlap.
     * @param a First rectangle
     * @param b Second rectangle
     * @return A boolean stating whether two rectangles overlap.
     */
    private boolean isOverlap(Rect a, Rect b) {
        return a.x <= (b.x + b.width) &&
               (a.x + a.width) >= b.x &&
               a.y <= (b.y + b.height) &&
               (a.y + a.height) >= b.y;
    }
    
    /**
     * Start background subtraction using first frame as background frame.
     */
    public void startBackgroundSubtraction() {
        bsmog = new BackgroundSubtractorMOG2(100, 75f, false);
        isFirstFrame = true;
    }
    
    /**
     * Return non-zero pixel count of foreground mask.
     * @return Non-zero pixel count
     */
    public int getNonZeroPixelCount() {
        return nonZeroCount;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fgMask = new Mat();
        output = new Mat();
        
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10));
    }    
    
}
