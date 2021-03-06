package Auto;

import java.util.HashSet;

public class NFA extends MooreMachine {
    public static final char LAMBDA = 0x03BB;

    public NFA() {
        super();
        outputAlpha.add('0');
        outputAlpha.add('1');
    }

    public NFA addState(String state) {
        super.addStateOutput(state, '0');
        return this;
    }

    public NFA setFinalState(String... fStates) {
        for (String state : fStates) {
            if (!states.contains(state)) throw new IllegalArgumentException("no such state: " + state);
            outputMap.put(state, '1');
        }
        return this;
    }
    public void deleteFinalState(String state){
        outputMap.put(state,'0');
    }

    public NFA addTransition(String state0, char symbol, String state1) {
        if (!states.contains(state0)) throw new IllegalArgumentException("no such state: " + state0);
        if (!states.contains(state1)) throw new IllegalArgumentException("no such state: " + state1);
        if (!inputAlpha.contains(symbol)) throw new IllegalArgumentException("no such symbol: " + symbol);
        String k = state0 + String.valueOf(symbol);
        if (!transitionMap.containsKey(k))
            transitionMap.put(k, state1);
        else {
            transitionMap.put(k, transitionMap.get(k) + "," + state1);
        }
        return this;
    }

    public boolean isAccepted(String input) {
        return run(input, initialState,new HashSet<String>());
    }

    private boolean run(String input, String state,HashSet<String> searched) {
        boolean ret = false;//the return value
        for(String s:getLambdaClosure(state)){
            if(!searched.contains(s)){
                searched.add(s);
                ret = ret || run(input, s, searched);
            }


        }
        if (input.length() == 0) {
            //if the input length == 0 then there are no more possible transitions and we need to check
            //if this is a final state.
            return outputMap.get(state) == '1'||ret;
        }

        if (transitionMap.get(state + input.charAt(0)) == null) return ret;
        String[] states = transitionMap.get(state + input.charAt(0)).split(",");
        for (String s : states) {
            //for each state
            //if any of the branches can reach a final state ret will be true;
            HashSet<String> recSearched = new HashSet<>();
            ret = ret || run(input.substring(1, input.length()), s,recSearched);
        }

        return ret;
    }

    private HashSet<String> getLambdaClosure(String state) {
        HashSet<String> ret = getLambdaClosure(state, new HashSet<>());
        return ret;
    }

    private HashSet<String> getLambdaClosure(String state, HashSet<String> set) {
        if (set.contains(state)) return set;
        set.add(state);
        if (transitionMap.get(state + LAMBDA) == null) return set;
        String[] states = transitionMap.get(state + LAMBDA).split(",");
        for (String s : states) {
            if (!set.contains(s)) {
                set.addAll(getLambdaClosure(s, set));
            }
        }
        return set;
    }

    public void deleteTransition(String s0,char symbol,String s1){
        //TODO not done yet!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        StringBuilder temp = new StringBuilder(transitionMap.get(s0+String.valueOf(symbol)));
        int index0 = temp.indexOf(s1);
        int index1=temp.indexOf(",",index0)+1;
        if(index1==0) index1=temp.length();
        temp.delete(index0,index1);
        if(temp.length()==0){
            transitionMap.remove(s0+String.valueOf(symbol));
        }
        else
            transitionMap.put(s0+String.valueOf(symbol),temp.toString());

    }
}
