package com.q;

import com.google.gson.Gson;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import org.ming.connect.enums.Common;
import org.ming.connect.model.HomeInfo;
import org.ming.connect.model.Room;
import org.ming.connect.model.User;
import org.ming.connect.model.UserStatus;
import org.ming.model.RoomList;
import redis.clients.jedis.Jedis;

import java.util.*;

public class ServerMain {

    private static Gson gson = new Gson();
    private static HashMap<String, NetSocket> sockets = new HashMap<>();
    private static HashMap<String, User> users = new HashMap<>();
    private static HashMap<String, String> user_socketId = new HashMap<>();
    private static Jedis jedis;
    private static Random random = new Random();
    private static Vertx vertx;
    public static DatagramSocket udp;
    private static HashMap<Integer,Handler<DatagramPacket>> udps = new HashMap<>();
    public static User getUser(String socketId){
        return users.get(socketId);
    }

    public static void registerUdpHandler(int index,Handler<DatagramPacket> handler){
        System.out.println("注册handler : "+index );
        udps.put(index,handler);
    }

    public static void main(String[] args) {
        vertx = Vertx.vertx();

        NetServer server = vertx.createNetServer();
        jedis = new Jedis("127.0.0.1",6379);

        udp = vertx.createDatagramSocket();
        udp.listen(12345,"127.0.0.1",datagramSocketAsyncResult -> {
            if (datagramSocketAsyncResult.succeeded()) {
                System.out.println("监听12345端口");
            }
        });
        udp.handler(datagramPacket -> {
            String s = datagramPacket.data().toString();
            String[] msgs = s.split("\\*");
            System.out.println("主线程 : "+ datagramPacket.data());
            Handler<DatagramPacket> handler = udps.get(Integer.valueOf(msgs[0]));
            if (handler!=null)
                handler.handle(datagramPacket);
        });

        server.connectHandler(socket->{
            SocketAddress ip = socket.remoteAddress();
            socket.write(ip+" 本地 : "+socket.localAddress());

//            System.out.println(socket.writeHandlerID());
            String sId = socket.writeHandlerID();
            socket.handler(buffer -> {
                String[] res = buffer.toString().split("\\*");
                Common common = Common.valueOf(res[0]);
                String body = res[1];
                switch (common){
                    case LOGIN:
                        User user = gson.fromJson(body, User.class);
                        System.out.println(user);
                        System.out.println(sId +"试图登录");
                        sockets.put(sId,socket);
                        users.put(sId,user);
                        jedis.hset("user-socketId",user.getName(), sId);
                        socket.write(linkCommon(Common.LOGIN_SUCCESS, sId));
                        //todo 如果已登录，向原线程发送挤掉线提示
                        break;
                    case CREATE_ROOM:
                        System.out.println(sId +"试图创建房间");
                        createRoom(socket);
                        break;
                    case QUERY_ROOM:
                        System.out.println("查询大厅所有房间");
                        Set<String> room_list = jedis.smembers("room_list");
                        System.out.println(room_list);
                        RoomList rooms = new RoomList();
                        for (String s : room_list) {
                            Room room = new Room();
                            room.setName("  ");
                            room.setIndex(Integer.valueOf(s));
                            rooms.add(room);
                        }
                        socket.write(linkCommon(
                                Common.QUERY_ROOM_SUCCESS,
                                gson.toJson(rooms)
                        ));
                        break;
                    case ENTER_ROOM:
                        enterRoom(socket,sId,body);
                        break;
                    case EXIT_ROOM:
                        if (exitRoom(sId)) {
                            socket.write(linkCommon(Common.EXIT_ROOM_SUCCESS,""));
                        }
                        break;
                    case GAME_START:
                        int index = getUser(sId).getRoomIndex();
                        Set<String> members = jedis.zrangeByScore("room:" + index, 0, 8);
                        int i = 0;
                        String[] ips = new String[members.size()];
                        for (String member : members) {
                            // todo 发送游戏开始指令，并附加地图名
                            ips[i++] = sockets.get(member).remoteAddress().host();
                            sockets.get(member).write(linkCommon(Common.GAME_STARTING,""));
                        }
                        System.out.println("启动游戏线程,该房间有 " + ips.length + "人");
                        new Thread(new GameConnectThread(index,ips)).start();
                        System.out.println("还是到这里了");
                        break;
                }
            });

            socket.closeHandler(aVoid -> {
                System.out.println(sId +" closed");

                exitRoom(sId);
                if (getUser(sId)!=null){
                    jedis.hdel("user-socketId",users.get(sId).getName());
                    users.remove(sId);
                }
                sockets.remove(sId);

            });
        });


        server.listen(38888,res -> {
            if (res.succeeded()) {
                System.out.println("成功开启38888端口服务");
            }
        });

    }

    private static void enterRoom(NetSocket socket,String sId, String roomIndex){
        User user = getUser(sId);
        //todo
        int index = Integer.valueOf(roomIndex);
        // todo redis 房间信息变更
        jedis.hset("roomStatus:"+index,sId, UserStatus.NONE.name());
        jedis.zadd("room:"+index,1,sId);
        // todo 用户房间信息变更
        user.setRoomIndex(index);
        System.out.println("进入房间"+roomIndex);
        Map<String, String> map = jedis.hgetAll("roomStatus:" + index);
        ArrayList<User> users = new ArrayList<>();
        map.forEach((socketId, status) -> {
            User o = getUser(socketId);
            o.setStatus(UserStatus.valueOf(status));
            users.add(o);
        });
        HomeInfo homeInfo = new HomeInfo("",roomIndex,users);

        //todo 发送变更后的房间信息到用户
        System.out.println(homeInfo);
//        Set<String> smembers = jedis.zrangeByScore("room:" + index,0,8);
        map.forEach((socketId, status) -> {
            System.out.println("同步"+socketId+"的信息");
            if(!socketId.equals(sId)){
                sockets.get(socketId).write(linkCommon(
                        Common.UPDATE_ROOM,
                        gson.toJson(homeInfo)
                ));
            }
        });

        socket.write(linkCommon(
                Common.ENTER_ROOM_SUCCESS,
                gson.toJson(homeInfo)
        ));
    }

    private static boolean exitRoom(String sId){
        User user = users.get(sId);
        if (user==null){
            System.out.println("该客户端未登录");
            return false;
        }
        int roomIndex = user.getRoomIndex();
        if (roomIndex>0){
            System.out.println("用户名退出前在房间"+roomIndex);
            //todo 房间信息变更
            jedis.hdel("roomStatus:" + roomIndex,sId);
            jedis.zrem("room:" + roomIndex,sId);
            // todo 用户信息变更
            user.setRoomIndex(-1);
            //todo 房间没人清除房间 或 通过房间信息到人员
            if (jedis.zcard("room:"+roomIndex)<=0) {

                jedis.srem("room_list",String.valueOf(roomIndex));
            }else {
                Map<String, String> map = jedis.hgetAll("roomStatus:" + roomIndex);
                ArrayList<User> users = new ArrayList<>();
                map.forEach((socketId, status) -> {
                    User o = getUser(socketId);
                    o.setStatus(UserStatus.valueOf(status));
                    users.add(o);
                });
                HomeInfo homeInfo = new HomeInfo("",roomIndex+"",users);
                //todo 发送变更后的房间信息到用户
                map.forEach((socketId, status) -> {
                    System.out.println("同步"+socketId+"的信息");
                    if(!socketId.equals(sId)){
                        sockets.get(socketId).write(linkCommon(
                                Common.UPDATE_ROOM,
                                gson.toJson(homeInfo)
                        ));
                    }
                });
            }
            return true;
        }else {
            System.out.println("玩家不在房间中");
            return false;
        }
    }

    private static void createRoom(NetSocket socket) {
        String sId = socket.writeHandlerID();
        int index = random.nextInt(9999);
        while (jedis.sadd("room_list", String.valueOf(index))!=1){
            index = random.nextInt(9999);
            System.out.println(index+" 房间已存在");
        }
        // monster ok none ban // 房主，已准备，未准备,不能进人
        jedis.hset("roomStatus:"+index,sId,UserStatus.Monster.name());
        jedis.zadd("room:"+index,1,sId);
        getUser(sId).setStatus(UserStatus.Monster);
        getUser(sId).setRoomIndex(index);
        socket.write(linkCommon(Common.CREATE_ROOM_SUCCESS, String.valueOf(index)));
    }

    private static String linkCommon(Common common, String s){
        return "DATA_HEAD*"+common+"*"+s+"*DATA_FOOTER";
    }


}
