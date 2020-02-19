package org.ming.controller;

import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import javafx.application.Platform;
import org.ming.connect.enums.Common;
import org.ming.connect.model.User;
import org.ming.framework.Navigator;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ConnectController extends AbstractVerticle {

    private Vertx vertx;
    private static NetSocket socket;
    private Boolean retry = true;
    private JsonObject jsonObject;
    private NetClient client;
    public static User user;
    private Gson gson = new Gson();
    public ConnectController(JsonObject jsonObject){
        this.jsonObject = jsonObject;
    }

    private static String linkCommon(Common common, String s){
        return common+"*"+s+"\n";
    }

    private void toOther(String other){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Navigator.pushNamed(other);
            }
        });
    }

    public static void write(Common common, String s){
        String all = linkCommon(common, s);
        System.out.println("send : "+all);
        socket.write(all);
//        System.out.println(socket);
    }

    @Override
    public void start() throws Exception {
        if (vertx==null)
            vertx = Vertx.vertx();
        NetClientOptions options = new NetClientOptions()
                .setConnectTimeout(10000)
                .setReconnectAttempts(10)
                .setReconnectInterval(500);

        client = vertx.createNetClient(options);
        tryConnect(client);
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    private void tryConnect(NetClient client){
        client.connect(38888, "localhost", res -> {
            if (res.succeeded()){
                if (socket!=null){
//                    retry = false;
                    socket.end();
                    socket =null;
                }
                System.out.println("Connected!");
                socket = res.result();
                System.out.println(socket.writeHandlerID());

                socket.handler(r -> {
//                    socket.write(linkCommon(Common.CREATE_ROOM,gson.toJson(user)));
                    System.out.println(r);
                    String msg = r.toString();
                    String[] msgs = msg.split("\\*");
                    switch (Common.valueOf(msgs[0])){
                        case LOGIN_SUCCESS:
                            toOther("/hall");
                            System.out.println("登录成功，转入大厅界面");
                            break;
                        case CREATE_ROOM_SUCCESS:
                            System.out.println("创建房间"+msgs[1]+"成功，转入房间界面");
                            jsonObject.put("homeIndex",msgs[1].toString());

//                            Iterator<Map.Entry<String, Object>> iterator = jsonObject.iterator();
//                            while (iterator.hasNext()){
//                                System.out.println(iterator.next());
//                            }
                            toOther("/home");
                            break;
                        case QUERY_ROOM_SUCCESS:
                            System.out.println(msgs[1]);
                            Set set = gson.fromJson(msgs[1], Set.class);
                            for (Object o : set) {
                                System.out.println(o);
                            }
                            break;
                    }

                });

                socket.closeHandler(aVoid -> {
                    try {
                        if (!retry){
                            retry = true;
                            return;
                        }
                        System.out.println("两秒后重连");
                        Thread.sleep(2000);
                        System.out.println("重连");
                        tryConnect(client);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                });
            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());

            }
        });
    }

}
