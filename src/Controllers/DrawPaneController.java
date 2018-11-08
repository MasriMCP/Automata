package Controllers;

import Auto.FiniteStateTransducer;
import Visual.State;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.util.HashSet;
import java.util.Optional;

public class DrawPaneController {
    @FXML
    private AnchorPane drawPane;
    private FiniteStateTransducer fst;
    HashSet<State> stateSet;
    private State selected;
    private final static Paint COLOR_SELECTED = Color.RED,
    COLOR_NOT_SELECTED = Color.BLACK;
    private boolean isCreatingTransition = false;// is true when the user is creating a transition between two states
    @FXML
    private void mouseClick(MouseEvent event){
        boolean occupied = false;
        //chack that the click was not inside a state
        for(State state:stateSet){
            //the distance between the click and the state is less than the radius
            if(dist(event.getSceneX(),event.getSceneY(),state.getCenterX(),state.getCenterY())<State.R){
                occupied = true;
                break;
            }
        }
        //if there's no state where the user pressed, add a new state
        if(!occupied){
             addState(event.getSceneX(), event.getSceneY());
        }
    }
    private void addState(double x, double y){
        State s = new State(x,y);
        TextInputDialog nameInput = new TextInputDialog();
        nameInput.setContentText("enter a name for this state");
        nameInput.setHeaderText("enter name");
        nameInput.setContentText("OK");
        Optional<String> n = nameInput.showAndWait();
        n.ifPresent(name -> {
            s.setName(name);
        });
        drawPane.getChildren().add(s);
        stateSet.add(s);
        s.setFill(COLOR_NOT_SELECTED);
        //add click listener
        //selects this state when pressed and deselects the previous selected state
        Line l = new Line();
        drawPane.getChildren().add(l);
        l.setStrokeWidth(3);
        s.setOnMousePressed((event)->{
            if(event.isPrimaryButtonDown()){
                if(selected!=null) selected.setFill(COLOR_NOT_SELECTED);
                s.setFill(COLOR_SELECTED);
                selected = s;
            }
            else if (event.isSecondaryButtonDown()){
                isCreatingTransition = true;

            }
        });
        //add drag listener
        //drags the state to where the mouse is

        s.setOnMouseDragged((event) -> {

            if(event.isPrimaryButtonDown()){
                s.setCenterX(event.getSceneX());
                s.setCenterY(event.getSceneY());
            }
            else if(event.isSecondaryButtonDown()){
                l.setStartX(s.getCenterX());
                l.setStartY(s.getCenterY());
                l.setEndX(event.getSceneX());
                l.setEndY(event.getSceneY());
            }
        });
        s.setOnMouseReleased((event)->{
            isCreatingTransition = false;
            //make the line invisible
            l.setEndX(l.getStartX());
            l.setEndY(l.getStartY());
        });
    }
    public void setFST(FiniteStateTransducer fst){
        this.fst = fst;
        System.out.println(fst);
        stateSet = new HashSet<>();
    }
    //returns the distance between two points (x0,y0) and (x1,y1)
    private double dist(double x0,double y0,double x1, double y1){
        return Math.sqrt(Math.pow(x0-x1,2)+Math.pow(y0-y1,2));
    }
}
