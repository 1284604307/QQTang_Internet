package org.ming.view.hall;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.ming.connect.enums.Common;
import org.ming.controller.ConnectController;

public class RoomCard extends VBox {

    private int index = 0;
    private String name ;
    private int allSeat = 0;
    private int count = 1;

    public RoomCard(int index,String name,int allSeat,int count) {
        super();
        this.setPrefWidth(245);
        this.setPrefHeight(90);
        this.setStyle("-fx-background-color: #ffffff");
        this.index = index;
        this.name = name;
        this.allSeat = allSeat;
        this.count = count;
        Text title = new Text(String.valueOf(index));
        Text roomName = new Text(name);
        TextFlow topLeft = new TextFlow(title,roomName);

        topLeft.prefWidth(245);topLeft.prefHeight(30);
        title.setStyle("-fx-background-color: #99999999");
        this.getChildren().add(topLeft);

        HBox content = new HBox();
        content.setPrefHeight(245);content.setPrefHeight(70);
        Text radio = new Text(count + "/" + allSeat);
        content.getChildren().add(radio);

        this.getChildren().add(content);


        this.setOnMouseClicked(event -> {
            ConnectController.write(Common.ENTER_ROOM,String.valueOf(index));
            System.out.println(this.getWidth()+" "+this.getHeight());
        });


        // 鼠标样式改变
        // Cursor.HAND：手型
        // Cursor.DEFAULT：箭头
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->this.setCursor(Cursor.HAND));
        this.addEventHandler(MouseEvent.MOUSE_EXITED, e->this.setCursor(Cursor.DEFAULT));

        // 控件文本改变
        this.setOnMouseReleased(event -> this.setStyle("-fx-background-color: #ffffff66"));
        this.setOnMousePressed(event -> this.setStyle("-fx-background-color: #ffffff36"));
        this.setOnMouseMoved(event -> this.setStyle("-fx-background-color: #ffffff66"));
        this.setOnMouseExited(event -> this.setStyle("-fx-background-color: #ffffff"));
    }
}
