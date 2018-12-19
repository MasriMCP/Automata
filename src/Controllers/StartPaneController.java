package Controllers;

import Auto.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

