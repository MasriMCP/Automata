package Visual;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashSet;

public class Transition extends Group implements Serializable {
    private CubicCurve l0 = new CubicCurve();//main line
    private final static int STROKE_WIDTH = 3;
    protected HashSet<Character> symbols = new HashSet<>();
    protected Label symbolsLabel = new Label();
    private State s0, s1;
    private Circle controlPoint;//(not) used to control the curve's control points
    private Path arrowEnd=new Path();
    public Transition(State s0, State s1) {
            controlPoint = new Circle();
            controlPoint.setFill(Color.BLACK);
            controlPoint.setRadius(5);
            getChildren().addAll(l0,symbolsLabel,controlPoint,arrowEnd);
        controlPoint.setFill(Color.rgb(0,0,0,0));
        l0.setStroke(Color.ORANGE);
        arrowEnd.setStroke(Color.ORANGE);
        arrowEnd.setStrokeWidth(STROKE_WIDTH);
        if(!(s0==null&&s1==null)) {
            this.s0 = s0;
            this.s1 = s1;
            l0.startXProperty().bind(s0.layoutXProperty().add(State.R));
            l0.startYProperty().bind(s0.layoutYProperty().add(State.R));
            l0.endXProperty().bind(s1.layoutXProperty().add(State.R));
            l0.endYProperty().bind(s1.layoutYProperty().add(State.R));

            int sign = s0.getName().compareTo(s1.getName());
            if(sign==0){
               l0.controlX1Property().bind(l0.startXProperty().subtract(50));
               l0.controlY1Property().bind(l0.startYProperty().subtract(50));
               l0.controlX2Property().bind(l0.startXProperty().add(50));
               l0.controlY2Property().bind(l0.startYProperty().subtract(50));
                symbolsLabel.layoutXProperty().bind(l0.startXProperty().subtract(symbolsLabel.widthProperty().divide(2)));
                symbolsLabel.layoutYProperty().bind(l0.startYProperty().subtract(50));
            }
            else{
                sign = Math.abs(sign) / sign;
                double angle = Math.atan((s1.getCenterY()-s0.getCenterY())/(s1.getCenterX()-s0.getCenterX()));
                controlPoint.centerXProperty().bind(l0.startXProperty().add(l0.endXProperty()).divide(2).add(50*Math.cos(angle+Math.PI/4)*sign));
                controlPoint.centerYProperty().bind(l0.startYProperty().add(l0.endYProperty()).divide(2).add(50*Math.sin(angle+Math.PI/4)*sign));
                l0.controlX1Property().bind(controlPoint.centerXProperty());
                l0.controlY1Property().bind(controlPoint.centerYProperty());
                l0.controlX2Property().bind(controlPoint.centerXProperty());
                l0.controlY2Property().bind(controlPoint.centerYProperty());
                symbolsLabel.layoutXProperty().bind(l0.controlX1Property().subtract(symbolsLabel.widthProperty().divide(2)));
                symbolsLabel.layoutYProperty().bind(l0.controlY1Property());
            }
            l0.setFill(null);
            l0.setStrokeWidth(STROKE_WIDTH);
            symbolsLabel.setText(symbols.toString());
            controlPoint.setOnMouseDragged(e->{
                controlPoint.centerXProperty().unbind();
                controlPoint.centerYProperty().unbind();
                controlPoint.setCenterX(e.getX());
                controlPoint.setCenterY(e.getY());
                update();
            });

            controlPoint.setOnMouseEntered(e->{
                controlPoint.setFill(Color.rgb(0,0,0,1));
            });
            controlPoint.setOnMouseExited(e->{
                controlPoint.setFill(Color.rgb(0,0,0,0));
            });


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
        l0.setControlX1((l0.getStartX()+l0.getEndX())/2);
        l0.setControlY1((l0.getStartY()+l0.getEndY())/2);
        l0.setControlX2((l0.getStartX()+l0.getEndX())/2);
        l0.setControlY2((l0.getStartY()+l0.getEndY())/2);
    }

    public void setFill(Paint p) {
        l0.setStroke(p);
        arrowEnd.setStroke(p);
    }

    public void addSymbolRange(char symbol0, char symbol1) {
        for (char i = symbol0; i <= symbol1; i++) {
            addSymbol(i);
        }
    }

    public void addSymbol(char symbol) {
        symbols.add(symbol);
    }
    public void removeSymbol(char symbol){
        symbols.remove(symbol);
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
    public void setControlPoint(double x,double y){
        controlPoint.centerXProperty().unbind();
        controlPoint.centerYProperty().unbind();
        controlPoint.setCenterX(x);
        controlPoint.setCenterY(y);
        update();

    }
    public Point getControlPoint(){
        return new Point((int)controlPoint.getCenterX(),(int)controlPoint.getCenterY());
    }
    public void update(){
        arrowEnd.getElements().removeAll(arrowEnd.getElements());
        double size=Math.max(l0.getBoundsInLocal().getWidth(),
                l0.getBoundsInLocal().getHeight());
        double scale=50;
        Point2D ori=eval(l0,.9f);
        Point2D tan=evalDt(l0,9f).normalize().multiply(scale);
        arrowEnd.getElements().add(new MoveTo(ori.getX()-0.2*tan.getX()-0.2*tan.getY(),
                ori.getY()-0.2*tan.getY()+0.2*tan.getX()));
        arrowEnd.getElements().add(new LineTo(ori.getX(), ori.getY()));
        arrowEnd.getElements().add(new LineTo(ori.getX()-0.2*tan.getX()+0.2*tan.getY(),
                ori.getY()-0.2*tan.getY()-0.2*tan.getX()));

    }
    //stolen from stackoverflow
    private Point2D eval(CubicCurve c, float t){
        Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+
                3*t*Math.pow(1-t,2)*c.getControlX1()+
                3*(1-t)*t*t*c.getControlX2()+
                Math.pow(t, 3)*c.getEndX(),
                Math.pow(1-t,3)*c.getStartY()+
                        3*t*Math.pow(1-t, 2)*c.getControlY1()+
                        3*(1-t)*t*t*c.getControlY2()+
                        Math.pow(t, 3)*c.getEndY());
        return p;
    }
    private Point2D evalDt(CubicCurve c, float t){
        Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+
                3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlX1()+
                3*((1-t)*2*t-t*t)*c.getControlX2()+
                3*Math.pow(t, 2)*c.getEndX(),
                -3*Math.pow(1-t,2)*c.getStartY()+
                        3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlY1()+
                        3*((1-t)*2*t-t*t)*c.getControlY2()+
                        3*Math.pow(t, 2)*c.getEndY());
        return p;
    }
}
