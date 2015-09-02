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

import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Burhanuddin
 */
public class Math {
    /**
     * Calculate mean value for a series in a chart
     * 
     * @param list A list of values in a series
     * @return Mean value
     */
    public static double mean(ObservableList<XYChart.Data<Number, Number>> list) {
        double sum = 0;
        
        sum = list.stream()
                .map((data) -> data.getYValue().doubleValue())
                .reduce(sum, (accumulator, _item) -> accumulator + _item);
        
        return sum / list.size();
    }
    
    /**
     * Calculate mid range value for a given series
     * 
     * @param list A list of data in a series
     * @return Mid range value
     */
    public static double midRange(ObservableList<XYChart.Data<Number, Number>> list) {
        if (list == null || list.isEmpty()) {
            return 0.0;
        }
        
        double min, max;
        min = max = list.get(0).getYValue().doubleValue();
        
        Iterator<XYChart.Data<Number, Number>> iterator = list.iterator();
        iterator.next();    // skip first data
        
        while (iterator.hasNext()) {
            XYChart.Data<Number, Number> data = iterator.next();
            double value = data.getYValue().doubleValue();
            
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        
        return (max + min) / 2;
    }
}
