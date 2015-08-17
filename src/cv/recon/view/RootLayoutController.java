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

import cv.recon.MainApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

/**
 *
 * @author Burhanuddin
 */
public class RootLayoutController implements Initializable {
    
    @FXML
    private HBox inputBox;
    @FXML
    private HBox outputBox;
    
    public MainApp mainApp;
    private InputDisplayController inputController;
    private OutputDisplayController outputController;
    
    VideoCapture vid;
    Timer timer;
    Mat mat;
    
    @FXML
    private void start(ActionEvent event) {
        if (!vid.isOpened()) {
            vid.open(0);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    vid.read(mat);

                    Platform.runLater(() -> {
                        inputController.updateView(mat);
                    });
                    Platform.runLater(() -> {
                        outputController.updateView(mat);
                    });
                }
            }, 0L, 100L);
        }
    }
    
    @FXML
    private void stop(ActionEvent event) {
        dispose();
    }
    
    public void dispose() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (vid.isOpened()) {
            vid.release();
        }
    }
    
    private void initInputDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InputDisplay.fxml"));
            Parent inputDisplay = loader.load();
            
            inputController = loader.getController();
            
            inputBox.getChildren().setAll(inputDisplay);
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initOutputDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OutputDisplay.fxml"));
            Parent outputDisplay = loader.load();
            
            outputController = loader.getController();
            
            outputBox.getChildren().setAll(outputDisplay);
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vid = new VideoCapture();
        mat = new Mat();
        
        initInputDisplay();
        initOutputDisplay();
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
}
