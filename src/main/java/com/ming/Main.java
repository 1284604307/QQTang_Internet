package com.ming;

import com.ming.MVC.model.Map.*;
import com.ming.MVC.model.Prop.Prop;
import com.ming.MVC.model.UnitType;
import com.ming.MVC.model.playerForms.骑士;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.ming.MVC.model.Bubble;
import com.ming.MVC.model.Exploding;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.Player;
import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.controller.KeyStack;

import java.util.*;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FlowPane root = new FlowPane();
        Button openLocalGame = new Button("打开本地游戏");
        Button Server = new Button("打开服务器");
        Button Client = new Button("打开客户端");
        openLocalGame.setLayoutY(30);
        openLocalGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        root.getChildren().add(openLocalGame);

        stage.setScene(new Scene(root));
        stage.show();
    }
}
