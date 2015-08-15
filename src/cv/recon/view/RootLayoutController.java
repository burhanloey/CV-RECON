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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

/**
 *
 * @author Burhanuddin
 */
public class RootLayoutController implements Initializable {
    
    @FXML
    private HBox hBox;
    
    public MainApp mainApp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDisplay();
    }
    
    private void initDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InputDisplay.fxml"));
            Parent inputDisplay = loader.load();
            
            hBox.getChildren().setAll(inputDisplay);
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
}
