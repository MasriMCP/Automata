package Auto;

public class DFA extends MooreMachine {
    public DFA() {
        super();
        outputAlpha.add('0');
        outputAlpha.add('1');
    }

    public DFA addState(String state) {
        super.addStateOutput(state, '0');
        return this;
    }

    public DFA setFinalState(String... fStates) {
        for (String state : fStates) {
            if (!states.contains(state)) throw new IllegalArgumentException("no such state: " + state);
            outputMap.put(state, '1');
        }
        return this;
    }

    public boolean isAccepted(String input) {
        return run(input).charAt(input.length()) == '1';
    }
}
