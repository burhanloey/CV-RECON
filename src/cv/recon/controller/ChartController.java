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
import cv.recon.util.Position;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Burhanuddin
 */
public class ChartController implements Initializable {

    @FXML
    private Label countLabel;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private LineChart<Number, Number> lineChart;
    
    private OutputDisplayController outputController;
    private ObservableList<XYChart.Data<Number, Number>> nonZeroCountValues;
    private XYChart.Series<Number, Number> nonZeroCountSeries;
    private ObservableList<XYChart.Data<Number, Number>> midRangeValues;
    private XYChart.Series<Number, Number> midRangeSeries;
    private long startTime;
    private long elapsedTime;
    private long count;
    private Timer timer;
    private Position currentPosition;
    
    /**
     * Start timer to run a new thread that updates chart.
     */
    public void startTimer() {
        if (timer == null) {
            count = 0;
            currentPosition = Position.CLOSE_TO_INITIAL;
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
     * 
     * @param nonZeroCount Non-zero pixel count
     */
    private void addNonZeroCount(int nonZeroCount) {
        nonZeroCountValues.add(new XYChart.Data<>(elapsedTime, nonZeroCount));
        
        clearOldData();
        calculateMidRange();
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
     * Calculate mid range value and update the chart
     */
    private void calculateMidRange() {
        if (!nonZeroCountValues.isEmpty()) {
            double midRange = Math.midRange(nonZeroCountValues);
            
            checkRepetition(midRange);
            setMidRangeOnChart(midRange);
        }
    }
    
    /**
     * Check if there is a change in position according to mid range value. If 
     * yes, then it is considered a repetition. The counter increased when the
     * position changed from AWAY_FROM_INITIAL to CLOSE_TO_INITIAL.
     * 
     * @param midRange Mid range value
     */
    private void checkRepetition(double midRange) {
        double currentValue = nonZeroCountValues
                                .get(nonZeroCountValues.size() - 1) // last position
                                .getYValue()
                                .doubleValue();
        
        Position newPos;
        if (currentValue > midRange) {
            newPos = Position.AWAY_FROM_INITIAL;
        } else {
            newPos = Position.CLOSE_TO_INITIAL;
        }
        
        if (!currentPosition.equals(newPos)) {
            currentPosition = newPos;
            
            if (currentPosition.equals(Position.CLOSE_TO_INITIAL)) {
                count++;
                countLabel.setText("" + count);
            }
        }
    }
    
    /**
     * Add mid range series on non-zero pixel count chart.
     * 
     * @param midRange Mid range value
     */
    private void setMidRangeOnChart(double midRange) {
        Number start = nonZeroCountValues.get(0).getXValue();
            
        if (midRangeValues.isEmpty()) {
            midRangeValues.add(new XYChart.Data<>(start, midRange));
        } else {
            Number end = nonZeroCountValues.get(nonZeroCountValues.size() - 1).getXValue();
            midRangeValues.setAll(new XYChart.Data<>(start, midRange), new XYChart.Data<>(end, midRange));
        }
    }
    
    /**
     * Called from root layout to make reference to output display controller.
     * 
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
        
        midRangeValues = FXCollections.observableArrayList();
        midRangeSeries = new XYChart.Series<>("Mid range", midRangeValues);
        lineChart.getData().add(midRangeSeries);
        
        xAxis.setForceZeroInRange(false);
    }    
    
}
