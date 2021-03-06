package Visual;
import java.util.HashMap;

public class MealyTransition extends Transition{
    private HashMap<Character,Character> symbolsMap = new HashMap<>();
    public MealyTransition(State s0, State s1) {
        super(s0, s1);
    }
    public void addSymbolOutput(char symbol,char output) {
        symbols.add(symbol);
        symbolsMap.put(symbol,output);
        symbolsLabel.setText(symbols.toString());
    }
    public HashMap<Character,Character> getMap(){return symbolsMap;}
    public void updateLabel(){
        String out = symbolsMap.toString();
        out = out.replace('=',':');
        symbolsLabel.setText(out);
    }
    public void removeSymbol(char symbol){
        symbolsMap.remove(symbol);
    }

    @Override
    public String toString() {
        return s0.getName()+s1.getName()+symbolsMap.toString();
    }
}
