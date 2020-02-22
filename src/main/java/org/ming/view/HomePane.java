package org.ming.view;

import io.vertx.core.json.JsonObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.ming.connect.enums.Common;
import org.ming.connect.model.HomeInfo;
import org.ming.connect.model.UserStatus;
import org.ming.controller.ConnectController;
import org.ming.framework.PushAndPopPane;
import org.ming.connect.model.User;
import org.ming.view.homeView.GameRoomWithChairView;

public class HomePane extends PushAndPopPane {
    private int homeIndex = 0;
    private HomeInfo homeInfo = ConnectController.homeInfo;
    private static GameRoomWithChairView showPane;
    public HomePane(Stage stage) {
        this(stage,new JsonObject());
    }

    public HomePane(Stage stage, JsonObject jsonObject) {
        super(stage, jsonObject);

        new Canvas();
        this.setStyle("-fx-background-color: #ffffff");
        Text number = new Text("333333");
        TextFlow textFlow = new TextFlow(new Text("房间号 : "),number);
        textFlow.setPrefWidth(800);
        textFlow.setPrefHeight(20);
        textFlow.setStyle("-fx-background-color: #6397ff43;-fx-text-alignment: center");
        this.getChildren().add(textFlow);
        showPane = new GameRoomWithChairView();


        this.getChildren().add(showPane);
        stage.show();

        String homeIndex = jsonObject.getString("homeIndex");
        number.setText(String.valueOf(homeIndex));
        this.homeIndex = Integer.parseInt(homeIndex);
        User user = ConnectController.user;

        String go ;
        if (user.getStatus()== UserStatus.Monster)
            go = "开始游戏";
        else go = "准备";
        Button start = new Button(go);
        start.setPrefWidth(80);
        start.setPrefHeight(50);
        start.setLayoutX(620);
        start.setLayoutY(540);
        start.setOnMouseClicked(event -> {
            if (user.getStatus()== UserStatus.Monster){
                ConnectController.write(Common.GAME_START,"");
            }else {
                ConnectController.write(Common.GAME_OK,"");
            }
        });
        Button exit = new Button("退出");
        exit.setOnMouseClicked(event -> {
            ConnectController.write(Common.EXIT_ROOM,"");
        });
        exit.setPrefWidth(80);
        exit.setPrefHeight(50);
        exit.setLayoutX(720);
        exit.setLayoutY(540);
        this.getChildren().add(start);
        this.getChildren().add(exit);
//        User user =new Gson().fromJson(jsonObject.getString("user"),User.class);
        for (User homeInfoUser : homeInfo.getUsers()) {
            addUser(homeInfoUser);
        }
        user.setRoomIndex(this.homeIndex);
    }

    private void addUser(User user){
        showPane.addUser(user);
    }

    public static void update(){
        showPane.update(ConnectController.homeInfo.getUsers());
    }
}
