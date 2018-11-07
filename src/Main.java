import Auto.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(new File("C:\\Users\\jit\\IdeaProjects\\Automata\\Resources" +
                "\\view\\start_pane.fxml").toURL());
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setTitle("Theory of Automata");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
/*
NFA d = new NFA();
        d.addState("0").addState("1").addState("2");
        d.addInputAlpha('0')
                .addInputAlpha('1');
        d.addTransition("0",'0',"0")
                .addTransition("0",'1',"0")
                .addTransition("0",'1',"1")
                .addTransition("1",'1',"2")
                .addTransition("2",'1',"2")
                .addTransition("2",'0',"2");
        d.setFinalState("2");
        d.setInitialState("0");
        d.addDescription("accepts string with two consecutive ones");
        d.setName("21");
        d.save();
 */
