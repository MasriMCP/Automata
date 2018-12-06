package Visual;

import Auto.FiniteStateTransducer;
import Auto.MealyMachine;
import Auto.MooreMachine;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
/*
    $$\   $$\            $$\            $$$$$$\                                    $$\            $$\
    $$$\  $$ |           $$ |          $$  __$$\                                   $$ |           $$ |
    $$$$\ $$ | $$$$$$\ $$$$$$\         $$ /  \__| $$$$$$\  $$$$$$\$$$$\   $$$$$$\  $$ | $$$$$$\ $$$$$$\    $$$$$$\
    $$ $$\$$ |$$  __$$\\_$$  _|        $$ |      $$  __$$\ $$  _$$  _$$\ $$  __$$\ $$ |$$  __$$\\_$$  _|  $$  __$$\
    $$ \$$$$ |$$ /  $$ | $$ |          $$ |      $$ /  $$ |$$ / $$ / $$ |$$ /  $$ |$$ |$$$$$$$$ | $$ |    $$$$$$$$ |
    $$ |\$$$ |$$ |  $$ | $$ |$$\       $$ |  $$\ $$ |  $$ |$$ | $$ | $$ |$$ |  $$ |$$ |$$   ____| $$ |$$\ $$   ____|
    $$ | \$$ |\$$$$$$  | \$$$$  |      \$$$$$$  |\$$$$$$  |$$ | $$ | $$ |$$$$$$$  |$$ |\$$$$$$$\  \$$$$  |\$$$$$$$\
    \__|  \__| \______/   \____/        \______/  \______/ \__| \__| \__|$$  ____/ \__| \_______|  \____/  \_______|
                                                                         $$ |
                                                                         $$ |
                                                                         \__|
 */
public class VisualFST extends Group {
    private FiniteStateTransducer fst;
    HashSet<State> stateSet;
    private Transition connectingTransition;//only used while the user is dragging a transition initStateArrow
    private Node selected, hovered;//the currently selected state
    private final static Paint COLOR_SELECTED = Color.RED,
            COLOR_NOT_SELECTED = Color.DARKGRAY,
            COLOR_TEXT_SELECTED = Color.WHITE,
            COLOR_FINAL_STROKE = Color.BLACK,
            COLOR_HOVER_STATE = Color.PURPLE,
            COLOR_TRANSITION_SELECTED = Color.RED,
            COLOR_TRANSITION_NOT_SELECTED = Color.ORANGE;//colors used
    HashSet<Transition> transitionSet;


    public VisualFST(FiniteStateTransducer fst) {
        this.fst = fst;
        stateSet = new HashSet<State>();
        transitionSet = new HashSet<>();
        connectingTransition = new Transition(null, null);
    }

    public FiniteStateTransducer getFst() {
        return fst;
    }

    public State addState(double x, double y, String name) {
        State s = null;
        //check for naming redundancy
        for (State i : stateSet) {

            if (i.getName().equals(name)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Cannot add state");
                alert.setContentText("There's already a state named \"" + name + "\"");
                alert.showAndWait();
                return null;//if the nae is redundant, odnt add a state
            }
        }
        if (fst.getType().equals("mor")) {
            TextInputDialog outputDialog = new TextInputDialog();
            outputDialog.setTitle("state output");
            outputDialog.setHeaderText("Enter state output:");
            outputDialog.setContentText("output:");
            Optional<String> outputResult = outputDialog.showAndWait();
            if (outputResult.isPresent()) {
                fst.addOutputAlpha(outputResult.get().charAt(0));
                s = new MooreState(x, y, outputResult.get().charAt(0));
                ((MooreMachine) fst).addStateOutput(name, outputResult.get().charAt(0));
            } else {
                return null;
            }
        } else {
            s = new State(x, y);
            fst.addState(name);
        }
        stateSet.add(s);
        s.setName(name);
        s.setLableFill(COLOR_TEXT_SELECTED);
        return s;
    }


}