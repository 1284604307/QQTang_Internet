package org.ming.connect;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.NetSocket;
import javafx.animation.AnimationTimer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ming.connect.enums.Common;
import org.ming.model.GameObjectManager;
import org.ming.model.World;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Position;
import org.ming.model.players.Player;
import org.ming.test.TcpClientVerticle;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class GameConnectThread implements Runnable {
    private World world = new World();
    private Player[] players;
    private DatagramSocket udp;
    private Gson gson = new Gson();
    private String[] ips ;

//    public GameConnectThread(NetSocket[] sockets) {
//        this.sockets = sockets;
//    }
    public GameConnectThread(String[] ips) {
        this.ips = ips;
    }

    @Override
    public void run() {
        System.out.println("游戏线程启动");
        Vertx vertx = Vertx.vertx();
        udp = vertx.createDatagramSocket();
//        ips = new String[sockets.length];
//        for (int i = 0; i < sockets.length; i++) {
//            ips[i] = sockets[i].remoteAddress().host();
//        }
        //todo 同步玩家,地图等初始信息
        players = new Player[ips.length];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i);
        }
//        players[1].setPosition(new Position(560,0));
//        players[1].setGroupId(2);
        world.setPlayers(players);
        try {
            world.loadMap(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("本地任务初始完毕");

        AtomicInteger initSize = new AtomicInteger();
        udp.handler(datagramPacket -> {
            String data = datagramPacket.data().toString();
            String[] msgs = data.split("\\*");
            System.out.println(data);
            switch (Common.valueOf(msgs[0])){
                case WAIT_INIT_GAME:
                    String host = datagramPacket.sender().host();
                    System.out.println(host+"请求同步");
                    udp.send(linkCommon(
                            Common.INIT_PLAYER,
                            gson.toJson(players)
                    ),12345,host,null);
                    break;
                case INIT_GAME_SUCCESS:
                    System.out.println("人物初始完毕");
                    if (initSize.incrementAndGet() >=ips.length){
                        //todo 游戏正式开始
                        new MainUpdate().start();
                    }
                    break;


            }
        });

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
            if (o==0) {
                try {
                    o++;
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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

                if(startTimer<0){
                    win();
                }else startTimer-=1000/60;



            }
            try {
                syn_info(world.getGameObjectManager());
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        private void win(){
            start = false;
            this.stop();
        }

    }

    private static String linkCommon(Common common, String s){
        return common+"*"+s;
    }


    private void syn_info(GameObjectManager gameObjectManager){

        for (String ip : ips) {
            udp.send(linkCommon(
                    Common.SYN_PLAYER,
                    gson.toJson(players)
                    ),
                    12345, ip, null);
            udp.send(linkCommon(
                    Common.SYN_GAME,
                    gson.toJson(gameObjectManager)
                    ),
                    12345, ip, null);
        }
    }
}
