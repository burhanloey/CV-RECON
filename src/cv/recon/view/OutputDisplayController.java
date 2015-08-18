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
package cv.recon.view;

import cv.recon.util.MatFXUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * FXML Controller class
 *
 * @author Burhanuddin
 */
public class OutputDisplayController implements Initializable {

    @FXML
    private ImageView outputView;
    
    Mat dst;
    WritableImage writableImage;
    
    /**
     * Update output view after image processing.
     * @param mat Original Mat
     */
    public void updateView(Mat mat) {
        processImage(mat);
        
        if (writableImage == null) {
            writableImage = MatFXUtils.toFXImage(dst, null);
        } else {
            MatFXUtils.toFXImage(dst, writableImage);
        }
        outputView.setImage(writableImage);
    }
    
    /**
     * Image processing.
     * @param src Source Mat
     */
    private void processImage(Mat src) {
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dst = new Mat();
    }    
    
}
