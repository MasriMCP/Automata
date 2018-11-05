import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        NFA m = new NFA();
        m.addState("a")
                .addState("b")
                .addState("c")
                .addState("d")
                .addState("e");
        m.addInputAlphaRange('a','c');
        m.addTransition("a",'a',"b")
                .addTransition("b",'b',"c")
                .addTransition("c",'a',"c")
                .addTransition("c",'b',"c")
                .addTransition("c",'c',"c")
                .addTransition("c",'c',"d")
                .addTransition("d",'c',"e");
        m.setInitialState("a");
        m.setFinalState("e");
        System.out.println(m.isAccepted("abababaaacccccc"));
    }
    static void read(FiniteStateTransducer f){
        Scanner s = new Scanner(System.in);
        String b;
        while (true){
            b = s.nextLine();
            System.out.println(f.run(b));
        }
    }
}

