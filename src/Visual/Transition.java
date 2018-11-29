package Visual;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class Transition extends Group {
    private Line l0 = new Line();//main line
    private final static int ARROW_HEAD_LENGTH = 20, STROKE_WIDTH = 3;
    private final static double ARROW_HEAD_ANGLE = 3 * Math.PI / 4;
    private HashSet<Character> symbols = new HashSet<>();
    private Label symbolsLabel = new Label();
    private State s0, s1;
    private ImageView arrowHead;
    private final static int ARROW_HEAD_SIZE=15;

    public Transition(State s0, State s1) {
        try {
             arrowHead = new ImageView(new Image(new FileInputStream("Resources/images/arrowhead.png")));
            getChildren().addAll(l0,symbolsLabel,arrowHead);
            arrowHead.setFitWidth(ARROW_HEAD_SIZE);
            arrowHead.setFitHeight(ARROW_HEAD_SIZE);
            arrowHead.rotateProperty().bind(lineRotateProperty());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(!(s0==null&&s1==null)) {
            this.s0 = s0;
            this.s1 = s1;
            l0.startXProperty().bind(s0.layoutXProperty().add(State.R));
            l0.startYProperty().bind(s0.layoutYProperty().add(State.R));
            l0.endXProperty().bind(s1.layoutXProperty().add(State.R));
            l0.endYProperty().bind(s1.layoutYProperty().add(State.R));
            int sign = s0.getName().compareTo(s1.getName());
            if (sign != 0)
                sign = Math.abs(sign) / sign;
            l0.setFill(Color.TRANSPARENT);
            l0.setStrokeWidth(STROKE_WIDTH);
            symbolsLabel.layoutXProperty().bind(s1.layoutXProperty().add(s0.layoutXProperty()).divide(2));
            symbolsLabel.layoutXProperty().bind(s1.layoutYProperty().add(s0.layoutYProperty()).divide(2));

            symbolsLabel.setText(symbols.toString());

        }
    }

    public void setStart(double x, double y) {
        l0.setStartX(x);
        l0.setStartY(y);



    }

    public void setEnd(double x, double y) {
        l0.setEndX(x);
        l0.setEndY(y);
        arrowHead.setX(x-ARROW_HEAD_SIZE/2);
        arrowHead.setY(y-ARROW_HEAD_SIZE/2);
    }

    public void setFill(Paint p) {
        l0.setStroke(p);
    }

    public void addSymbolRange(char symbol0, char symbol1) {
        for (char i = symbol0; i <= symbol1; i++) {
            addSymbol(i);
        }
    }

    public void addSymbol(char symbol) {
        symbols.add(symbol);
        symbolsLabel.setText(symbols.toString());
    }

    public HashSet<Character> getSymbols() {
        return symbols;
    }

    public State getS0() {
        return s0;
    }

    public State getS1() {
        return s1;
    }
    private DoubleProperty lineRotateProperty(){
        double ret = 0;
        if(l0.getEndX()>l0.getStartX())
            ret = (180/ Math.PI*Math.atan((l0.getEndY()-l0.getStartY())/(l0.getEndX()-l0.getStartX())))+90;
        else if(l0.getEndX()<l0.getStartX())
           ret =(180/ Math.PI*Math.atan((l0.getEndY()-l0.getStartY())/(l0.getEndX()-l0.getStartX())))+270;
        else{
            if(l0.getEndY()>l0.getStartY())ret = 180;
            else ret = 0;
        }
        System.out.println(ret);
        return new SimpleDoubleProperty(180);
    }
}
