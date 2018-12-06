package Auto;

import Visual.State;
import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.DataFormatException;

public abstract class FiniteStateTransducer implements Serializable {
    protected HashSet<String> states;//Q
    protected HashSet<Character> inputAlpha;//sigma
    protected HashSet<Character> outputAlpha;//gamma
    protected HashMap<String, String> transitionMap;//delta
    protected String initialState;//q0
    protected HashMap<String, Character> outputMap;//theta
    private String desc;
    private String name;
    private String type;

    FiniteStateTransducer() {
        states = new HashSet<>();
        transitionMap = new HashMap<>();
        inputAlpha = new HashSet<>();
        outputAlpha = new HashSet<>();
        outputMap = new HashMap<>();
        if (this instanceof MealyMachine) {
            type = "mea";
        } else if (this instanceof DFA) {
            type = "dfa";
        } else if (this instanceof NFA) {
            type = "nfa";
        } else if (this instanceof MooreMachine) {
            type = "mor";
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public FiniteStateTransducer setInitialState(String state) {
        if (!states.contains(state)) throw new IllegalArgumentException("no such state: " + state);
        initialState = state;
        return this;
    }

    public FiniteStateTransducer addInputAlphaRange(char symbol0, char symbol1) {
        for (char i = symbol0; i <= symbol1; i++) {
            addInputAlpha(i);
        }
        return this;
    }

    public FiniteStateTransducer addInputAlpha(char symbol) {
        inputAlpha.add(symbol);
        return this;
    }

    public FiniteStateTransducer addOutputAlphaRange(char symbol0, char symbol1) {
        for (char i = symbol0; i <= symbol1; i++) {
            addOutputAlpha(i);
        }
        return this;
    }

    public FiniteStateTransducer addOutputAlpha(char symbol) {
        outputAlpha.add(symbol);
        return this;
    }

    public FiniteStateTransducer addState(String state) {
        states.add(state);
        return this;
    }

    public FiniteStateTransducer addTransition(String state0, char symbol, String state1) {
        if (!states.contains(state0)) throw new IllegalArgumentException("no such state: " + state0);
        if (!states.contains(state1)) throw new IllegalArgumentException("no such state: " + state1);
        if (!inputAlpha.contains(symbol)) throw new IllegalArgumentException("no such symbol: " + symbol);

        transitionMap.put(state0 + String.valueOf(symbol), state1);
        return this;
    }

    public FiniteStateTransducer addTransitionRange(String state0, char symbol0, String state1, char symbol1){
        if(symbol1>symbol0)
            for (char i = symbol0; i <= symbol1; i++) {
                    addTransition(state0, i, state1);

            }
        else
            for (char i = symbol1; i <= symbol0; i++) {
                    addTransition(state0, i, state1);

            }
        return this;
    }
    public void save(String path) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(this);
        out.close();
    }
    public static FiniteStateTransducer load(String path) throws IOException {
        FiniteStateTransducer ret = null;
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
        try {
            ret = (FiniteStateTransducer)in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;//TODO this
    }



    public abstract String run(String input);

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("states", states);
        obj.put("input alpha", inputAlpha);
        obj.put("output alpha", outputAlpha);
        obj.put("transition map", transitionMap);
        obj.put("initial state", initialState);
        obj.put("output map", outputMap);
        obj.put("description", desc);
        obj.put("name", name);
        obj.put("type", type);
        return obj.toString();
    }

    public void addDescription(String desc) {
        this.desc = desc;
    }

    public String describe() {
        return desc;
    }

    protected boolean isTotal() {
        if (transitionMap.size() != inputAlpha.size() * states.size()) return false;
        else {
            for (String s : states) {
                for (char c : inputAlpha) {
                    if (!transitionMap.containsKey(s + String.valueOf(c))) return false;
                }
            }
        }
        return true;
    }

    public HashSet<Character> getInputAlpha() {

        return inputAlpha;
    }

    public boolean isConnected(String s0, String s1) {
        for (char c : inputAlpha) {
            if (s1.equals(transitionMap.get(s0 + String.valueOf(c)))) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, Character> getOutputMap() {
        return outputMap;
    }

    public void delete(String state) {
        if (state.equals(initialState)) {
            initialState = null;
        }
        outputMap.remove(state);
        states.remove(state);
        HashSet<String> temp = new HashSet<>();

        for (String s : transitionMap.keySet()) {
            //since two states can start with the same substring and a state name+symbol combination
            // can match another state name, we test that s starts with state AND is longer by 1 (the symbol char)
            if ((s.startsWith(state) && s.length() == state.length() + 1) || transitionMap.get(s).equals(state))
                temp.add(s);
        }
        for (String s : temp) {
            transitionMap.remove(s);
        }

    }
    public void deleteInitialState(){
        initialState = null;
    }

    public void deleteTransition(String s0,char symbol,String s1){
        transitionMap.remove(s0+String.valueOf(symbol));
    }

    public String getType() {
        return type;
    }

    public String getInitialState() {
        return initialState;
    }

    public HashSet<Character> getOutputAlpha() {
        return outputAlpha;
    }

    private void setStates(HashSet<String> states) {
        this.states = states;
    }

    private void setInputAlpha(HashSet<Character> inputAlpha) {
        this.inputAlpha = inputAlpha;
    }

    private void setOutputAlpha(HashSet<Character> outputAlpha) {
        this.outputAlpha = outputAlpha;
    }

    private void setTransitionMap(HashMap<String, String> transitionMap) {
        this.transitionMap = transitionMap;
    }

    private void setOutputMap(HashMap<String, Character> outputMap) {
        this.outputMap = outputMap;
    }
}
