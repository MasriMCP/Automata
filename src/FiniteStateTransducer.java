import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

abstract class FiniteStateTransducer implements Serializable {
    protected HashSet<String> states;//Q
    protected HashSet<Character> inputAlpha;//sigma
    protected HashSet<Character> outputAlpha;//gamma
    protected HashMap<String,String> transitionMap;//delta
    protected String initialState;//q0
    protected HashMap<String,Character> outputMap;//theta
    FiniteStateTransducer(){
        states = new HashSet<>();
        transitionMap = new HashMap<>();
        inputAlpha = new HashSet<>();
        outputAlpha = new HashSet<>();
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
    public static FiniteStateTransducer load(String path) throws IOException {
        ObjectInputStream obj = new ObjectInputStream(new FileInputStream(path));
        try{
            return (FiniteStateTransducer) obj.readObject();
        }
        catch (ClassNotFoundException ex){
            return null;
        }
        finally {
            obj.close();
        }
    }
    public void save(String path)throws IOException {
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(path));
        obj.writeObject(this);
        obj.close();
    }
    public abstract String run(String input);
    @Override
    public String toString() {
        return transitionMap.toString();
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
}
