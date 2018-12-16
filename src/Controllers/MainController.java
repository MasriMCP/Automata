package Controllers;

import Auto.*;
import Visual.MealyTransition;
import Visual.MooreState;
import Visual.State;
import Visual.Transition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.security.krb5.internal.Krb5;

import java.awt.*;
import java.awt.Button;
import java.io.*;
import java.util.*;

public class MainController {

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
    private int noNameStateCounter = 0;
    private File openFile = null;
    private boolean connecting = false;// is true when the user is creating a transition between two states
    public final static int SELECT = 0, ADD = 1, CONNECT = 2, DELETE = 3, INITIAL = 4, FINAL = 5;
    HashSet<Transition> transitionSet = new HashSet<>();
    final FileChooser fileChooser = new FileChooser();
    private String openPath = null;
    private int mode = SELECT;
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
    private AnchorPane drawPane;
    @FXML
    private Label typeLabel;
    @FXML
    MenuBar menuBar;
    @FXML
    ToggleButton finalToggle;
    ImageView initStateArrow;

    @FXML
    void run(ActionEvent event) {
        if (fst.getInitialState() == null) {
            Alert noInitialAlert = new Alert(Alert.AlertType.WARNING,
                    "No initial state has been set");
            noInitialAlert.setTitle("cannot run");
            noInitialAlert.showAndWait();
            return;
        }
        try {
            if (fst instanceof DFA) {
                outputLabel.setText(((DFA) (fst)).isAccepted(inputText.getText()) ? "accepted" : "not accepted");
            } else if (fst instanceof NFA) {
                outputLabel.setText(((NFA) (fst)).isAccepted(inputText.getText()) ? "accepted" : "not accepted");
            } else {
                System.out.println(inputText.getText());
                System.out.println(fst.run(inputText.getText()));
                outputLabel.setText(fst.run(inputText.getText()));
            }
        } catch (IllegalStateException ex) {
            Alert noInitialAlert = new Alert(Alert.AlertType.WARNING,
                    "Not a total function");
            noInitialAlert.setTitle("cannot run");
            noInitialAlert.showAndWait();
            return;
        }
    }

    @FXML
    void toolAdd(Event event) {
        mode = ADD;
        modeLabel.setText("add");
    }

    @FXML
    void toolConnect(Event event) {
        mode = CONNECT;
        modeLabel.setText("connect");
    }

    @FXML
    void toolDelete(Event event) {
        mode = DELETE;
        modeLabel.setText("delete");
    }

    @FXML
    void toolFinal(Event event) {
        mode = FINAL;
        modeLabel.setText("final");
    }

    @FXML
    void toolInitial(Event event) {
        mode = INITIAL;
        modeLabel.setText("initial");
    }

    @FXML
    void toolSelect(Event event) {
        mode = SELECT;
        modeLabel.setText("select");
    }

    void setFST(FiniteStateTransducer fst) {
        //this method always gets called first and s pretty much a constructor
        //this should have been done differently but no one is going to read this code anyway
        //so.................................................................................
        this.fst = fst;
        stateSet = new HashSet<>();
        typeLabel.setText("Type:" + fst.getClass().getSimpleName());
        nameLabel.setText("Name: " + fst.getName());
        descLabel.setText("Description: " + fst.describe());
        try {
            initStateArrow = new ImageView(new Image(new FileInputStream("Resources/images/arrow.png")));
            initStateArrow.setFitHeight(20);
            initStateArrow.setFitWidth(20);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        connectingTransition = new Transition(null, null);
        connectingTransition.setStart(-100, -100);
        connectingTransition.setEnd(-100, -100);
        drawPane.getChildren().add(connectingTransition);
        if (!(fst instanceof DFA || fst instanceof NFA)) {
            finalToggle.setVisible(false);
        }
        drawPane.getScene().setOnKeyPressed(e -> {

            if(e.getCode()==KeyCode.V){
                toolSelect(e);

            }
            else if(e.getCode()==KeyCode.C){
                toolConnect(e);
            }
            else if(e.getCode()==KeyCode.DELETE){
                toolDelete(e);
            }
            else if(e.getCode()==KeyCode.A){
                toolAdd(e);
            }
            else if (e.getCode() == KeyCode.S && e.isControlDown() && e.isShiftDown()) {
                saveAsDialog(e);
            } else if (e.getCode() == KeyCode.S && e.isControlDown()) {
                saveDialog(e);
            }
            else if(e.getCode() == KeyCode.N && e.isControlDown()){
                newFile(e);
            }
            else if(e.getCode() == KeyCode.O && e.isControlDown()){
                openFile(e);
            }
        });
        drawPane.getScene().getWindow().setOnCloseRequest(this::quit);

    }

    public void mouseClick(MouseEvent mouseEvent) {

        if (!isOccupied(mouseEvent.getX(), mouseEvent.getY()) && mode == ADD) {
            addState(mouseEvent.getX(), mouseEvent.getY());
        } else if (mode == SELECT) {
            if (selected instanceof Transition) {
                ((Transition) selected).setFill(COLOR_TRANSITION_NOT_SELECTED);
            } else if (selected instanceof State) {
                ((State) selected).setCircleFill(COLOR_NOT_SELECTED);
            }
            selected = null;
        }


    }


    private void addState(double x, double y) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("name");
        dialog.setHeaderText("Enter state name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            name = name.trim();
            if(!name.matches("[A-Za-z0-9\\-]{0,8}")){
                Alert nameNotAcceptedAlert = new Alert(Alert.AlertType.ERROR,
                        "State names can only be alphanumerical or a dash \"-\" and under 8 characters long.",ButtonType.OK);
                nameNotAcceptedAlert.showAndWait();
                return;
            }
            else if (name.equals("")) {
                noNameStateCounter++;
                name = generateNoName();
            }
            State s = null;
            //check for naming redundancy
            for (State i : stateSet) {

                if (i.getName().equals(name)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Cannot add state");
                    alert.setContentText("There's already a state named \"" + name + "\"");
                    alert.showAndWait();
                    return;//if the nae is redundant, odnt add a state
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
                    return;
                }
            } else {
                s = new State(x, y);
                fst.addState(name);
            }


            drawPane.getChildren().add(s);
            stateSet.add(s);
            s.setName(name);
            s.setCircleFill(COLOR_NOT_SELECTED);
            s.setLableFill(COLOR_TEXT_SELECTED);
            initState(s);

        });
        dialog.setResult("");
    }

    String generateNoName() {
        int temp = noNameStateCounter;
        String ret = "";
        int offset = 0x0626;
        int range = 16;
        while (temp>0){
            ret += String.valueOf((char)(temp%range+offset));
            temp/=range;
        }
        return ret;
    }

    private void addTransition(State s0, State s1) {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Transition");
        dialog.setHeaderText("Enter a list or range of symbols:");
        dialog.setContentText("symbols:");
        HashSet<Character> newlyAddedSymbols = new HashSet<>();
        Optional<String> result = dialog.showAndWait();
        char mealyOutputChar = 0;
        result.ifPresent(symbolsString -> {
            TextInputDialog mealyOutputDialog = new TextInputDialog();
            mealyOutputDialog.setContentText("add an output character");
            mealyOutputDialog.setHeaderText("add output");
            Optional<String> mealyOutputResult = null;
            //compile a list of legal symbols,a list of illegal symbols and a list of symbols that are already used by s0
            LinkedList<Character> illegalSymbolsList = new LinkedList<>(), legalSymbolsList = new LinkedList<>(), addedSymbolsList = new LinkedList<>();
            //if it's in the form letter..letter or number..number
            if (symbolsString.matches("\\s*[A-Za-z]\\.\\.[A-Za-z]\\s*|\\d\\.\\.\\d\\s*")) {
                symbolsString = symbolsString.trim();
                char symbol0 = symbolsString.charAt(0), symbol1 = symbolsString.charAt(3);
                if (symbol1 > symbol0)
                    for (char i = symbol0; i <= symbol1; i++) {
                        if (fst.getTransitionMap().containsKey(s0.getName() + String.valueOf(i)) && !(fst instanceof NFA))
                            addedSymbolsList.add(i);
                        else {
                            if (fst.getInputAlpha().contains(i)) legalSymbolsList.add(i);
                            else illegalSymbolsList.add(i);
                        }


                    }
                else
                    for (char i = symbol1; i <= symbol0; i++) {
                        if (fst.getTransitionMap().containsKey(s0.getName() + String.valueOf(i)) && !(fst instanceof NFA))
                            addedSymbolsList.add(i);
                        else {
                            if (fst.getInputAlpha().contains(i)) legalSymbolsList.add(i);
                            else illegalSymbolsList.add(i);
                        }
                    }
            } else if(symbolsString.matches("\\s*([A-Za-z0-9]\\s*,\\s*)*[A-Za-z0-9]\\s*")) {
                symbolsString = symbolsString.trim();
                String[] temp = symbolsString.split("\\s*,\\s*");
                for (int i = 0; i < temp.length; i++) {

                    char addedSymbol = 0;
                    addedSymbol = temp[i].charAt(0);
                    if (fst.getTransitionMap().containsKey(s0.getName() + String.valueOf(addedSymbol)) && !(fst instanceof NFA))
                        addedSymbolsList.add(addedSymbol);
                    else {
                        if (fst.getInputAlpha().contains(addedSymbol)) legalSymbolsList.add(addedSymbol);
                        else illegalSymbolsList.add(addedSymbol);
                    }

                }
            }
            else if(fst instanceof NFA && symbolsString.matches("\\s*")){
                    char addedSymbol = NFA.LAMBDA;
                    if (fst.getTransitionMap().containsKey(s0.getName() + String.valueOf(addedSymbol)) && !(fst instanceof NFA))
                        addedSymbolsList.add(addedSymbol);
                    else {
                        if (fst.getInputAlpha().contains(addedSymbol)) legalSymbolsList.add(addedSymbol);
                        else illegalSymbolsList.add(addedSymbol);
                    }
            }
            else{
                new Alert(Alert.AlertType.ERROR,"input must be alphanumerical in the form:\n1.range: (char)..(char)\n" +
                        "2.comma separated values: (char),(char)...\n3.empty input is equal to lambda (NFA only)").showAndWait();
                return;
            }

            //reference to the transition object
            Transition trans = null;
            //if the two states are already connected, find a reference to the transition object that connects them
            if (fst.isConnected(s0.getName(), s1.getName())) {
                for (Transition t : transitionSet) {
                    if (t.getS0() == s0 && t.getS1() == s1) trans = t;
                }
                if (fst.getType().equals("mea")) {
                    mealyOutputResult = mealyOutputDialog.showAndWait();
                    if (!mealyOutputResult.isPresent() || "".equals(mealyOutputResult.get())) {
                        return;
                    }
                }
            }

            //otherwise, create a new transition object
            else {
                if (fst.getType().equals("mea")) {
                    mealyOutputResult = mealyOutputDialog.showAndWait();
                    trans = new MealyTransition(s0, s1);


                    if (!mealyOutputResult.isPresent()||"".equals(mealyOutputResult.get())) {
                        return;
                    }
                } else {
                    trans = new Transition(s0, s1);
                }

                trans.setOnMouseClicked(e -> {
                    if (mode == SELECT) {
                        if (selected != null) {
                            if (selected instanceof State) {
                                ((State) selected).setCircleFill(COLOR_NOT_SELECTED);
                            } else if (selected instanceof Transition) {
                                ((Transition) selected).setFill(COLOR_TRANSITION_NOT_SELECTED);
                            }
                        }
                        selected = (Transition) e.getSource();
                        ((Transition) selected).setFill(COLOR_TRANSITION_SELECTED);
                    } else if (mode == DELETE) {
                        Transition deletedTransition = (Transition) e.getSource();
                        //
                        TextInputDialog deleteDialog = new TextInputDialog();

                        deleteDialog.setTitle("delete transition");
                        deleteDialog.setHeaderText("Enter a list or range of symbols to delete:");
                        deleteDialog.setContentText("symbols:");

                        Optional<String> deleteResult = deleteDialog.showAndWait();

                        deleteResult.ifPresent(deleteString -> {
                            if (deleteString.matches("[A-Za-z]..[A-Za-z]|\\d..\\d")) {
                                char symbol0 = deleteString.charAt(0), symbol1 = deleteString.charAt(3);
                                if (symbol1 > symbol0)
                                    for (char i = symbol0; i <= symbol1; i++) {
                                        if (fst.getInputAlpha().contains(i)) {
                                            fst.deleteTransition(deletedTransition.getS0().getName(), i, deletedTransition.getS1().getName());
                                            if(fst instanceof MealyMachine){
                                                fst.getOutputMap().remove(s0.getName()+i);
                                            }
                                            deletedTransition.removeSymbol(i);
                                        }

                                    }
                                else
                                    for (char i = symbol1; i <= symbol0; i++) {
                                        if (fst.getInputAlpha().contains(i)) {
                                            fst.deleteTransition(deletedTransition.getS0().getName(), i, deletedTransition.getS1().getName());
                                            if(fst instanceof MealyMachine){
                                                fst.getOutputMap().remove(s0.getName()+i);
                                            }
                                            deletedTransition.removeSymbol(i);

                                        }
                                    }
                            } else {
                                String[] deleteTemp = deleteString.split("\\s+|,");//one or more spaces or a comma
                                if (deleteTemp.length == 0) {
                                    if (fst.getInputAlpha().contains(NFA.LAMBDA)) {
                                        fst.deleteTransition(deletedTransition.getS0().getName(), NFA.LAMBDA, deletedTransition.getS1().getName());
                                        deletedTransition.removeSymbol(NFA.LAMBDA);

                                    }
                                } else
                                    for (int i = 0; i < deleteTemp.length; i++) {
                                        if (fst.getInputAlpha().contains(deleteTemp[i].charAt(0))) {
                                            fst.deleteTransition(deletedTransition.getS0().getName(), deleteTemp[i].charAt(0), deletedTransition.getS1().getName());
                                            if(fst instanceof MealyMachine){
                                                fst.getOutputMap().remove(s0.getName()+deleteTemp[i].charAt(0));
                                            }
                                            deletedTransition.removeSymbol(deleteTemp[i].charAt(0));

                                        }

                                    }
                            }
                            if (deletedTransition.getSymbols().size() == 0) {
                                drawPane.getChildren().remove(deletedTransition);
                            }
                            //add the newly added symbols to the input alpha label
                            Object[] sorted = fst.getInputAlpha().toArray();
                            Arrays.sort(sorted);
                            inputAlphaLabel.setText(Arrays.toString(sorted));

                        });
                        deletedTransition.updateLabel();
                    }
                });

            }

            if (legalSymbolsList.size() > 0) {

                //only add a transition if there are legal symbols to be added
                if (!drawPane.getChildren().contains(trans)) {
                    transitionSet.add(trans);
                    drawPane.getChildren().add(trans);
                    trans.toBack();
                }
                for (char i : legalSymbolsList) {
                    fst.addTransition(s0.getName(), i, s1.getName());
                    trans.addSymbol(i);
                }
                newlyAddedSymbols.addAll(legalSymbolsList);
            }
            if (illegalSymbolsList.size() > 0) {

                Alert illegalSymbolsAlert = new Alert(Alert.AlertType.WARNING,
                        "symbols: " + illegalSymbolsList.toString() + " are not in the input alphabet." +
                                "would you like to add them?", ButtonType.YES, ButtonType.NO);
                illegalSymbolsAlert.setTitle("Illegal symbols");
                Optional<ButtonType> illegalSymbolsResult = illegalSymbolsAlert.showAndWait();
                if (illegalSymbolsResult.get() == ButtonType.YES) {
                    //if the transition object has not already been added (no legal symbols)
                    if (!drawPane.getChildren().contains(trans)) {
                        transitionSet.add(trans);
                        drawPane.getChildren().add(trans);
                        trans.toBack();
                    }
                    for (char i : illegalSymbolsList) {
                        fst.addInputAlpha(i);
                        fst.addTransition(s0.getName(), i, s1.getName());
                        trans.addSymbol(i);
                    }
                    newlyAddedSymbols.addAll(illegalSymbolsList);
                    //add the newly added symbols to the input alpha label
                    Object[] sorted = fst.getInputAlpha().toArray();
                    Arrays.sort(sorted);
                    inputAlphaLabel.setText(Arrays.toString(sorted));

                }
            }
            if (addedSymbolsList.size() > 0) {
                Alert addedSymbolsAlert = new Alert(Alert.AlertType.WARNING,
                        "symbols: " + addedSymbolsList.toString() + " are already connected to other states." +
                                "would you like to delete those transitions and add new transitions?", ButtonType.YES, ButtonType.NO);
                addedSymbolsAlert.setTitle("added symbols");
                Optional<ButtonType> addedSymbolsResult = addedSymbolsAlert.showAndWait();
                if (addedSymbolsResult.get() == ButtonType.YES) {
                    for (char i : addedSymbolsList) {
                        Transition deletedTransition = null;
                        for (Transition t : transitionSet) {
                            if (t.getS0() == s0 && t.getSymbols().contains(i)) {
                                deletedTransition = t;
                                break;
                            }
                        }
                        deletedTransition.removeSymbol(i);
                        fst.deleteTransition(s0.getName(), i, s1.getName());
                        if (deletedTransition.getSymbols().size() <= 0) {
                            transitionSet.remove(deletedTransition);
                            drawPane.getChildren().remove(deletedTransition);
                        }
                        if (fst instanceof MealyMachine) {
                            fst.addOutputAlpha(mealyOutputChar);
                            ((MealyMachine) fst).addTransitionOutput(s0.getName(), i, s1.getName(), mealyOutputChar);
                            addMealyTransition(s0, s1, i, mealyOutputChar);
                        } else {
                            fst.addTransition(s0.getName(), i, s1.getName());
                            addTransition(s0, s1, i);
                        }

                        deletedTransition.updateLabel();
                    }
                }
            }
            if (fst.getType().equals("mea")) {
                fst.addOutputAlpha(mealyOutputResult.get().charAt(0));
                for (char i : newlyAddedSymbols) {
                    ((MealyMachine) fst).addTransitionOutput(s0.getName(), i, s1.getName(), mealyOutputResult.get().charAt(0));
                    ((MealyTransition) trans).addSymbolOutput(i, mealyOutputResult.get().charAt(0));
                }

            }
            trans.updateLabel();
        });

    }

    private void deleteState(State state) {
        HashSet<Transition> temp = new HashSet<>();
        for (Transition t : transitionSet) {
            if (t.getS0() == state || t.getS1() == state) {
                temp.add(t);
            }
        }
        transitionSet.removeAll(temp);
        stateSet.remove(state);
        drawPane.getChildren().removeAll(temp);
        drawPane.getChildren().remove(state);
        fst.delete(state.getName());
    }

    private void setInitial(State s) {
        fst.setInitialState(s.getName());
        if (!drawPane.getChildren().contains(initStateArrow)) drawPane.getChildren().add(initStateArrow);
        initStateArrow.xProperty().unbind();
        initStateArrow.yProperty().unbind();
        initStateArrow.xProperty().bind(s.layoutXProperty().subtract(State.R));
        initStateArrow.yProperty().bind(s.layoutYProperty().subtract(State.R - 10));
    }

    private void setFinal(State s) {
        if (fst instanceof DFA) {
            //if it's already a final state
            if (fst.getOutputMap().get(s.getName()) == '1') {
                ((DFA) (fst)).deleteFinalState(s.getName());
                s.setCircleStroke(null);
            } else {
                ((DFA) (fst)).setFinalState(s.getName());
                s.setCircleStroke(COLOR_FINAL_STROKE);
            }
        } else if (fst instanceof NFA) {
            //if it's already a final state
            if (fst.getOutputMap().get(s.getName()) == '1') {
                ((NFA) (fst)).deleteFinalState(s.getName());
                s.setCircleStroke(null);
            } else {
                ((NFA) (fst)).setFinalState(s.getName());
                s.setCircleStroke(COLOR_FINAL_STROKE);
            }
        }

    }

    @FXML
    public void inputAlphaAdd(ActionEvent event) {

        String str = inputAlphaText.getText();
        if (str.matches("[A-Za-z]..[A-Za-z]|\\d..\\d")) {
            fst.addInputAlphaRange(str.charAt(0), str.charAt(3));
        } else {
            String[] temp = str.split("\\s+|,");//one or more spaces or a comma
            for (int i = 0; i < temp.length; i++) {
                fst.addInputAlpha(temp[i].charAt(0));

            }

        }
        Object[] sorted = fst.getInputAlpha().toArray();
        Arrays.sort(sorted);
        inputAlphaLabel.setText(Arrays.toString(sorted));
        inputAlphaText.setText("");
    }

    //returns the distance between two points (x0,y0) and (x1,y1)
    private double dist(double x0, double y0, double x1, double y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
    }

    private boolean isOccupied(double x, double y) {
        for (State s : stateSet) {
            if (dist(x, y, s.getCenterX(), s.getCenterY()) < 2 * State.R) {
                return true;
            }
        }
        return false;
    }

    @FXML
    Label debugLabel;

    @FXML
    void debug() {
        debugLabel.setText(fst.toString());
    }

    @FXML
    void saveDialog(Event event) {
        if (openPath == null) {
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File("C:\\Users\\jit\\Desktop\\examples"));
            chooser.setInitialFileName(fst.getName()+".fst");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FST", ".fst"));
            try {
                openPath = chooser.showSaveDialog(drawPane.getScene().getWindow()).getAbsolutePath();
            }
            catch (NullPointerException ex){
                return;
            }

        }
        try {
            save(openPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveAsDialog(Event event) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File("C:\\Users\\jit\\Desktop\\examples"));
        chooser.setInitialFileName(fst.getName());
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FST", ".fst"));
        try {
            openPath = chooser.showSaveDialog(drawPane.getScene().getWindow()).getAbsolutePath();
        }
        catch (NullPointerException ex){
            return;
        }
        try {
            save(openPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void save(String path) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream((new FileOutputStream(path)));
        Map<String, Point> saveStateMap = new HashMap<>();
        for (State i : stateSet) {
            saveStateMap.put(i.getName(), new Point((int) i.getCenterX(), (int) i.getCenterY()));
        }
        Map<String, Point> saveControlPointMap = new HashMap<>();
        for (Transition i : transitionSet) {
            saveControlPointMap.put(i.getS0().getName() + "," + i.getS1().getName(), i.getControlPoint());
        }
        out.writeObject(saveStateMap);
        out.writeObject(saveControlPointMap);
        out.writeObject(fst);
    }

    @FXML
    public void clear(ActionEvent e) {
        Alert clearAlert = new Alert(Alert.AlertType.WARNING,
                "are you sure? this will delete all states and transitions.", ButtonType.YES, ButtonType.NO);
        clearAlert.setTitle("Clear");
        Optional<ButtonType> clearResult = clearAlert.showAndWait();
        if (clearResult.get() == ButtonType.YES) {

            HashSet<State> copy = new HashSet<>();
            for (State i : stateSet) {
                copy.add(i);
            }
            for (State i : copy) {
                deleteState(i);
            }
            if (!(fst instanceof DFA || fst instanceof NFA))
                fst.getOutputAlpha().removeAll(fst.getOutputAlpha());
            Set<String> copy2 = fst.getOutputMap().keySet();
            for (String i : copy2) {
                fst.getOutputMap().remove(i);
            }
        }
    }
    public void load(String path) throws IOException {
        openPath = path;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            Map<String, Point> loadStateMap = (HashMap<String, Point>) in.readObject();
            Map<String, Point> loadControlPointMap = (HashMap<String, Point>) in.readObject();
            fst = (FiniteStateTransducer) in.readObject();
            inputAlphaLabel.setText(fst.getInputAlpha().toString());
            setFST(fst);


            for (String i : loadStateMap.keySet()) {
                State s = (fst instanceof MooreMachine) && !(fst instanceof DFA || fst instanceof NFA) ?
                        addMooreState(loadStateMap.get(i).x, loadStateMap.get(i).y, i, fst.getOutputMap().get(i)) :
                        addState(loadStateMap.get(i).x, loadStateMap.get(i).y, i);
                if (i.equals(fst.getInitialState())) {
                    setInitial(s);
                }
                if ((fst instanceof DFA || fst instanceof NFA) && fst.getOutputMap().get(i) == '1') {
                    s.setCircleStroke(COLOR_FINAL_STROKE);
                }
                initState(s);
            }

            for (String i : fst.getTransitionMap().keySet()) {
                if (fst instanceof NFA) {
                    State s0 = null;
                    String[] stateStrings = fst.getTransitionMap().get(i).split(",");
                    LinkedList<State> statelist = new LinkedList<>();
                    for (State j : stateSet) {
                        if (j.getName().equals(i.substring(0, i.length() - 1))) {
                            s0 = j;
                        }
                        for (String name : stateStrings) {
                            if (j.getName().equals(name)) {
                                statelist.add(j);
                            }
                        }
                    }
                    for (State k : statelist) {
                        Transition t = addTransition(s0, k, i.charAt(i.length() - 1));
                        Point p = loadControlPointMap.get(s0.getName() + "," + k.getName());
                        t.setControlPoint(p.x, p.y);
                    }

                } else {
                    State s0 = null, s1 = null;
                    for (State j : stateSet) {
                        if (j.getName().equals(i.substring(0, i.length() - 1))) {
                            s0 = j;
                        }
                        if (j.getName().equals(fst.getTransitionMap().get(i))) {
                            s1 = j;
                        }
                    }
                    if (fst instanceof MealyMachine) {
                        Transition t = addMealyTransition(s0, s1, i.charAt(i.length() - 1), fst.getOutputMap().get(i));
                        Point p = loadControlPointMap.get(s0.getName() + "," + s1.getName());
                        t.setControlPoint(p.x, p.y);
                    } else {
                        Transition t = addTransition(s0, s1, i.charAt(i.length() - 1));
                        Point p = loadControlPointMap.get(s0.getName() + "," + s1.getName());
                        t.setControlPoint(p.x, p.y);
                    }
                }
            }



        }
        catch (StreamCorruptedException e){
            new Alert(Alert.AlertType.ERROR,"File corrupt or incompatible",ButtonType.OK).showAndWait();
            throw e;
        }
        catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Unknown error",ButtonType.OK).showAndWait();
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }

    private Transition addTransition(State s0, State s1, char symbol) {
        Transition trans = null;
        if (isConnected(s0, s1)) {
            for (Transition t : transitionSet) {
                if (t.getS0() == s0 && t.getS1() == s1) trans = t;
            }
        } else {
            trans = new Transition(s0, s1);
        }
        transitionSet.add(trans);
        if (!drawPane.getChildren().contains(trans))
            drawPane.getChildren().add(trans);
        trans.addSymbol(symbol);
        trans.updateLabel();
        initTransition(trans);
        trans.toBack();
        return trans;

    }

    private Transition addMealyTransition(State s0, State s1, char symbol, char output) {
        MealyTransition trans = null;
        if (isConnected(s0, s1)) {
            for (Transition t : transitionSet) {
                if (t.getS0() == s0 && t.getS1() == s1) trans = (MealyTransition) t;
            }
        } else {
            trans = new MealyTransition(s0, s1);
        }
        trans.addSymbolOutput(symbol, output);
        transitionSet.add(trans);
        drawPane.getChildren().add(trans);
        trans.addSymbol(symbol);
        trans.updateLabel();
        initTransition(trans);
        trans.toBack();
        return trans;
    }

    private boolean isConnected(State s0, State s1) {
        for (Transition t : transitionSet) {
            if ((t.getS0().getName().equals(s0.getName())) && (t.getS1().getName().equals(s1.getName()))) {
                return true;
            }
        }
        return false;
    }

    private void initTransition(Transition trans) {
        trans.setOnMouseClicked(e -> {
            if (mode == SELECT) {
                if (selected != null) {
                    if (selected instanceof State) {
                        ((State) selected).setCircleFill(COLOR_NOT_SELECTED);
                    } else if (selected instanceof Transition) {
                        ((Transition) selected).setFill(COLOR_TRANSITION_NOT_SELECTED);
                    }
                }
                selected = (Transition) e.getSource();
                ((Transition) selected).setFill(COLOR_TRANSITION_SELECTED);
            } else if (mode == DELETE) {
                Transition deletedTransition = (Transition) e.getSource();
                //
                TextInputDialog deleteDialog = new TextInputDialog();

                deleteDialog.setTitle("delete transition");
                deleteDialog.setHeaderText("Enter a list or range of symbols to delete:");
                deleteDialog.setContentText("symbols:");

                Optional<String> deleteResult = deleteDialog.showAndWait();

                deleteResult.ifPresent(deleteString -> {
                    if (deleteString.matches("[A-Za-z]..[A-Za-z]|\\d..\\d")) {
                        char symbol0 = deleteString.charAt(0), symbol1 = deleteString.charAt(3);
                        if (symbol1 > symbol0)
                            for (char i = symbol0; i <= symbol1; i++) {
                                if (fst.getInputAlpha().contains(i)) {
                                    fst.deleteTransition(deletedTransition.getS0().getName(), i, deletedTransition.getS1().getName());
                                    deletedTransition.removeSymbol(i);
                                }

                            }
                        else
                            for (char i = symbol1; i <= symbol0; i++) {
                                if (fst.getInputAlpha().contains(i)) {
                                    fst.deleteTransition(deletedTransition.getS0().getName(), i, deletedTransition.getS1().getName());
                                    deletedTransition.removeSymbol(i);

                                }
                            }
                    } else {
                        String[] deleteTemp = deleteString.split("\\s+|,");//one or more spaces or a comma
                        for (int i = 0; i < deleteTemp.length; i++) {
                            if (fst.getInputAlpha().contains(deleteTemp[i].charAt(0))) {
                                fst.deleteTransition(deletedTransition.getS0().getName(), deleteTemp[i].charAt(0), deletedTransition.getS1().getName());
                                deletedTransition.removeSymbol(deleteTemp[i].charAt(0));

                            }

                        }
                    }
                    if (deletedTransition.getSymbols().size() == 0) {
                        drawPane.getChildren().remove(deletedTransition);
                    }
                    //add the newly added symbols to the input alpha label
                    Object[] sorted = fst.getInputAlpha().toArray();
                    Arrays.sort(sorted);
                    inputAlphaLabel.setText(Arrays.toString(sorted));

                });
                deletedTransition.updateLabel();
            }
        });


    }

    private State addState(double x, double y, String name) {
        State s = new State(x, y);
        s.setName(name);
        stateSet.add(s);
        s.setLableFill(COLOR_TEXT_SELECTED);
        initState(s);
        s.setCircleFill(COLOR_NOT_SELECTED);
        drawPane.getChildren().add(s);
        return s;
    }

    private MooreState addMooreState(double x, double y, String name, char output) {
        MooreState s = new MooreState(x, y, output);
        s.setName(name);
        stateSet.add(s);
        s.setLableFill(COLOR_TEXT_SELECTED);
        initState(s);
        s.setCircleFill(COLOR_NOT_SELECTED);
        drawPane.getChildren().add(s);
        return s;
    }

    private void initState(State s) {
        s.setOnMousePressed((e) -> {
            State temp = (State) e.getSource();
            if (mode != ADD) {

                if (selected != null) {
                    if (selected instanceof State) {
                        ((State) selected).setCircleFill(COLOR_NOT_SELECTED);
                    } else if (selected instanceof Transition) {
                        ((Transition) selected).setFill(COLOR_TRANSITION_NOT_SELECTED);
                    }
                }
                selected = temp;
                ((State) selected).setCircleFill(COLOR_SELECTED);
                if (mode == INITIAL) {
                    setInitial(temp);
                }
                if (mode == FINAL) {
                    setFinal(temp);
                }
                if (mode == DELETE) {
                    deleteState(temp);
                }
            }
        });
        s.setOnMouseDragged((e) -> {
            State temp = (State) e.getSource();
            if (mode == SELECT) {
                temp.setCenterX(e.getSceneX() - toolBox.getWidth());
                temp.setCenterY(e.getSceneY() - menuBar.getHeight());
                for (Transition t : transitionSet) {
                    if (t.getS0() == s || t.getS1() == s) {
                        t.update();
                    }
                }
            } else if (mode == CONNECT) {
                connectingTransition.setVisible(true);
                connectingTransition.setStart(temp.getCenterX(), temp.getCenterY());
                connectingTransition.update();
                connectingTransition.setEnd(e.getSceneX() - toolBox.getWidth(), e.getSceneY() - menuBar.getHeight());
                connecting = true;
            }
        });
        s.setOnMouseReleased((e) -> {
            State temp = (State) e.getSource();
            if (connecting) {
                connecting = false;
                //remove the connecting initStateArrow from the screen
                connectingTransition.setStart(-100, -100);
                connectingTransition.setEnd(-100, -100);
                connectingTransition.setVisible(false);
                //the MouseEntered event does not fire while dragging and fires after the MouseReleased
                //so the hovered variable would be null and cannot be used here
                State connectTo = null;
                for (State i : stateSet) {
                    if (dist(i.getCenterX(), i.getCenterY(), e.getSceneX() - toolBox.getWidth(), e.getSceneY() - menuBar.getHeight()) < State.R) {
                        connectTo = i;
                    }
                }
                if (connectTo != null) {
                    addTransition(temp, connectTo);
                }
            }
        });
        s.setOnMouseEntered((e) -> {
            State temp = (State) e.getSource();
            if (temp != selected)
                temp.setCircleFill(COLOR_HOVER_STATE);
            hovered = temp;
        });
        s.setOnMouseExited((e) -> {
            State temp = (State) e.getSource();
            hovered = null;
            if (temp == selected) {
                temp.setCircleFill(COLOR_SELECTED);
            } else {
                temp.setCircleFill(COLOR_NOT_SELECTED);
            }
        });
    }

    @FXML
    public void newFile(Event event) {
        try {
            if(saveChangesDialog())
            WindowLoader.loadStartWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openFile(Event event) {
        try {
            if(saveChangesDialog()) {
                String path = WindowLoader.showFileChooserDialog((Stage) drawPane.getScene().getWindow());
                WindowLoader.loadMainWindowWithPath(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit(Event event){
        saveChangesDialog();
    }
    boolean saveChangesDialog(){
        //returns false if no value is the user canceled
        Alert onQuitAlert = new Alert(Alert.AlertType.CONFIRMATION,"Save changes?",
                ButtonType.YES,ButtonType.NO);
        onQuitAlert.setTitle("save changes");
        Optional<ButtonType> res = onQuitAlert.showAndWait();
        if(res.isPresent()) {
            if (res.get() == ButtonType.YES) {
                if (openPath != null) {
                    try {
                        save(openPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    saveAsDialog(null);
                }
                return true;
            } else if (res.get() == ButtonType.NO) {
                return true;
            }
        }

            return false;
    }
}

