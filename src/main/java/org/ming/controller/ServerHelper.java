package org.ming.controller;

import com.google.gson.Gson;
import io.vertx.core.net.NetSocket;
import org.ming.test.DoMessage;
import org.ming.test.MsgUtils;

import java.util.HashMap;
import java.util.Map;

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

            } catch (IllegalStateException e) {
                System.out.println(stringNetSocketEntry.getKey()+"信号中断，移除");
                SOCKET_MAP.remove(stringNetSocketEntry.getKey());
            }
        }
    }
}
