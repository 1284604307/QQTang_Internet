package org.ming.test;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ming.controller.KeyStack;
import org.ming.model.World;
import org.ming.model.base.ImageUrl;
import org.ming.model.players.Player;

import java.util.ArrayList;


public class TcpClientVerticle extends Application {


    private static final Logger LOGGER = LogManager.getLogger(TcpClientVerticle.class);

    private  String socketId;
    private static Gson gson = new Gson();

    private  NetSocket netSocket;

    private static Canvas[] gameCanvas = new Canvas[2];
    private static GraphicsContext g2D;

    private final static int translateX = 8;
    private final static int translateY = 22;

    private static Image gameWindowBg = new Image("file:src/main/resources\\Images\\BackgroundImage\\GameWindow.png");

    private KeyStack<KeyCode> keyStack = new KeyStack<>();
    private World world = new World();
    private Player[] players;

    public static void main(String[] args) {
        gson = new Gson();
        launch();
    }

    public void init(){
        Vertx vertx = Vertx.vertx();

        NetClientOptions options = new NetClientOptions();
        options.setTcpKeepAlive(true);

        NetClient client = vertx.createNetClient();

        final RecordParser parser = RecordParser.newDelimited("\n", h -> {
            String msg = h.toString();
//            LOGGER.debug("客户端解包: " + msg);
            String[] msgSplit = msg.split("\\*");

            String socketId1 = msgSplit[0];
            String body = msgSplit[1];

            if ("Server".equals(body)) {
                socketId = socketId1;
            }else if ("Syn_Players".equals(body)){
                // todo 画面正式开始
                players = new Player[Integer.parseInt(socketId1)];
                // 锁帧线程 和 UI线程 启动
                new TcpClientVerticle.MainUpdate().start();
                for (int i = 0; i < players.length; i++) {
                    players[i] =  new Player(ImageUrl.Monkey);
                }
            }else {
                DoMessage doMessage = gson.fromJson(body, DoMessage.class);
                if (doMessage.behavior==Behavior.SYN_PLAYER){
                    Player[] ps = gson.fromJson(doMessage.value, Player[].class);
                    for (int i = 0; i < ps.length; i++) {
//                        if (ps[i].getTransStatus()==TransStatus.NULL) System.out.println(ps[i]);
                        players[i].updateProperty(ps[i]);
//                        players[i] = ps[i];
                    }

                }
                else if (doMessage.behavior == Behavior.UPDATE_ALL){
                    ArrayList<ArrayList<String>> arrayList = gson.fromJson(doMessage.value, ArrayList.class);
//                    gameObjectManager.updateItems(arrayList);
                }
                else {
                    switch (doMessage.behavior){
                        case ADD_BUBBLE:
//                            MusicManager.putBubble();
                            break;
                        case BUBBLE_BOOM:
//                            MusicManager.bubbleBomb();
                            break;

                    }
                }
            }
        });

        client.connect(8080, "127.0.0.1", conn -> {
            if (conn.succeeded()) {
                System.out.println("client ok");
                netSocket = conn.result();
                netSocket.handler(parser);
            } else {
                conn.cause().printStackTrace();
            }
        });

        // 客户端每 20 秒 发送一次心跳包
        vertx.setPeriodic(1000 * 20 , t -> {
            System.out.println("发送心跳包检活");
            if (netSocket != null)
                netSocket.write(MsgUtils.joinMsg(socketId, "PING"));
        });

    }




    @Override
    public void start(Stage stage) {

        Pane root = new AnchorPane();
        stage.setTitle("仿QQ堂");
        Canvas backgroundCanvas = new Canvas(800,600);
        //todo 画操作栏
        backgroundCanvas.getGraphicsContext2D().drawImage(gameWindowBg,0,0);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        gameCanvas[0] = new Canvas(600,540);
        gameCanvas[1] = new Canvas(600,540);

        for (Canvas gameCanva : gameCanvas) {
            gameCanva.setLayoutX(translateX);
            gameCanva.setLayoutY(translateY);
        }
        g2D = gameCanvas[1].getGraphicsContext2D();

//        //todo 置入主画布
        root.getChildren().add(gameCanvas[0]);
        root.getChildren().add(gameCanvas[1]);
        root.getChildren().add(backgroundCanvas);
        stage.show();
        init();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    if (keyStack.search(event.getCode())<0){
                        netSocket.write( MsgUtils.joinMsg(socketId, gson.toJson(new DoMessage(Behavior.KEY_PRESSED,event.getCode().toString())) ));
                    }
                    keyStack.push(event.getCode());
                    break;
                case SPACE:
                    netSocket.write( MsgUtils.joinMsg(socketId, gson.toJson(new DoMessage(Behavior.KEY_PRESSED,event.getCode().toString())) ));
                    break;
            }
        });
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()){
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    netSocket.write(MsgUtils.joinMsg(socketId,  gson.toJson(new DoMessage(Behavior.KEY_RELEASED,event.getCode().toString())) ) );
                    if (keyStack.search(event.getCode())>=0) {
                        keyStack.remove(event.getCode());
                    }
                    break;
            }
        });

        gameCanvas[1].setVisible(false);

        stage.show();
    }




    class MainUpdate extends AnimationTimer {
        private int o = 0;

        @Override
        public void handle(long now) {
            int oldO = o;
            o = (++o)%2;
            //todo 地图
            g2D = gameCanvas[o].getGraphicsContext2D();
            g2D.drawImage(new Image("file:src/main/resources\\Images\\Map\\标准.jpg"),0,0);

            for (int i = 0; i < world.getRows(); i++) {
                for (int j = 0; j < world.getCols(); j++) {
                    world.draw(g2D,i,j);
                }
                for (Player player:
                        players) {
                    if (player.getPoint().y == i ) {
                        player.draw(g2D);
                    }
                }
            }

            gameCanvas[o].setVisible(true);
            gameCanvas[oldO].setVisible(false);

            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }





}