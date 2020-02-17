package org.ming.view.hall;

import io.vertx.core.json.JsonObject;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.ming.framework.Navigator;
import org.ming.framework.PushAndPopPane;

public class HallView extends PushAndPopPane {

    public HallView(Stage stage) {
        this(stage,new JsonObject());
    }

    public HallView(Stage stage, JsonObject jsonObject) {
        super(stage, jsonObject);

        AnchorPane root = new AnchorPane();
        root.setPrefWidth(800);
        root.setPrefHeight(600);
        this.getChildren().add(root);
        Button quickStartButton = new Button("快捷开始");
        quickStartButton.setPrefWidth(120);
        quickStartButton.setPrefHeight(40);
        Button crateRoomButton = new Button("创建房间");
        crateRoomButton.setPrefWidth(120);
        crateRoomButton.setPrefHeight(40);
        Button localGameButton = new Button("本地对战");
        Button back = new Button("返回");
        back.setPrefWidth(80);
        back.setPrefHeight(60);
        root.getChildren().add(crateRoomButton);
        root.getChildren().add(quickStartButton);
        root.getChildren().add(back);
        back.setOnMouseClicked(event -> {
            Navigator.pop();
        });

//        crateRoomButton.setLayoutX(600);
//        crateRoomButton.setLayoutY(500);
        AnchorPane.setRightAnchor(crateRoomButton,230d);
        AnchorPane.setBottomAnchor(crateRoomButton,0d);
        AnchorPane.setRightAnchor(quickStartButton,100d);
        AnchorPane.setBottomAnchor(quickStartButton,0d);
        AnchorPane.setRightAnchor(back,0d);
        AnchorPane.setBottomAnchor(back,0d);

        crateRoomButton.setOnMouseClicked(event -> {
            jsonObject.put("homeIndex",1);
            Navigator.pushNamed("/home");
        });

    }
}
