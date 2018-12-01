package Visual;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class Transition extends Group {
    private CubicCurve l0 = new CubicCurve();//main line
    private final static int STROKE_WIDTH = 3;
    private HashSet<Character> symbols = new HashSet<>();
    private Label symbolsLabel = new Label();
    private State s0, s1;
    private ImageView arrowHead;
    private final static int ARROW_HEAD_SIZE=15;
    public Transition(State s0, State s1) {
        try {
             arrowHead = new ImageView(new Image(new FileInputStream("Resources/images/arrowhead.png")));
            getChildren().addAll(l0,symbolsLabel);
            arrowHead.setFitWidth(ARROW_HEAD_SIZE);
            arrowHead.setFitHeight(ARROW_HEAD_SIZE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        l0.setStroke(Color.ORANGE);
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
            double angle = Math.atan((s1.getCenterY()-s0.getCenterY())/(s1.getCenterX()-s0.getCenterX()));
            l0.controlX1Property().bind(l0.startXProperty().add(l0.endXProperty()).divide(2).add(50*Math.cos(angle)*sign));
            l0.controlY1Property().bind(l0.startYProperty().add(l0.endYProperty()).divide(2).add(50*Math.sin(angle)*sign));
            l0.controlX2Property().bind(l0.startXProperty().add(l0.endXProperty()).divide(2).add(50*Math.cos(angle)*sign));
            l0.controlY2Property().bind(l0.startYProperty().add(l0.endYProperty()).divide(2).add(50*Math.sin(angle)*sign));
            l0.setFill(null);

            l0.setStrokeWidth(STROKE_WIDTH);
            symbolsLabel.layoutXProperty().bind(l0.controlX1Property().subtract(symbolsLabel.widthProperty().divide(2)));
            symbolsLabel.layoutYProperty().bind(l0.controlY1Property());
            arrowHead.xProperty().bind(l0.endXProperty());
            arrowHead.yProperty().bind(l0.endYProperty());
            arrowHead.rotateProperty().bind(l0.rotateProperty());
            symbolsLabel.setText(symbols.toString());

        }
    }

    public void setStart(double x, double y) {
        l0.setStartX(x);
        l0.setStartY(y);
        l0.setControlX1((l0.getStartX()+l0.getEndX())/2);
        l0.setControlY1((l0.getStartY()+l0.getEndY())/2);
        l0.setControlX2((l0.getStartX()+l0.getEndX())/2);
        l0.setControlY2((l0.getStartY()+l0.getEndY())/2);
    }

    public void setEnd(double x, double y) {
        l0.setEndX(x);
        l0.setEndY(y);
        arrowHead.setX(x-ARROW_HEAD_SIZE/2);
        arrowHead.setY(y-ARROW_HEAD_SIZE/2);
        l0.setControlX1((l0.getStartX()+l0.getEndX())/2);
        l0.setControlY1((l0.getStartY()+l0.getEndY())/2);
        l0.setControlX2((l0.getStartX()+l0.getEndX())/2);
        l0.setControlY2((l0.getStartY()+l0.getEndY())/2);
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
    /*private SimpleDoubleProperty rotationProperty(){
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
    }*/
    public void updateLabel(){
        symbolsLabel.setText(symbols.toString());
    }

}
