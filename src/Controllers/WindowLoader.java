package Controllers;

import Auto.FiniteStateTransducer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
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
        FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource("View/main_pane.fxml"));
        Parent root = loader.load();
        mainStage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("images/logo.png")));

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
        FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource("View/main_pane.fxml"));
        Parent root = loader.load();
        mainStage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("images/logo.png")));

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
        FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource("View/start_pane.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 450);
        startStage.setTitle("Theory of Automata");
        startStage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("images/logo.png")));
        startStage.setScene(scene);
        startStage.show();
    }
    public static String showFileChooserDialog(Stage stage) throws IOException {
        Properties prop = new Properties();
        prop.loadFromXML(Main.class.getClassLoader().getResourceAsStream("config/config.xml"));

        FileChooser chooser = new FileChooser();
        String directory = (String)prop.getOrDefault("lod",System.getProperty("user.home"));
        chooser.setInitialDirectory(new File(Paths.get(directory).getParent().toString()));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FST","*.fst"));
        String path = chooser.showOpenDialog(stage).getAbsolutePath();
        prop.setProperty("lod", path);

        new Thread(()->{
            try {
                prop.storeToXML(new FileOutputStream("resources/config/config.xml"),null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return path;

    }
}
