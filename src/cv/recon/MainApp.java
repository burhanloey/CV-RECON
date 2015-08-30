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
package cv.recon;

import cv.recon.controller.RootLayoutController;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

/**
 *
 * @author Burhanuddin
 */
public class MainApp extends Application {
    
    public static final String TITLE = "CV-RECON";
    public static final String VIEW_DIR = "/cv/recon/view/";
    
    public Stage primaryStage;
    private final boolean debug = true;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        loadOpenCV();
        initRootLayout();
    }
    
    /**
     * Load OpenCV library for JNI.
     * <p/>
     * For debug build, the path to DLL file is in lib folder in class path.
     * For release build, the path is in lib folder relative to the JAR file.
     */
    private void loadOpenCV() {
        try {
            if (!debug) {
                String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                String dir = path.substring("/".length(), path.length() - MainApp.TITLE.length() - ".jar".length());
                System.setProperty("java.library.path", dir + "/lib");

                final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
                sysPathsField.setAccessible(true);
                sysPathsField.set(null, null);
            }
            
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Initialize root layout.
     * <p/>
     * Override on close request to clear resources stream from OpenCV function
     * before exiting.
     */
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_DIR + "RootLayout.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setTitle(TITLE);
            primaryStage.setScene(scene);
            
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            
            primaryStage.setOnCloseRequest((WindowEvent event) -> {
                controller.dispose();
                primaryStage.close();
            });

            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
