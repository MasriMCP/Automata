package Visual;

import java.util.HashMap;
import java.util.HashSet;

public class MealyTransition extends Transition{
    private HashMap<Character,Character> symbolsMap = new HashMap<>();
    public MealyTransition(State s0, State s1) {
        super(s0, s1);
    }
    public void addSymbolOutput(char symbol,char output) {
        symbolsMap.put(symbol,output);
        symbolsLabel.setText(symbols.toString());
    }
    public HashMap<Character,Character> getMap(){return symbolsMap;}
    public void updateLabel(){
        symbolsLabel.setText(symbolsMap.toString());
    }
}
