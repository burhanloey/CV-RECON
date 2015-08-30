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
    @FXML
    private HBox chartBox;
    
    public MainApp mainApp;
    private InputDisplayController inputController;
    private OutputDisplayController outputController;
    private ChartController chartController;
    
    private VideoCapture vid;
    private Timer timer;
    private Mat src;
    
    /**
     * Called from Start button.
     * <p/>
     * Start OpenCV video capture and update the display for input and output.
     * @param event 
     */
    @FXML
    private void start(ActionEvent event) {
        if (!vid.isOpened()) {
            vid.open(0);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    vid.read(src);

                    if (!src.empty()) {
                        Platform.runLater(() -> {
                            inputController.updateView(src);
                        });
                        Platform.runLater(() -> {
                            outputController.updateView(src);
                        });
                    }
                }
            }, 0L, 100L);
        }
    }
    
    /**
     * Called from 'Capture' button.
     * @param event 
     */
    @FXML
    private void capture(ActionEvent event) {
        chartController.startTimer();
        outputController.startBackgroundSubtraction();
    }
    
    /**
     * Called from Stop button.
     * @param event 
     */
    @FXML
    private void stop(ActionEvent event) {
        dispose();
    }
    
    /**
     * Stop updating input and output display. Close OpenCV video stream
     * resources.
     */
    public void dispose() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (vid.isOpened()) {
            vid.release();
        }
        chartController.stopTimer();
    }
    
    /**
     * Initialize input display.
     */
    private void initInputDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MainApp.VIEW_DIR + "InputDisplay.fxml"));
            Parent inputDisplay = loader.load();
            
            inputController = loader.getController();
            
            inputBox.getChildren().setAll(inputDisplay);
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Initialize output display.
     */
    private void initOutputDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MainApp.VIEW_DIR + "OutputDisplay.fxml"));
            Parent outputDisplay = loader.load();
            
            outputController = loader.getController();
            
            outputBox.getChildren().setAll(outputDisplay);
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Initialize chart.
     */
    private void initChart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MainApp.VIEW_DIR + "Chart.fxml"));
            Parent chart = loader.load();
            
            chartController = loader.getController();
            chartController.setOutputController(outputController);
            
            chartBox.getChildren().setAll(chart);
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vid = new VideoCapture();
        src = new Mat();
        
        initInputDisplay();
        initOutputDisplay();
        initChart();
    }
    
    /**
     * Called from MainApp to make reference back to itself.
     * @param mainApp 
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
}
