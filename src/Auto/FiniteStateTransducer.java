package Auto;

import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public abstract class FiniteStateTransducer implements Serializable {
    protected HashSet<String> states;//Q
    protected HashSet<Character> inputAlpha;//sigma
    protected HashSet<Character> outputAlpha;//gamma
    protected HashMap<String,String> transitionMap;//delta
    protected String initialState;//q0
    protected HashMap<String,Character> outputMap;//theta
    private String desc;
    private String name;
    private String type;

    FiniteStateTransducer(){
        states = new HashSet<>();
        transitionMap = new HashMap<>();
        inputAlpha = new HashSet<Character>();
        outputAlpha = new HashSet<>();
        if(this instanceof MealyMachine){
            type = "mea";
        }
        else if(this instanceof DFA){
            type = "dfa";
        }
        else if(this instanceof NFA){
            type = "nfa";
        }
        else if(this instanceof MooreMachine){
            type = "mor";
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public FiniteStateTransducer setInitialState(String state){
        if(!states.contains(state)) throw new IllegalArgumentException("no such state: "+state);
        initialState = state;
        return this;
    }
    public FiniteStateTransducer addInputAlphaRange(char symbol0,char symbol1){
        for(char i=symbol0;i<=symbol1;i++){
            addInputAlpha(i);
        }
        return this;
    }
    public FiniteStateTransducer addInputAlpha(char symbol){
        inputAlpha.add(symbol);
        return this;
    }
    public FiniteStateTransducer addOutputAlphaRange(char symbol0,char symbol1){
        for(char i=symbol0;i<=symbol1;i++){
            addOutputAlpha(i);
        }
        return this;
    }
    public FiniteStateTransducer addOutputAlpha(char symbol){
        outputAlpha.add(symbol);
        return this;
    }
    public FiniteStateTransducer addState(String state) {
        states.add(state);
        return this;
    }
    public FiniteStateTransducer addTransition(String state0,char symbol,String state1){
        if(!states.contains(state0)) throw new IllegalArgumentException("no such state: "+state0);
        if(!states.contains(state1)) throw new IllegalArgumentException("no such state: "+state1);
        if(!inputAlpha.contains(symbol)) throw new IllegalArgumentException("no such symbol: "+symbol);

        transitionMap.put(state0+String.valueOf(symbol),state1);
        return this;
    }
    public FiniteStateTransducer addTransitionRange(String state0,char symbol0,String state1,char symbol1){
        for(char i=symbol0;i<=symbol1;i++){
            addTransition(state0,i,state1);
        }
        return this;
    }
    public static FiniteStateTransducer load(String path) throws IOException {
        FiniteStateTransducer ret=null;
        JSONParser parser = new JSONParser();
        try {

            parser.parse(path);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;//TODO this
    }
    public void save(){
        String path = name+"."+type;
        try( BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)))){
            writer.write(this.toString());
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public abstract String run(String input);
    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("states",states);
        obj.put("input alpha",inputAlpha);
        obj.put("output alpha",outputAlpha);
        obj.put("transition map",transitionMap);
        obj.put("initial state",initialState);
        obj.put("output map",outputMap);
        obj.put("description",desc);
        obj.put("name",name);
        obj.put("type",type);
        return obj.toString();
    }
    public void addDescription(String desc)
    {
        this.desc = desc;
    }
    public String describe(){
        return desc;
    }
    protected boolean isTotal(){
        if(transitionMap.size()!=inputAlpha.size()*states.size()) return false;
        else{
            for(String s:states){
                for(char c:inputAlpha){
                    if(!transitionMap.containsKey(s+String.valueOf(c))) return false;
                }
            }
        }
        return true;
    }

    public HashSet<Character> getInputAlpha() {
        return inputAlpha;
    }
}
