package Controllers;

import Auto.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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
        f = nfa.isSelected()?new NFA():dfa.isSelected()?new DFA():mea.isSelected()?new MealyMachine():
                mor.isSelected()?new MooreMachine():null;
        if(f!=null){
            f.setName(name.getText());
            f.addDescription(desc.getText());
            Stage primaryStage = new Stage();
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
            FXMLLoader loader = new FXMLLoader(new File("Resources/View/main_pane.fxml").toURL());
            Parent root = loader.load();
            ((MainController)loader.getController()).setFST(f);
            Scene scene = new Scene(root, 700, 600);
            primaryStage.setTitle("Theory of Automata");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
    @FXML
    void loadAuto(){

    }
}

