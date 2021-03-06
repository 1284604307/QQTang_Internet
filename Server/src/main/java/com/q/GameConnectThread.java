package com.q;

import com.google.gson.Gson;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.NetSocket;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ming.connect.enums.Common;
import org.ming.connect.model.RecycleMark;
import org.ming.model.GameObjectManager;
import org.ming.model.World;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Position;
import org.ming.model.base.UnitType;
import org.ming.model.baseInterface.GameObject;
import org.ming.model.bubble.Bubble;
import org.ming.model.players.Player;
import org.ming.model.prop.Prop;
import org.ming.test.TcpClientVerticle;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameConnectThread extends Thread {
    private World world = new World();
    private Player[] players;
    private DatagramSocket udp = ServerMain.udp;
    private Gson gson = new Gson();
    private String[] ips ;
    private int index;
    int count = 0;
    private NetSocket[] sockets;
    private HashMap<String,Player> ip_play = new HashMap<>();
    private HashMap<NetSocket,Player> socket_play = new HashMap<>();
    private ArrayList<String> oks = new ArrayList<>();
//    public GameConnectThread(NetSocket[] sockets) {
//        this.sockets = sockets;
//    }
    public GameConnectThread(int index,String[] ips,NetSocket[] sockets) {
        this.ips = ips;
        System.out.println(Arrays.toString(ips));
        this.index = index;
        this.sockets = sockets;

        players = new Player[ips.length];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i);
            ip_play.put(ips[i],players[i]);
            socket_play.put(sockets[i],players[i]);
        }
        if (players.length>1){
            players[1].setPosition(new Position(480,0));
        }
        world.setPlayers(players);
        try {
            world.loadMap(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player[] getPlayers() {
        return players;
    }

    public NetSocket[] getSockets() {
        return sockets;
    }

    public boolean ok(String ip){
        if (oks.contains(ip)) {
            return false;
        }else {
            oks.add(ip);
            return oks.size() == ips.length;
        }
    }

    public synchronized void gameStart() {
        new GameConnectThread.MainUpdate().start();
    }

    public void setSockets(NetSocket[] sockets) {
        this.sockets = sockets;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void run() {
        System.out.println("游戏线程启动");
        //todo 同步玩家,地图等初始信息
        Handler<DatagramPacket> handler = datagramPacket -> {
            String data = datagramPacket.data().toString();
            String[] msgs = data.split("\\*");
            System.out.println(data);
            Player player = ip_play.get(datagramPacket.sender().host());
            switch (Common.valueOf(msgs[1])) {
                case KEYDOWN:
                    KeyCode keyCode = KeyCode.valueOf(msgs[2]);
                    if (keyCode==KeyCode.SPACE){
                        Bubble bubble = player.attck(world);
                        if(bubble!=null){
                            System.out.println("发送泡泡"+gson.toJson(bubble));
                            //todo 给放泡泡的人声音提示
                            // ex 通知所有人更新泡泡
                            syn_info(linkCommon(Common.SYN_GAME_BUBBLE,gson.toJson(bubble)));
                            System.out.println("发送成功");
                        }
                    }
                    else
                        player.getKeyStack().push(keyCode);
                    break;
                case KEYUP:
                    keyCode = KeyCode.valueOf(msgs[2]);
                    player.getKeyStack().remove(keyCode);
                    break;

            }
        };

        ServerMain.registerUdpHandler(index,handler);
    }

    class MainUpdate extends Thread {
        private int o = 0;
        private int startTimer = 240000;
        private boolean start = true;
        private double end = 0;
        private int liver = 2;
        DecimalFormat df=new DecimalFormat("00");

        private void win(){
            start = false;
            this.interrupt();
        }

        @Override
        public void run() {
            System.out.println("游戏循环!!!");
            try {
                Thread.sleep(2000);
                System.out.println("进入游戏循环");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (start){
                ArrayList<RecycleMark> recycleMarks = new ArrayList<>();
                ArrayList<RecycleMark> marks = new ArrayList<>();
                int row = world.getRows();
                int col = world.getCols();
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        ArrayList<ArrayList<RecycleMark>> update = world.update(i, j);
                        recycleMarks.addAll(update.get(0));
                        marks.addAll(update.get(1));
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

                try {
                    if (marks.size()>0){
                        System.out.println("增加"+marks);
                    }
                    if (recycleMarks.size()>0){
                        System.out.println("删除"+recycleMarks);
                    }
                    syn_info(recycleMarks,marks);
                    Thread.sleep(1000/30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String linkCommon(Common common, String s){
        return common+"*"+s;
    }

    private static String tcpLinkCommon(Common common, String s){
        return "DATA_HEAD*"+common+"*"+s+"*DATA_FOOTER";
    }

    private void syn_info(String s){
        for (String ip : ips) {
            udp.send(s,
                    12344, ip, null);
        }
    }

    private void syn_info(ArrayList<RecycleMark> recycleMarks, ArrayList<RecycleMark> marks){

        for (String ip : ips) {
            if (marks.size()>0){
                if(marks.size()>45){
                    udp.send(
                            linkCommon(
                                    Common.SYN_GAME_ADD,
                                    gson.toJson(marks.subList(0,40))
                            ),
                            12344, ip, null);
                    udp.send(
                            linkCommon(
                                    Common.SYN_GAME_ADD,
                                    gson.toJson(marks.subList(40,recycleMarks.size()))
                            ),
                            12344, ip, null);
                }else
                    udp.send(
                            linkCommon(
                                    Common.SYN_GAME_ADD,
                                    gson.toJson(marks)
                            ),
                            12344, ip, null);
            }
            udp.send(
                linkCommon(
                    Common.SYN_PLAYER,
                    gson.toJson(players)),
                12344, ip, null);
            if (recycleMarks.size()>0){
                if(recycleMarks.size()>40){
                    udp.send(
                            linkCommon(
                                    Common.SYN_GAME_REMOVE,
                                    gson.toJson(recycleMarks.subList(0,40))
                            ),
                            12344, ip, null);
                    udp.send(
                            linkCommon(
                                    Common.SYN_GAME_REMOVE,
                                    gson.toJson(recycleMarks.subList(40,recycleMarks.size()))
                            ),
                            12344, ip, null);
                }else
                    udp.send(
                        linkCommon(
                            Common.SYN_GAME_REMOVE,
                            gson.toJson(recycleMarks)
                        ),
                        12344, ip, null);
            }
        }
    }
}
