package Visual;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MooreState extends State{
    private Label outputLabel;
    VBox innerPanel = new VBox();
    public MooreState(double x, double y,char output) {
        super(x, y);
        outputLabel = new Label();
        outputLabel.setText(String.valueOf(output));
        outputLabel.setTextFill(Color.WHITE);
        innerPanel.setPrefWidth(R);
        innerPanel.setPrefHeight(R);
        innerPanel.setAlignment(Pos.CENTER);
        getChildren().add(innerPanel);
        getChildren().remove(nameLabel);
        innerPanel.getChildren().addAll(nameLabel,outputLabel);
    }

}
