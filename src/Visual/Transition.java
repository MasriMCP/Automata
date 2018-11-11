package Visual;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.awt.event.MouseEvent;
import java.util.HashSet;

public class Transition extends Group {
    private Line l0 = new Line();//main line
    private final Line l1 = new Line(),l2 = new Line();//arrow head
    private final static int ARROW_HEAD_LENGTH = 10;
    private final static double ARROW_HEAD_ANGLE=3*Math.PI/4;
    private HashSet<Character> symbols = new HashSet<>();
    public Transition(State s0,State s1){
        getChildren().addAll(l0,l1,l2);
        Circle c = new Circle();
        Line l = new Line();
        l.startXProperty().bind(c.centerXProperty());
        l0.startXProperty().bind(s0.layoutXProperty().add(State.R));
        l0.startYProperty().bind(s0.layoutYProperty().add(State.R));
        l0.endXProperty().bind(s1.layoutXProperty().add(State.R));
        l0.endYProperty().bind(s1.layoutYProperty().add(State.R));
        l1.endXProperty().bind(s1.layoutXProperty().add(State.R));
        l1.endYProperty().bind(s1.layoutYProperty().add(State.R));
        l2.endXProperty().bind(s1.layoutXProperty().add(State.R));
        l2.endYProperty().bind(s1.layoutYProperty().add(State.R));
        //stolen from Stackoverflow
        //https://stackoverflow.com/questions/41353685/how-to-draw-arrow-javafx-pane
        if (s1.getCenterX() == s0.getCenterX() && s1.getCenterX() == s0.getCenterX()) {
            // arrow parts of length 0
            l1.startXProperty().bind(s0.layoutXProperty().add(State.R));
            l1.startYProperty().bind(s0.layoutYProperty().add(State.R));
            l2.startXProperty().bind(s0.layoutXProperty().add(State.R));
            l2.startYProperty().bind(s0.layoutYProperty().add(State.R));
        } else {
            double x0 = s0.getCenterX(),x1 = s1.getCenterX(),
                    y0 = s0.getCenterY(),y1= s1.getCenterY();
            double factor = ARROW_HEAD_LENGTH / Math.hypot(x0-x1, y0-y1);
            double factorO = 5 / Math.hypot(x0-x1, y0-y1);

            // part in direction of main line
            double dx = (x0 - x1) * factor;
            double dy = (y0 - y1) * factor;

            // part ortogonal to main line
            double ox = (x0 - x1) * factorO;
            double oy = (y0 - y1) * factorO;

            l1.setStartX(x1 + dx - oy);
            l1.setStartY(y1 + dy + ox);
            l2.setStartX(x1 + dx + oy);
            l2.setStartY(y1 + dy - ox);
            l1.startXProperty().bind(s1.layoutXProperty().add(State.R).add(dx - oy));
            l1.startYProperty().bind(s1.layoutYProperty().add(State.R).add(dx + ox));
            l2.startXProperty().bind(s1.layoutXProperty().add(State.R).add(dx + oy));
            l2.startYProperty().bind(s1.layoutYProperty().add(State.R).add(dx - ox));
        }
    }
    public void setStart(double x,double y){
        l0.setStartX(x);
        l0.setStartY(y);
    }
    public void setEnd(double x,double y){
        l0.setEndX(x);
        l0.setEndY(y);
    }
    public void setFill(Paint p){
        l0.setStroke(p);
        l1.setStroke(p);
        l2.setStroke(p);
    }
    public void addSymbol(char symbol){
        symbols.add(symbol);
    }
    public HashSet<Character> getSymbols() {
        return symbols;
    }
}
