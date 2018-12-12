package Controllers;

import Auto.FiniteStateTransducer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Properties;

/**
 * class that has general functions that can be used to communicate in between stages
 */
public class WindowLoader {
    private static Stage mainStage = null;
    private static Stage startStage = null;

    public static void loadMainWindowEmpty(FiniteStateTransducer fst) throws IOException {
        if(mainStage!=null){
            mainStage.close();
        }
        mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(new File("Resources/View/main_pane.fxml").toURL());
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        mainStage.setTitle("Theory of Automata");
        mainStage.setScene(scene);

        mainStage.show();
        ((MainController) loader.getController()).setFST(fst);
    }
    public static void loadMainWindowWithPath(String path) throws IOException {
        if(mainStage!=null){
            mainStage.close();
        }
        mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(new File("Resources/View/main_pane.fxml").toURL());
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        mainStage.setTitle("Theory of Automata");
        mainStage.setScene(scene);
        mainStage.show();
        try {
            ((MainController) loader.getController()).load(path);
        }catch (StreamCorruptedException e){
            mainStage.close();
            throw e;
        }
    }
    public static void loadStartWindow() throws IOException {
        if(startStage!=null){
            startStage.close();
        }
        startStage = new Stage();
        FXMLLoader loader = new FXMLLoader(new File("Resources\\View\\start_pane.fxml").toURL());
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 450);
        startStage.setTitle("Theory of Automata");
        startStage.getIcons().add(new Image(new FileInputStream(new File("Resources/images/logo.png"))));
        startStage.setScene(scene);
        startStage.show();
    }
    public static String showFileChooserDialog(Stage stage) throws IOException {
        Properties prop = new Properties();
            prop.load(Main.class.getClassLoader().getResourceAsStream("config/config.xml"));
        FileChooser chooser = new FileChooser();
        String directory = prop.getProperty("last_open_directory");
        chooser.setInitialDirectory(new File("C:\\Users\\jit\\Desktop\\examples"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FST","*.fst"));
        return chooser.showOpenDialog(stage).getAbsolutePath();

    }
}
