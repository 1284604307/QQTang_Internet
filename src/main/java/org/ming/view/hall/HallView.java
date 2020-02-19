package org.ming.view.hall;

import io.vertx.core.json.JsonObject;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.ming.connect.enums.Common;
import org.ming.controller.ConnectController;
import org.ming.framework.Navigator;
import org.ming.framework.PushAndPopPane;

public class HallView extends PushAndPopPane {

    public HallView(Stage stage) {
        this(stage,new JsonObject());
    }

    public HallView(Stage stage, JsonObject jsonObject) {
        super(stage, jsonObject);

        BorderPane rootPane = new BorderPane();
        rootPane.setPrefWidth(800);
        rootPane.setPrefHeight(600);
        Pane title = new Pane();
        title.setPrefWidth(800);
        title.setPrefHeight(100);
        rootPane.setTop(title);


        //  240 X 100  一页10个最多
        ConnectController.write(Common.QUERY_ROOM,"0,1");

        TilePane left = new TilePane();
        left.setPrefWidth(500);
        left.setPrefHeight(500);
        rootPane.setLeft(left);


        VBox right = new VBox();
        right.setPrefWidth(300);
        right.setPrefHeight(500);
        rootPane.setRight(right);
        left.setStyle("-fx-background-color: black");
        rootPane.setStyle("-fx-background-color: white");

        VBox userPane = new VBox();
        userPane.setPrefHeight(160);
        VBox chatPane = new VBox();
        chatPane.setPrefHeight(300);
        HBox buttonPane = new HBox();
        buttonPane.setPrefHeight(40);

        right.getChildren().addAll(userPane,chatPane,buttonPane);
        this.getChildren().add(rootPane);

        Button quickStartButton = new Button("快捷开始");
        quickStartButton.setPrefWidth(120);
        Button crateRoomButton = new Button("创建房间");
        crateRoomButton.setPrefWidth(120);
        Button localGameButton = new Button("本地对战");
        Button back = new Button("返回");
        back.setPrefWidth(80);

        buttonPane.getChildren().addAll(crateRoomButton,quickStartButton,back);
        back.setOnMouseClicked(event -> {
            Navigator.pop();
        });


        crateRoomButton.setOnMouseClicked(event -> {
            jsonObject.put("homeIndex",1);
            ConnectController.write(Common.CREATE_ROOM,"");
//            Navigator.pushNamed("/home");
        });

    }
}
