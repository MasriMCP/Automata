package Visual;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.util.HashMap;

public class State extends StackPane {
    public final static double R = 15;
    private static final double STROKE_WIDTH = 5;
    private Circle circle = new Circle();
    private Label nameLabel = new Label();

    public State(double x, double y) {
        setCenterX(x);
        setCenterY(y);
        circle.setRadius(R);
        getChildren().addAll(circle, nameLabel);

    }

    public String getName() {
        return nameLabel.getText();

    }

    public void setName(String name) {
        nameLabel.setText(name);
    }

    public void setCircleFill(Paint paint) {
        circle.setFill(paint);
    }
    public void setCircleStroke(Paint paint){
        if(paint==null){circle.setStrokeWidth(0);return;}
        circle.setStroke(paint);
        circle.setStrokeWidth(STROKE_WIDTH);
    }

    public void setLableFill(Paint paint) {
        nameLabel.setTextFill(paint);
    }

    public void setCenterX(double value) {
        setLayoutX(value - R);
    }

    public void setCenterY(double value) {
        setLayoutY(value - R);
    }

    public double getCenterX() {
        return getLayoutX() + R;
    }

    public double getCenterY() {
        return getLayoutY() + R;
    }
    public void setInitial(){

    }
}
