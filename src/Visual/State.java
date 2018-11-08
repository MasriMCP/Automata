package Visual;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.awt.*;

public class State extends Circle {
    public final static double R =15;
    private Label nameLabel = new Label();
    public State(double x,double y){
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(R);
    }

    public String getName() {
        return nameLabel.getText();

    }

    public void setName(String name) {
        nameLabel.setText(name);
    }
}
