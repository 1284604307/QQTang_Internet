package org.ming.view.loginView;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ming.connect.enums.Common;
import org.ming.controller.ConnectController;
import org.ming.framework.Navigator;
import org.ming.framework.PushAndPopPane;
import org.ming.connect.model.User;
import org.ming.view.hall.HallView;

public class LoginView extends PushAndPopPane {

    private Gson gson = new Gson();
    private boolean retry = true;

    public LoginView(Stage stage, JsonObject jsonObject) {
        super(stage, jsonObject);


        VBox root =  new VBox();
        TextField username = new TextField();
        username.setPromptText("用户名");
        root.getChildren().add(username);
        Button login = new Button("登入");
        root.getChildren().add(login);
        login.setOnMouseClicked(event -> {
            System.out.println(username.getText());
            User user = new User(username.getText(), 0);
            ConnectController.write(Common.LOGIN,gson.toJson(user));
            ConnectController.user = user;
        });

        Button createRoom = new Button("创建房间");
        createRoom.setOnMouseClicked(event -> {
            ConnectController.write(Common.CREATE_ROOM,"");
        });
        root.getChildren().add(createRoom);
        this.getChildren().add(root);

    }

    private static String linkCommon(Common common, String s){
        return common+"*"+s+"\n";
    }



}
