package org.ming;

import com.google.gson.Gson;
import io.vertx.core.json.JsonObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.ming.controller.ConnectController;
import org.ming.framework.Navigator;
import org.ming.mapEditer.MapEditor;
import org.ming.connect.model.User;
import org.ming.view.HomePane;
import org.ming.view.IndexPane;
import org.ming.view.LocalGamePane;
import org.ming.view.hall.HallView;
import org.ming.view.loginView.LoginView;

public class Main extends Application {


    private Stage stage ;
    private static StackPane root = new StackPane();
    private static Navigator routes = new Navigator("/");

    public static StackPane getRoot() {
        return root;
    }

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("仿 QQ-TANG");
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        JsonObject jsonObject = new JsonObject();
        routes.route("/", () -> {
            return new IndexPane(stage);
        }).route("/loading", () -> {
            return new LoadingPane(stage);
        }).route("/localGame", () -> {
            return new LocalGamePane(stage,jsonObject);
        }).route("/hall", () -> {
            return new HallView(stage,jsonObject);
        }).route("/home", () -> {
            return new HomePane(stage,jsonObject);
        }).route("/mapEditor", () -> {
            return new MapEditor(stage,jsonObject);
        }).route("/login", () -> {
            return new LoginView(stage,jsonObject);
        });
        root.getChildren().add(routes.initialRoute());

        stage.show();

//        Gson gson = new Gson();
//        User user = new User("李四",1);
//        jsonObject.put("user", gson.toJson(user));
//        jsonObject.put("user",user);

        try {
            ConnectController connect = new ConnectController(jsonObject);
            connect.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Navigator getRoutes() {
        return routes;
    }

    public static void setRoutes(Navigator routes) {
        Main.routes = routes;
    }

    public static void main(String[] args) {
        launch();
    }

}
