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

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Burhanuddin
 */
public class CannyDetector {
    int edgeThresh;
    int lowThreshold;
    final int max_lowThreshold;
    int ratio;
    int kernel_size;
    
    /**
     * Construct Canny detector with default values.
     */
    public CannyDetector() {
        this(1, 50, 100, 3, 3);
    }
    
    /**
     * Construct Canny detector with specified low threshold.
     * @param lowThreshold Low threshold value
     */
    public CannyDetector(int lowThreshold) {
        this(1, lowThreshold, 100, 3, 3);
    }
    
    /**
     * Construct Canny detector with specified values.
     * @param edgeThresh Edge threshold value. Default value is 1.
     * @param lowThreshold Low threshold value. Default value is 50.
     * @param max_lowThreshold Max low threshold. Default value is 100.
     * @param ratio Ratio value. Default value is 3.
     * @param kernel_size Kernel size. Default value is 3.
     */
    public CannyDetector (int edgeThresh, int lowThreshold, int max_lowThreshold, int ratio, int kernel_size) {
        this.edgeThresh = edgeThresh;
        this.lowThreshold = lowThreshold;
        this.max_lowThreshold = max_lowThreshold;
        this.ratio = ratio;
        this.kernel_size = kernel_size;
    }
    
    /**
     * Set low threshold value to Canny detector.
     * @param lowThreshold Low threshold value
     */
    public void setLowThreshold(int lowThreshold) {
        if (lowThreshold >= 0 && lowThreshold <= max_lowThreshold) {
            this.lowThreshold = lowThreshold;
        }
    }
    
    /**
     * Apply Canny detector to Mat.
     * @param src Input Mat
     * @param dst Output Mat
     */
    public void detect(Mat src, Mat dst) {
        Mat src_gray = new Mat();
        Mat detected_edges = new Mat();
        
        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(src_gray, detected_edges, new Size(3, 3));
        Imgproc.Canny(detected_edges, 
                detected_edges, 
                lowThreshold, 
                lowThreshold * ratio, 
                kernel_size, 
                true);
        
        dst.setTo(new Scalar(0));
        src.copyTo(dst, detected_edges);
    }
}
