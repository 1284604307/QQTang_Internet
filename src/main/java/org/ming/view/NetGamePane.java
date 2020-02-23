package org.ming.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;
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
import org.ming.connect.enums.Common;
import org.ming.connect.model.RecycleMark;
import org.ming.controller.ConnectController;
import org.ming.framework.PushAndPopPane;
import org.ming.model.World;
import org.ming.model.base.Point;
import org.ming.model.base.UnitType;
import org.ming.model.bubble.Bubble;
import org.ming.model.exploding.Exploding;
import org.ming.model.players.Player;
import org.ming.model.prop.Prop;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static org.ming.connect.enums.Common.INIT_GAME;
import static org.ming.connect.enums.Common.INIT_PLAYER;

public class NetGamePane extends PushAndPopPane {

//    private static final Logger LOGGER = LogManager.getLogger(NetGamePane.class);
    private Canvas[] gameCanvas = new Canvas[]{new Canvas(600,540),new Canvas(600,540)};
//    private final static int translateX = 8;
//    private final static int translateY = 22;
    private Canvas backgroundCanvas;
    private World world = new World();
    private Player[] players;
    private Gson gson = new Gson();
    private String index;
    private DatagramSocket udp;

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public NetGamePane(Stage stage, JsonObject jsonObject) {
        super(stage, jsonObject);
        for (Canvas gameCanva : gameCanvas) {
            gameCanva.setLayoutX(4);
            gameCanva.setLayoutY(12);
        }
        index = ConnectController.homeInfo.getIndex();
        Pane root = new AnchorPane();
        stage.setTitle("仿QQ堂");
        backgroundCanvas = new Canvas(800,600);


        Handler<String[]> tcpHandler = msgs -> {
            switch (Common.valueOf(msgs[0])){

                case INIT_PLAYER:
                    world.setPlayers(gson.fromJson(msgs[1], Player[].class));
                    players = world.getPlayers();
                    ConnectController.write(Common.WAIT_INIT_GAME,"");
                    break;
                case INIT_GAME:
                    UnitType[][] unitTypes = gson.fromJson(msgs[1], UnitType[][].class);
                    init(stage);
                    world.getGameObjectManager().setProps(new Prop[unitTypes.length][unitTypes[0].length]);
                    System.out.println(unitTypes.length);
                    for (int i = 0; i < unitTypes.length; i++) {
                        for (int i1 = 0; i1 < unitTypes[i].length; i1++) {
                            if (unitTypes[i][i1]!=null){
                                Prop prop = new Prop(new Point(i1, i));
                                prop.setUnitType(unitTypes[i][i1]);
                                world.getGameObjectManager().getProps()[i][i1]=prop;
                            }
                        }
                    }
                    ConnectController.write(Common.INIT_GAME_SUCCESS,"");
                    world.getMusicManager().readyGo();
                    break;
            }
        };
        ConnectController.registerTcpHandler(tcpHandler);

        udp = ConnectController.udp;
        ConnectController.registerUdpHandler(datagramPacket -> {

            Buffer data = datagramPacket.data();
            String s = data.toString();
            String[] msgs = s.split("\\*");
            switch (Common.valueOf(msgs[0])){
                case SYN_GAME_REMOVE:
                    ArrayList<RecycleMark> recycleMarks = gson.fromJson(msgs[1], new TypeToken<ArrayList<RecycleMark>>() {}.getType());
                    for (RecycleMark recycleMark : recycleMarks) {
                        world.removeNode(recycleMark);
                    }
                    break;
                case SYN_GAME_BUBBLE:
                    world.addNode(gson.fromJson(msgs[1], Bubble.class));
                    break;
                case SYN_GAME_ADD:
                    ArrayList<RecycleMark> marks = gson.fromJson(msgs[1], new TypeToken<ArrayList<RecycleMark>>() {}.getType());
                    for (RecycleMark recycleMark : marks) {
                        world.addNode(new Exploding(new Point(recycleMark.getX(),recycleMark.getY()),(byte) recycleMark.getDir()));
                    }
                    break;
                case SYN_PLAYER:
                    world.setPlayers(
                        gson.fromJson(msgs[1],Player[].class)
                    );
                    players = world.getPlayers();
                    break;
            }
        });

        ConnectController.write(Common.WAIT_INIT_PLAYER,"");
        //todo 画操作栏
        Image gameWindowBg = new Image("file:src/main/resources\\Images\\BackgroundImage\\GameWindow.png");
        backgroundCanvas.getGraphicsContext2D().setFont(new Font("楷体",24));
        backgroundCanvas.getGraphicsContext2D().drawImage(gameWindowBg,0,0);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);

//        //todo 置入主画布
        root.getChildren().add(gameCanvas[0]);
        root.getChildren().add(gameCanvas[1]);
        root.getChildren().add(backgroundCanvas);
        stage.show();

        gameCanvas[1].setVisible(false);
        stage.show();

    }


    private void init(Stage stage){
        // todo 画面正式开始
        try {
            world.loadMap(null);
            world.getMusicManager().oBGM();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 锁帧线程 和 UI线程 启动
        new MainUpdate().start();
        Scene scene = stage.getScene();
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    udp.send(linkCommon(Common.KEYDOWN,event.getCode().toString()),
                            12345,"127.0.0.1",datagramSocketAsyncResult -> {
                                System.out.println(datagramSocketAsyncResult.succeeded()?"按下指令发送":"按下指令发送失败");
                            }
                    );
                    break;
                case SPACE:
                    udp.send(linkCommon(Common.KEYDOWN,event.getCode().toString()),
                            12345,"127.0.0.1",datagramSocketAsyncResult -> {
                                System.out.println(datagramSocketAsyncResult.succeeded()?"点按指令发送":"点按指令发送失败");
                            }
                    );
                    break;
            }
        });
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()){
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    udp.send(linkCommon(Common.KEYUP,event.getCode().toString()),
                            12345,"127.0.0.1",datagramSocketAsyncResult -> {
//                                System.out.println(datagramSocketAsyncResult.succeeded()?"松开指令发送":"松开指令发送失败");
                            }
                    );
                    break;
            }
        });

    }

    private String linkCommon(Common common, String s){
        return index+"*"+common+"*"+s;
    }

    class MainUpdate extends AnimationTimer {
        private int o = 0;
        private int startTimer = 240000;
        private boolean start = true;
        private double end = 0;
        private int liver = 2;
        DecimalFormat df=new DecimalFormat("00");

        @Override
        public void handle(long now) {
            int oldO = o;
            o = (++o)%2;
            //todo 地图
            GraphicsContext g2D = gameCanvas[o].getGraphicsContext2D();
            g2D.drawImage(new Image("file:src/main/resources\\Images\\Map\\标准.jpg"),0,0);
            if (start){
                int row = world.getRows();
                int col = world.getCols();
                for (int i = 0; i < row; i++) {
                    for (Player player:
                            players) {
                        if (player.getPoint().y == i ) {
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
                Thread.sleep(1000/30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        private void drawTime(int time) {
            int m = time / 30;
            int s = time%30;
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
