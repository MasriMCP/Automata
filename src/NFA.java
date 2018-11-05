public class NFA extends MooreMachine{
    NFA(){
        super();
        outputAlpha.add('0');
        outputAlpha.add('1');
    }
    public NFA addState(String state){
        super.addStateOutput(state,'0');
        return this;
    }
    public NFA setFinalState(String state){
        if(!states.contains(state)) throw new IllegalArgumentException("no such state: "+state);
        outputMap.put(state,'1');
        return this;
    }
    public NFA addTransition(String state0,char symbol,String state1){
        if(!states.contains(state0)) throw new IllegalArgumentException("no such state: "+state0);
        if(!states.contains(state1)) throw new IllegalArgumentException("no such state: "+state1);
        if(!inputAlpha.contains(symbol)) throw new IllegalArgumentException("no such symbol: "+symbol);
        String k = state0+String.valueOf(symbol);
        if(!transitionMap.containsKey(k))
        transitionMap.put(k,state1);
        else{
            transitionMap.put(k,transitionMap.get(k)+","+state1);
        }
        return this;
    }
    public boolean isAccepted(String input){
        return run(input,initialState);
    }

    private boolean run(String input,String state){
        if(input.length()==0){
            //if the input length == 0 then there are no more possible transitions and we need to check
            //if this is a final state.
            return outputMap.get(state)=='1';
        }
        boolean ret = false;//the return value
        try{
            //states is an array of the possible states we can transition to
            String[] states  = transitionMap.get(state+input.charAt(0)).split(",");
        }
        catch (NullPointerException ex){
            //if the transition is null that means there are no more possible transitions for this branch
            //and the result is false
            return false;
        }
        for(String s:states){
            //for each state
            //if any of the branches can reach a final state ret will be true;
            ret = ret || run(input.substring(1,input.length()),s);
        }
        return ret;
    }

}
