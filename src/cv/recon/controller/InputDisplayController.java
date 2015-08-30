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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.opencv.core.Mat;

/**
 * FXML Controller class
 *
 * @author Burhanuddin
 */
public class InputDisplayController implements Initializable {

    @FXML
    private ImageView inputView;
    
    private WritableImage writableImage;
    
    /**
     * Update input view without image processing.
     * @param src Original Mat
     */
    public void updateView(Mat src) {
        if (writableImage == null) {
            writableImage = MatFXUtils.toFXImage(src, null);
        } else {
            MatFXUtils.toFXImage(src, writableImage);
        }
        inputView.setImage(writableImage);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
