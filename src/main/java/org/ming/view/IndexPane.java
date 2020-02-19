package org.ming.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.ming.framework.Navigator;
import org.ming.framework.PushAndPopPane;

public class IndexPane extends PushAndPopPane {
    public IndexPane(Stage stage) {
        super(stage);

        HBox hBox = new HBox();

        Button b1 = new Button("本地对战 - 双人");
        Button b2 = new Button("网络对战");
        Button b3 = new Button("大厅");
        Button mapEditor = new Button("地图编辑器");
        hBox.getChildren().add(b1);
        hBox.getChildren().add(b2);
        hBox.getChildren().add(b3);
        hBox.getChildren().add(mapEditor);

        this.getChildren().add(hBox);

        b1.setOnMouseClicked(event -> {
            Navigator.pushNamed("/localGame");
        });
        b2.setOnMouseClicked(event -> {
            Navigator.pushNamed("/login");
        });
        b3.setOnMouseClicked(event -> {
            Navigator.pushNamed("/hall");
        });
        mapEditor.setOnMouseClicked(event -> {
            Navigator.pushNamed("/mapEditor");
        });
    }
}
