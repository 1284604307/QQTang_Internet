package org.ming.view.loginView;

import io.vertx.core.json.JsonObject;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.ming.framework.PushAndPopPane;

public class LoginView extends PushAndPopPane {
    public LoginView(Stage stage, JsonObject jsonObject) {
        super(stage, jsonObject);

        TextField username = new TextField();
        Button login = new Button("登入");

        login.setOnMouseClicked(event -> {
            //todo 向服务器注册用户名

        });
    }


}
