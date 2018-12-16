package Auto;

import java.util.HashMap;

public class MooreMachine extends FiniteStateTransducer {
    public MooreMachine() {
        super();
    }

    public MooreMachine addStateOutput(String state, char output) {
        if (!outputAlpha.contains(output)) throw new IllegalArgumentException("no such symbol: " + output);
        states.add(state);
        outputMap.put(state, output);
        return this;
    }

    @Override
    public String run(String input) {
        if (!isTotal()) throw new IllegalStateException("not a total function");
        String currentState = initialState;
        StringBuilder ret = new StringBuilder("");
        ret.append(outputMap.get(currentState));
        for (int i = 0; i < input.length(); i++) {
            char temp = input.charAt(i);
            if(!inputAlpha.contains(temp)) throw new IllegalArgumentException("illegal symbol: "+String.valueOf(temp));
            currentState = transitionMap.get(currentState + String.valueOf(temp));
            ret.append(outputMap.get(currentState));
        }
        return ret.toString();
    }
}
