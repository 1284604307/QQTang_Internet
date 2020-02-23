package org.ming.controller;

import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import javafx.application.Platform;
import org.ming.connect.enums.Common;
import org.ming.connect.model.HomeInfo;
import org.ming.connect.model.User;
import org.ming.connect.model.UserStatus;
import org.ming.framework.Navigator;
import org.ming.model.RoomList;
import org.ming.view.HomePane;

import java.util.*;

public class ConnectController extends AbstractVerticle {

    private Vertx vertx;
    private static NetSocket socket;
    private Boolean retry = true;
    private JsonObject jsonObject;
    private NetClient client;
    public static User user;
    public static DatagramSocket udp;
    public static HomeInfo homeInfo = null;
    private Gson gson = new Gson();
    public ConnectController(JsonObject jsonObject){
        this.jsonObject = jsonObject;
    }
    public static RoomList rooms;

    private static String linkCommon(Common common, String s){
        return common+"*"+s+"*\n";
    }

    private static ArrayList<Handler<DatagramPacket>> udpHandlers = new ArrayList<>();

    public static void registerUdpHandler(Handler<DatagramPacket> handler){
        udpHandlers.add(handler);
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

        udp = vertx.createDatagramSocket();

        udp.listen(12344,"127.0.0.1",datagramSocketAsyncResult -> {
            System.out.println(datagramSocketAsyncResult.succeeded()?"监听12344":"未成功监听");
        });

        System.out.println("配置UDP");
        udp.handler(datagramPacket -> {

            for (Handler<DatagramPacket> udpHandler : udpHandlers) {
                udpHandler.handle(datagramPacket);
            }

        });


        client = vertx.createNetClient(options);
        tryConnect(client);
        rooms = new RoomList();
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

                final String[] s = {"",""};
                socket.handler(r -> {
//                    socket.write(linkCommon(Common.CREATE_ROOM,gson.toJson(user)));
                    System.out.println(r);
                    String msg = r.toString();
                    String[] msgs = msg.split("\\*");

                    if (msgs[0].equals("DATA_HEAD")){
                        //todo 数据头
                        if (msgs.length==3){
                            //还有数据
                            s[0] +=msgs[1];
                            s[1] +=msgs[2];
                        }else if (msgs.length==4){
                            s[0] += msgs[1];
                            s[1] += msgs[2];

                            String[] temp = s.clone();
                            s[0] = "";
                            s[1]="";
                            parseData(temp);
                        }
                    }else {
                        if (msgs.length==2){
                            s[1]+=msgs[0];

                            String[] temp = s.clone();
                            s[0] = "";
                            s[1]="";
                            parseData(temp);
                        }else {
                            s[1]+=msgs[0];
                        }
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

    private void parseData(String[] msgs){

        System.out.println("指令 "+msgs[0]);

        switch (Common.valueOf(msgs[0])){
            case LOGIN_SUCCESS:
                toOther("/hall");
                System.out.println("登录成功，转入大厅界面");
                break;
            case QUERY_ROOM_SUCCESS:
                RoomList set = gson.fromJson(msgs[1], RoomList.class);
                System.out.println("查询大厅房间成功");
                rooms.addAll(set);
                break;
            case CREATE_ROOM_SUCCESS:
                System.out.println("创建房间"+msgs[1]+"成功，转入房间界面");
                int index = Integer.valueOf(msgs[1]);
                ArrayList<User> users = new ArrayList<>();
                user.setRoomIndex(index);
                user.setStatus(UserStatus.Monster);
                users.add(user);
                homeInfo = new HomeInfo("",msgs[1],users);
                jsonObject.put("homeIndex",msgs[1]);
//                  Iterator<Map.Entry<String, Object>> iterator = jsonObject.iterator();
//                  while (iterator.hasNext()){
//                      System.out.println(iterator.next());
//                  }
                toOther("/home");
                break;
            case ENTER_ROOM_SUCCESS:
                //TODO 获取房间内信息
                homeInfo = gson.fromJson(msgs[1], HomeInfo.class);
                System.out.println("进入房间"+homeInfo.getIndex()+"成功，转入房间界面");
                jsonObject.put("homeIndex",homeInfo.getIndex());
                toOther("/home");
                break;
            case EXIT_ROOM_SUCCESS:
                System.out.println("退出房间成功");
                user.setRoomIndex(-1);
                Navigator.pop();
                break;
            case UPDATE_ROOM:
                System.out.println("房间人员有变更");
                homeInfo = gson.fromJson(msgs[1], HomeInfo.class);
                HomePane.update();
                break;
            case GAME_STARTING:
                toOther("/netGame");
                break;

        }
    }

}
