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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

/**
 * FXML Controller class
 *
 * @author Burhanuddin
 */
public class InputDisplayController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private ImageView inputView;
    
    VideoCapture vid;
    
    @FXML
    private void startCamera(ActionEvent event) {
        Mat mat = new Mat();
        vid.open(0);
        vid.read(mat);
        
        int width = mat.width();
        int height = mat.height();
        int channels = mat.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        mat.get(0, 0, sourcePixels);
        
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        
        WritableImage wim = SwingFXUtils.toFXImage(bufferedImage, null);
        inputView.setImage(wim);
        
        vid.release();
    }
    
    @FXML
    private void stopCamera(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vid = new VideoCapture();
    }    
    
}
