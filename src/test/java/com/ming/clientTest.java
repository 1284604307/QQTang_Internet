package com.ming;

import com.google.gson.Gson;
import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.Map.Wall;
import com.ming.MVC.model.Player;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class clientTest extends Application {
    static Player player;
    public static void main(String[] args)  {
        // 创建一个TCP客户端
        Vertx vertx = Vertx.vertx();
        NetClient client = vertx.createNetClient();

// 连接服务器
        client.connect(5555, "localhost", conn -> {
            if (conn.succeeded()) {
                NetSocket s = conn.result();

                Gson gson = new Gson();
                 player = new Player();
                // 向服务器写数据
                s.write(Buffer.buffer(gson.toJson(player)));


                // 读取服务器的响应数据
                s.handler(buffer -> {
                    System.out.println(buffer.toString());
                });
            } else {
                System.out.println("连接服务器异常");
            }
        });

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FlowPane flowPane = new FlowPane();
        Button send = new Button("send");
        send.setOnMouseClicked(event -> {
            System.out.println(player);
        });
        flowPane.getChildren().add(send);
        stage.setScene(new Scene(flowPane,400,400));
        stage.show();
    }
}
