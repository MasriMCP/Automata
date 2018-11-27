package Controllers;

import Auto.DFA;
import Auto.FiniteStateTransducer;
import Auto.NFA;
import Visual.State;
import Visual.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class MainController {
    private FiniteStateTransducer fst;
    HashSet<State> stateSet;
    private State selected;//the currently selected state
    private final static Paint COLOR_SELECTED = Color.RED,
            COLOR_NOT_SELECTED = Color.BLACK,
            COLOR_TEXT_SELECTED = Color.WHITE,
            COLOR_TRANSITION_NOT_SELECTED=Color.ORANGE;//colors used
    private boolean connecting = false;// is true when the user is creating a transition between two states
    public final static int SELECT=0,ADD=1,CONNECT=2,DELETE=3,INITIAL=4,FINAL=5;
    HashSet<Transition> transitionSet = new HashSet<>();
    final FileChooser fileChooser = new FileChooser();
    private int mode =SELECT;
    @FXML
    private VBox toolBox;

    @FXML
    private ToggleGroup toolbox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label descLabel;

    @FXML
    private Label inputAlphaLabel;

    @FXML
    private TextField inputAlphaText;

    @FXML
    private TextField inputText;

    @FXML
    private Label outputLabel;

    @FXML
    private Label modeLabel;

    @FXML
    private Font x3;

    @FXML
    private Color x4;
    @FXML
    private AnchorPane drawPane;
    @FXML
    private Label typeLabel;
    @FXML
    MenuBar menuBar;

    @FXML
    void run(ActionEvent event) {
        if(fst instanceof DFA){
            outputLabel.setText(((DFA)(fst)).isAccepted(inputText.getText())?"accepted":"not accepted");
        }
        else if(fst instanceof NFA){
            outputLabel.setText(((NFA)(fst)).isAccepted(inputText.getText())?"accepted":"not accepted");
        }
        else {
            outputLabel.setText(fst.run(inputText.getText()));
        }
    }

    @FXML
    void toolAdd(ActionEvent event) {
        mode=ADD;
        connecting = false;
        modeLabel.setText("add");
    }

    @FXML
    void toolConnect(ActionEvent event) {
        mode=CONNECT;
        modeLabel.setText("connect");
    }

    @FXML
    void toolDelete(ActionEvent event) {
        mode=DELETE;
        connecting = false;
        modeLabel.setText("delete");
    }

    @FXML
    void toolFinal(ActionEvent event) {
        mode=FINAL;
        connecting = false;
    }

    @FXML
    void toolInitial(ActionEvent event){
        mode = INITIAL;
        connecting = false;
    }

    @FXML
    void toolSelect(ActionEvent event) {
        mode=SELECT;
        connecting = false;
        modeLabel.setText("select");
    }
    void setFST(FiniteStateTransducer fst){
        this.fst = fst;
        stateSet = new HashSet<>();
        typeLabel.setText("Type:"+fst.getClass().getSimpleName());
        nameLabel.setText("Name: "+fst.getName());
        descLabel.setText("Description: "+fst.describe());

    }
    public void mouseClick(MouseEvent mouseEvent){

        if(!isOccupied(mouseEvent.getX(),mouseEvent.getY())&&mode==ADD){
            addState(mouseEvent.getX(),mouseEvent.getY());
        }

    }
    TextInputDialog dialog = new TextInputDialog();

    private State addState(double x,double y){
        State s = new State(x,y);

        dialog.setTitle("name");
        dialog.setHeaderText("Enter state name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            fst.addState(name);
            drawPane.getChildren().add(s);
            stateSet.add(s);
            s.setName(name);
            s.setLableFill(COLOR_TEXT_SELECTED);
            s.setOnMousePressed((e)->{
                if(connecting){
                    addTransition(selected,'0',s);
                }
                else if(mode==SELECT||mode ==CONNECT||mode ==INITIAL||mode ==FINAL||mode==DELETE){
                    if(selected!=null) selected.setCircleFill(COLOR_NOT_SELECTED);
                    selected = s;
                    selected.setCircleFill(COLOR_SELECTED);
                    if(mode==CONNECT){
                        connecting=true;
                    }
                    if(mode==INITIAL){
                        setInitial(s);
                    }
                    if(mode==FINAL){
                        setFinal(s);
                    }
                    if(mode==DELETE){
                        deleteState(s);
                    }
                }
            });
            s.setOnMouseDragged((e)->{
                if(mode ==SELECT){
                    selected.setCenterX(e.getSceneX()-toolBox.getWidth());
                    selected.setCenterY(e.getSceneY()-menuBar.getHeight());
                }
                if(mode == CONNECT){

                }
            });
        });
        dialog.setResult("");
        return s;
    }
    private void addTransition(State s0,char symbol,State s1){

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Transition");
        dialog.setHeaderText("Enter a list or range of symbols:");
        dialog.setContentText("symbols:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(str -> {
            Transition trans=null;
            if(fst.isConnected(s0.getName(),s1.getName())){
                for(Transition t:transitionSet){
                    if(t.getS0()==s0&&t.getS1()==s1) trans = t;
                }
            }
            else {
                trans = new Transition(s0, s1);
                transitionSet.add(trans);
                trans.setFill(COLOR_TRANSITION_NOT_SELECTED);
                drawPane.getChildren().add(trans);
            }

            connecting = false;
            if(str.matches("[A-Za-z]..[A-Za-z]|\\d..\\d")){
                fst.addTransitionRange(s0.getName(),str.charAt(0),s1.getName(),str.charAt(3));
                trans.addSymbolRange(str.charAt(0),str.charAt(3));
            }
            else{
                String[] temp = str.split("\\s+|,");//one or more spaces or a comma
                for(int i=0;i<temp.length;i++){
                    fst.addTransition(s0.getName(),temp[i].charAt(0),s1.getName());
                    trans.addSymbol(temp[i].charAt(0));
                }

            }


        });

    }
    private void deleteState(State state){
        HashSet<Transition> temp = new HashSet<>();
        for(Transition t:transitionSet){
            if(t.getS0()==state||t.getS1()==state){
                temp.add(t);
            }
        }
        transitionSet.removeAll(temp);
        drawPane.getChildren().removeAll(temp);
        drawPane.getChildren().remove(state);
        fst.delete(state.getName());
    }
    private void setInitial(State s){
        fst.setInitialState(s.getName());
        s.setCircleFill(Color.CYAN);
    }
    private void setFinal(State s){
        if(fst instanceof DFA){
            ((DFA)(fst)).setFinalState(s.getName());
        }
        else if(fst instanceof NFA){
            ((NFA)(fst)).setFinalState(s.getName());
        }
        s.setCircleFill(Color.YELLOW);
    }
    private void select(MouseEvent e){
        for(State s:stateSet){
            if(dist(e.getX(),e.getY(),s.getCenterX(),s.getCenterY())<State.R){
                if(selected!=null) selected.setCircleFill(COLOR_NOT_SELECTED);
                selected = s;
                selected.setCircleFill(COLOR_SELECTED);

            }
        }
    }
    @FXML
    public void inputAlphaAdd(ActionEvent event){

        String str= inputAlphaText.getText();
        if(str.matches("[A-Za-z]..[A-Za-z]|\\d..\\d")){
            fst.addInputAlphaRange(str.charAt(0),str.charAt(3));
        }
        else{
            String[] temp = str.split("\\s+|,");//one or more spaces or a comma
            for(int i=0;i<temp.length;i++){
                fst.addInputAlpha(temp[i].charAt(0));

            }

        }
        Object[] sorted = fst.getInputAlpha().toArray();
        Arrays.sort(sorted);
        inputAlphaLabel.setText(Arrays.toString(sorted));
        inputAlphaText.setText("");
        System.out.println(fst);
    }
    //returns the distance between two points (x0,y0) and (x1,y1)
    private double dist(double x0,double y0,double x1, double y1){
        return Math.sqrt(Math.pow(x0-x1,2)+Math.pow(y0-y1,2));
    }
    private boolean isOccupied(double x,double y){
        for(State s:stateSet) {
            if (dist(x, y, s.getCenterX(), s.getCenterY()) < 2*State.R) {
                return true;
            }
        }
        return false;
    }
    @FXML Label debugLabel;
    @FXML
    void debug(){
        debugLabel.setText(fst.toString());
    }
    @FXML
    void save(ActionEvent event){
        HashMap<String,String> vertices = new HashMap<>();
        for(State s:stateSet){
            vertices.put(s.getName(),String.valueOf(s.getCenterX())+","+String.valueOf(s.getCenterY()));
        }
        JSONObject obj = new JSONObject();
        obj.put("vertices",vertices);
        obj.put("fst",fst.toString());
        FileChooser chooser =new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FST",".fst"));

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(chooser
        .showSaveDialog(drawPane.getScene().getWindow()))))){

            out.write(obj.toJSONString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

