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

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import cv.recon.util.Math;

/**
 * FXML Controller class
 *
 * @author Burhanuddin
 */
public class ChartController implements Initializable {

    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private LineChart<Number, Number> lineChart;
    
    OutputDisplayController outputController;
    ObservableList<XYChart.Data<Number, Number>> nonZeroCountValues;
    XYChart.Series<Number, Number> nonZeroCountSeries;
    ObservableList<XYChart.Data<Number, Number>> meanValues;
    XYChart.Series<Number, Number> meanSeries;
    long startTime;
    long elapsedTime;
    Timer timer;
    
    /**
     * Start timer to run a new thread that updates chart.
     */
    public void startTimer() {
        if (timer == null) {
            nonZeroCountValues.clear();
            startTime = System.nanoTime();

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    elapsedTime = System.nanoTime() - startTime;
                    elapsedTime = elapsedTime / 1000000;

                    Platform.runLater(() -> {
                        addNonZeroCount(outputController.getNonZeroPixelCount());
                    });
                }
            }, 0, 100);
        }
    }
    
    /**
     * Stop timer
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    
    /**
     * Add non-zero pixel count to chart.
     * @param nonZeroCount Non-zero pixel count
     */
    private void addNonZeroCount(int nonZeroCount) {
        nonZeroCountValues.add(new XYChart.Data<>(elapsedTime, nonZeroCount));
        
        clearOldData();
        calculateMean();
    }
    
    /**
     * Remove earliest data to limit the graph to only display a maximum
     * amount of data
     */
    private void clearOldData() {
        if (nonZeroCountValues.size() > 50) {
            nonZeroCountValues.remove(0);
        }
    }
    
    /**
     * Calculate mean value and update the chart
     */
    private void calculateMean() {
        if (!nonZeroCountValues.isEmpty()) {
            double mean = Math.mean(nonZeroCountValues);
            Number start = nonZeroCountValues.get(0).getXValue();

            if (meanValues.isEmpty()) {
                meanValues.add(new XYChart.Data<>(start, mean));
            } else {
                Number end = nonZeroCountValues.get(nonZeroCountValues.size() - 1).getXValue();
                meanValues.setAll(new XYChart.Data<>(start, mean), new XYChart.Data<>(end, mean));
            }
        }
    }
    
    /**
     * Called from root layout to make reference to output display controller.
     * @param outputController 
     */
    public void setOutputController(OutputDisplayController outputController) {
        this.outputController = outputController;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nonZeroCountValues = FXCollections.observableArrayList();
        nonZeroCountSeries = new XYChart.Series<>("Start point", nonZeroCountValues);
        lineChart.getData().add(nonZeroCountSeries);
        
        meanValues = FXCollections.observableArrayList();
        meanSeries = new XYChart.Series<>("Mean", meanValues);
        lineChart.getData().add(meanSeries);
        
        xAxis.setForceZeroInRange(false);
    }    
    
}
