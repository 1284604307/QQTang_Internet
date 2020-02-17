package org.ming.view;

import io.vertx.core.json.JsonObject;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ming.framework.PushAndPopPane;
import org.ming.model.World;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Position;
import org.ming.model.players.Player;
import org.ming.test.TcpClientVerticle;

import java.text.DecimalFormat;

import static javafx.scene.input.KeyCode.*;

public class LocalGamePane extends PushAndPopPane {

    private static final Logger LOGGER = LogManager.getLogger(TcpClientVerticle.class);

    private Canvas[] gameCanvas = new Canvas[]{new Canvas(600,540),new Canvas(600,540)};
    private GraphicsContext g2D;

    private final static int translateX = 8;
    private final static int translateY = 22;

    private static Image gameWindowBg;
    private Canvas backgroundCanvas;
    private World world = new World();
    private Player[] players;

    public LocalGamePane(Stage stage) {
        this(stage,new JsonObject());
    }

    public LocalGamePane(Stage stage, JsonObject jsonObject) {
        super(stage, jsonObject);
        Pane root = new AnchorPane();
        stage.setTitle("仿QQ堂");
        backgroundCanvas = new Canvas(800,600);

        //todo 画操作栏
        gameWindowBg = new Image("file:src/main/resources\\Images\\BackgroundImage\\GameWindow.png");
        backgroundCanvas.getGraphicsContext2D().setFont(new Font("楷体",24));

        backgroundCanvas.getGraphicsContext2D().drawImage(gameWindowBg,0,0);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);

//        //todo 置入主画布
        root.getChildren().add(gameCanvas[0]);
        root.getChildren().add(gameCanvas[1]);
        root.getChildren().add(backgroundCanvas);
        stage.show();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    players[0].getKeyStack().push(event.getCode());
                    break;
                case ENTER:
                    players[0].attck(world);
                    break;
                case A:
                    players[1].getKeyStack().push(LEFT);
                    break;
                case S:
                    players[1].getKeyStack().push(DOWN);
                    break;
                case D:
                    players[1].getKeyStack().push(RIGHT);
                    break;
                case W:
                    players[1].getKeyStack().push(UP);
                    break;
                case SPACE:
                    players[1].attck(world);
                    break;

            }
        });
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()){
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    players[0].getKeyStack().remove(event.getCode());
                    break;
                case A:
                    players[1].getKeyStack().remove(LEFT);
                    break;
                case S:
                    players[1].getKeyStack().remove(DOWN);
                    break;
                case D:
                    players[1].getKeyStack().remove(RIGHT);
                    break;
                case W:
                    players[1].getKeyStack().remove(UP);
                    break;
            }
        });


        gameCanvas[1].setVisible(false);

        stage.show();
        init(stage);

    }


    public void init(Stage stage){
        // todo 画面正式开始
        players = new Player[2];
        for (int i = 0; i < players.length; i++) {
            players[i] =  new Player(ImageUrl.Monkey);
        }
        players[1].setPosition(new Position(560,0));
        players[1].setGroupId(2);
        world.setPlayers(players);
        world.read(stage);
        // 锁帧线程 和 UI线程 启动
        new MainUpdate().start();
    }

    class MainUpdate extends AnimationTimer {
        private int o = 0;
        private int startTimer = 240000;
        private boolean start = true;
        private double end = 0;
        private int liver = 2;
        DecimalFormat df=new DecimalFormat("00");
        MainUpdate(){
        }

        @Override
        public void handle(long now) {
            int oldO = o;
            o = (++o)%2;
            //todo 地图
            g2D = gameCanvas[o].getGraphicsContext2D();
            g2D.drawImage(new Image("file:src/main/resources\\Images\\Map\\标准.jpg"),0,0);
            if (start){
                int row = world.getRows();
                int col = world.getCols();
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        world.update(i,j);
                    }
                    for (Player player:
                            players) {
                        if (player.getPoint().y == i ) {
                            player.update(world);
                            if (player.recycled())
                                liver--;
                        }
                        if (liver<=1)
                            win();
                    }
                }

                for (int i = 0; i <row; i++) {
                    for (int j = 0; j < col; j++) {
                        world.draw(g2D,i,j);
                    }
                    for (Player player:
                            players) {
                        if (player.getPoint().y == i ) {
                            player.draw(g2D);
                        }
                    }
                }

                if(startTimer<0){
                    win();
                }else startTimer-=1000/60;
                drawTime(startTimer/1000);
            }else {
                g2D.setFill(Color.WHITE);
                if (end<60) {
                    g2D.setGlobalAlpha(0.4+ end/100);
                    g2D.fillRect(100,end++,400,400);
                    g2D.setGlobalAlpha(1);
                } else {
                    g2D.fillRect(100,end,400,400);
                }
                g2D.setFill(Color.BLACK);
                g2D.setFont(new Font(30));
                g2D.fillText("  结束啦!  ",240,end);
            }

            gameCanvas[o].setVisible(true);
            gameCanvas[oldO].setVisible(false);



            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        private void drawTime(int time) {
            int m = time / 60;
            int s = time%60;
            backgroundCanvas.getGraphicsContext2D().setFill(Color.WHITE);
            backgroundCanvas.getGraphicsContext2D().fillRect(680,32,100,38);
            backgroundCanvas.getGraphicsContext2D().setFill(Color.BLACK);
            backgroundCanvas.getGraphicsContext2D().fillText(df.format(m)+":"+df.format(s),700,60);
        }
        private void win(){
            start = false;
            world.getMusicManager().stopBGM();
            world.getMusicManager().win();
        }

    }

}
