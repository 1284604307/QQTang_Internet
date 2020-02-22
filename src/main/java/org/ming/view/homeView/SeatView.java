package org.ming.view.homeView;


import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.ming.model.base.ImageUrl;
import org.ming.connect.model.User;

public class SeatView extends AnchorPane {

    private Text name = new Text(" ");
    private User user = null;
    private final Text NONE = new Text("\n无\n人");
    private final Text BAN = new Text("\n\n禁");
    private Text showText = NONE;
    private ImageView player = new ImageView();
    private int row;
    private int col;
    private int index;
    private boolean open = true;

    public SeatView(int row,int col){
        super();
        this.index = row*4+col;
        NONE.setFont(new Font(30));
        BAN.setFont(new Font(30));
        TextFlow textFlow = new TextFlow();
        setPrefWidth(120);
        setPrefHeight(200);
        this.setLayoutX(col*135);
        this.setLayoutY(row*320);
        this.getChildren().add(textFlow);
        textFlow.setPrefWidth(120);
        textFlow.setPrefHeight(16);
        textFlow.setStyle("-fx-background-color: #ff2222;-fx-text-alignment: center");
        textFlow.getChildren().add(name);
        name.setFill(Color.WHITE);
        TextFlow noneView = new TextFlow(showText);
        noneView.setPrefWidth(120);
        noneView.setPrefHeight(184);
        noneView.setLayoutY(16);
        noneView.setStyle("-fx-background-color: #ffffff33;-fx-text-alignment: center;");
        this.getChildren().add(noneView);
        player.setX(30);
        player.setY(80);
        this.getChildren().add(player);

        this.setStyle("-fx-background-color: #00000022;-fx-start-margin: 10");
    }

    public boolean empty(){
        return open && user == null;
    }

    public ImageView getPlayer() {
        return player;
    }

    public User getUser() {
        return user;
    }

    private void setPlayer(ImageView view){
        this.player = view;

    }

    public void setUser(User user) {
        this.user = user;
        if (user==null) {
            this.name.setText(null);
            showText.setVisible(true);
            showText = NONE;
        } else{
            this.name.setText(user.getName());
            showText.setVisible(false);
            user.setSeatIndex(index);
            this.player.setImage(ImageUrl.roles.get(user.getPlayerName()));
            System.out.println(user.getName()+"坐在了座位"+index);
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
