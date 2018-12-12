package Controllers;

import Controllers.WindowLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class Main extends Application {
        /*
            $$\      $$\  $$$$$$\   $$$$$$\  $$$$$$$\  $$$$$$\
            $$$\    $$$ |$$  __$$\ $$  __$$\ $$  __$$\ \_$$  _|
            $$$$\  $$$$ |$$ /  $$ |$$ /  \__|$$ |  $$ |  $$ |
            $$\$$\$$ $$ |$$$$$$$$ |\$$$$$$\  $$$$$$$  |  $$ |
            $$ \$$$  $$ |$$  __$$ | \____$$\ $$  __$$<   $$ |
            $$ |\$  /$$ |$$ |  $$ |$$\   $$ |$$ |  $$ |  $$ |
            $$ | \_/ $$ |$$ |  $$ |\$$$$$$  |$$ |  $$ |$$$$$$\
            \__|     \__|\__|  \__| \______/ \__|  \__|\______|
          */

    @Override
    public void start(Stage primaryStage) throws Exception {

        WindowLoader.loadStartWindow();


    }
}
