package test;

import com.google.gson.Gson;
import com.ming.GameObjectManager;
import com.ming.LocalGameObjectManager;
import com.ming.MVC.controller.KeyStack;
import com.ming.MVC.model.*;
import com.ming.MVC.model.Map.*;
import com.ming.MVC.model.Prop.Prop;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.base.Position;
import com.ming.MVC.model.playerForms.骑士;
import com.ming.MusicManager;
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
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Array;
import java.util.*;


/**
 * Created by sweet on 2018/1/2.
 * ---------------------------
 */
public class TcpClientVerticle extends Application {


    private static final Logger LOGGER = LogManager.getLogger(TcpClientVerticle.class);

    private  String socketId;
    static Gson gson = new Gson();

    private  NetSocket netSocket;

    private static Canvas gameCanvas;
    private static GraphicsContext g2D;

    private final static int translateX = 8;
    private final static int translateY = 22;

    private ArrayList<DoMessage> doMessages = new ArrayList<>();

    static Image gameWindowBg = new Image("file:src/main/resources\\Images\\BackgroundImage\\GameWindow.png");

    KeyStack<KeyCode> keyStack = new KeyStack<>();

    public static LocalGameObjectManager gameObjectManager;
    private Player player;
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
//                System.out.println(doMessage);
                if (doMessage.behavior==Behavior.SYN_PLAYER){
                    Player[] ps = gson.fromJson(doMessage.value, Player[].class);
                    for (int i = 0; i < ps.length; i++) {
                        players[i].updateProperty(ps[i]);
                    }

                }
                else if (doMessage.behavior == Behavior.UPDATE_ALL){
                    ArrayList<ArrayList<String>> arrayList = gson.fromJson(doMessage.value, ArrayList.class);
                    gameObjectManager.updateItems(arrayList);
//                    if (arrayList.size()>0)
//                        System.out.println(arrayList);
                }
                else {
//                    doMessages.add(doMessage);
                }
            }
        });

        client.connect(8080, "127.0.0.1", conn -> {
            if (conn.succeeded()) {
                System.out.println("client ok");
                netSocket = conn.result();
                netSocket.handler(parser::handle);
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
    public void start(Stage stage) throws Exception {
        init();

        Pane root = new AnchorPane();
        stage.setTitle("仿QQ堂");
        Canvas backgroundCanvas = new Canvas(800,600);
        //todo 画操作栏
        backgroundCanvas.getGraphicsContext2D().drawImage(gameWindowBg,0,0);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        gameCanvas = new Canvas(600,540);
        gameCanvas.setLayoutX(translateX);
        gameCanvas.setLayoutY(translateY);
        g2D = gameCanvas.getGraphicsContext2D();

        gameObjectManager = new LocalGameObjectManager();

//        //todo 置入主画布
        root.getChildren().add(gameCanvas);
        root.getChildren().add(backgroundCanvas);
        gameCanvas.setFocusTraversable(true);
        stage.show();

        gameCanvas.setOnKeyPressed(event -> {
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
        gameCanvas.setOnKeyReleased(event -> {
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

        MusicManager.init();


        stage.show();
    }



    class MainUpdate extends AnimationTimer {

        @Override
        public void handle(long now) {

            //todo 地图
            g2D.drawImage(new Image("file:src/main/resources\\Images\\Map\\标准.jpg"),0,0);

            player = players[0];

            boolean d = true;
            int s = 0;
            for (Item item:gameObjectManager.getItems()) {
                s = item.point.y ;
                if (d && s >= player.point.y )
                {
                    d = false;
                    player.draw(g2D);
                }
                item.draw(g2D);
            }
            if (d){
                player.draw(g2D);
            }

//            drawItemsByItemsArr();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


//    private void drawItemsByItemsArr(){
//
//        ArrayList<Item>[][] items = gameObjectManager.getItemsArr();
//
//        for (int i =0;i<items.length;i++) {
//            ArrayList<Item>[] itemArr = items[i];
//            for (ArrayList<Item> itemsArr : itemArr) {
//                if (itemsArr == null) continue;
//                for (Item item : itemsArr) {
//                    if (item.getUnitType() == UnitType.NULL) continue;
//                    item.draw(g2D);
//                }
//            }
//            // ex 在第人物所在行渲染角色
//            if (player.getPoint().y == i ) {
//                player = player.trans();
//                player.draw(g2D);
//            }
//        }
//
//
//    }




}