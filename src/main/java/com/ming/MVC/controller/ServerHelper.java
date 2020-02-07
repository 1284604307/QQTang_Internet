package com.ming.MVC.controller;

import com.google.gson.Gson;
import com.ming.MVC.model.Player;
import io.vertx.core.net.NetSocket;
import test.DoMessage;
import test.MsgUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import static com.ming.MVC.model.Behavior.SYN_PLAYER;

public class ServerHelper {

    //ex playerID , NetSocket
    public final Map<String, NetSocket> SOCKET_MAP = new HashMap<>();
    //ex playerID , Long
    public final Map<String, Long> ACTIVE_SOCKET_MAP = new HashMap<>();

//    private final Map<String, Player> players = new HashMap<>();

    private Gson gson = new Gson();

    public void sendAll(DoMessage doMessage){
        for (Map.Entry<String, NetSocket> stringNetSocketEntry : SOCKET_MAP.entrySet()) {
            try {
                NetSocket socket = stringNetSocketEntry.getValue();
                socket.write(MsgUtils.joinMsg("NULL",
                        gson.toJson(doMessage)
                ));
            }catch (NullPointerException ignored){

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
