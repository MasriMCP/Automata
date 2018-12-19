package Controllers;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.FileOutputStream;
import java.util.Properties;

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
        Properties prop = new Properties();
        prop.loadFromXML(getClass().getClassLoader().getResourceAsStream("config/config.xml"));
        if(prop.getProperty("lod")==null){
            prop.setProperty("lod",System.getProperty("user.home"));
        }
        prop.storeToXML(new FileOutputStream("resources/config/config.xml"),null);
        WindowLoader.loadStartWindow();
    }
}
