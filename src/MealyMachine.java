import java.io.*;
import java.util.HashMap;
public class MealyMachine extends FiniteStateTransducer{
    MealyMachine(){
        super();
        outputMap = new HashMap<>();
    }
    public MealyMachine addTransitionOutput(String state0,char symbol,String state1,char output){
        if(!outputAlpha.contains(output)) throw new IllegalArgumentException("no such symbol: "+output);
        addTransition(state0,symbol,state1);
        outputMap.put(state0+String.valueOf(symbol),output);
        return this;
    }
    @Override
    public String run(String input) {
        if(!isTotal()) throw new IllegalStateException("not a total function");
        String currentState = initialState;
        StringBuilder ret = new StringBuilder("");
        for(int i=0;i<input.length();i++){
            char temp = input.charAt(i);
            ret.append(outputMap.get(currentState+String.valueOf(temp)));
            currentState = transitionMap.get(currentState+String.valueOf(temp));
        }
        return ret.toString();
    }

}
