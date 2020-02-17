package org.ming.view;

import com.google.gson.Gson;
import io.vertx.core.json.JsonObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.ming.framework.PushAndPopPane;
import org.ming.pojo.User;
import org.ming.view.homeView.GameRoomWithChairView;

public class HomePane extends PushAndPopPane {
    private int homeIndex = 0;

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
        GameRoomWithChairView showPane = new GameRoomWithChairView();


        this.getChildren().add(showPane);
        stage.show();

        homeIndex = jsonObject.getInteger("homeIndex");
        number.setText(String.valueOf(homeIndex));
        User user =new Gson().fromJson(jsonObject.getString("user"),User.class);
//        User user = (User) jsonObject.getValue("user");
        showPane.addUser(user);
        user.setRoomIndex(homeIndex);
    }
}
