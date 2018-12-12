package Controllers;

import Auto.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashSet;
import java.util.Properties;

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
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            WindowLoader.loadMainWindowEmpty(f);
        }
    }

    @FXML
    void loadAuto(ActionEvent event) {


        try{
            String path = WindowLoader.showFileChooserDialog((Stage)((Node)(event.getSource())).getScene().getWindow());
            WindowLoader.loadMainWindowWithPath(path);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }

        catch (IOException e) {
            //do nothing
        }

        catch (NullPointerException e){
            //do nothing
        }

    }
}

