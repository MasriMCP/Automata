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

        FXMLLoader loader = new FXMLLoader(new File("Resources\\View\\start_pane.fxml").toURL());
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setTitle("Theory of Automata");
        primaryStage.getIcons().add(new Image(new FileInputStream(new File("Resources/images/logo.png"))));
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
