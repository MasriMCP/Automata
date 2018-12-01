package Controllers;

import Auto.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashSet;

public class StartPaneController {

    @FXML
    private TextField name;

    @FXML
    private RadioButton nfa;
    @FXML
    private RadioButton dfa;

    @FXML
    private RadioButton mea;

    @FXML
    private RadioButton mor;

    @FXML
    private TextArea desc;

    @FXML
    void create(Event event) throws IOException {
        FiniteStateTransducer f;
        f = nfa.isSelected() ? new NFA() : dfa.isSelected() ? new DFA() : mea.isSelected() ? new MealyMachine() :
                mor.isSelected() ? new MooreMachine() : null;
        if (f != null) {
            f.setName(name.getText());
            f.addDescription(desc.getText());
            Stage primaryStage = new Stage();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            FXMLLoader loader = new FXMLLoader(new File("Resources/View/main_pane.fxml").toURL());
            Parent root = loader.load();
            ((MainController) loader.getController()).setFST(f);
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Theory of Automata");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    @FXML
    void loadAuto(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FST","fst"));
        try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(chooser.showOpenDialog(
                ((Node)(event.getSource())).getScene().getWindow()
        ))))){
            String line = in.readLine();
            JSONParser parser = new JSONParser();
            JSONObject g = (JSONObject)parser.parse(line);
            FiniteStateTransducer f;
            JSONObject fstJson = (JSONObject)parser.parse((String)g.get("fst"));
            String type = (String)fstJson.get("type");
            f = type.equals("mea")?new MealyMachine():type.equals("mor")?new MooreMachine():
                    type.equals("dfa")?new DFA():new NFA();
            f.setName((String)fstJson.get("name"));
            //TODO: rest of the load
            Stage primaryStage = new Stage();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            FXMLLoader loader = new FXMLLoader(new File("Resources/View/main_pane.fxml").toURL());
            Parent root = loader.load();
            ((MainController) loader.getController()).setFST(f);
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Theory of Automata");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

