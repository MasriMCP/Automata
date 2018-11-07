package Controllers;

import Auto.FiniteStateTransducer;
import Auto.NFA;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ResourceBundle;

public class DrawPaneController {
    @FXML
    private AnchorPane drawPane;
    private FiniteStateTransducer fst;
    @FXML
    private void mouseClick(MouseEvent e){
        drawPane.getChildren().add(new Circle(e.getSceneX(), e.getSceneY(), 15));
    }
    public void setFST(FiniteStateTransducer fst){
        this.fst = fst;
        System.out.println(fst);
    }
}
