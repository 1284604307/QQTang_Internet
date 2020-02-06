package com.ming;

import com.google.gson.Gson;
import com.ming.MVC.model.Bubble;
import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.Player;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.net.NetServer;

public class serverTest {
    public static void main(String[] args) {

        new Bubble(new Player(),false);
        // 创建TCP服务器
//        Vertx vertx = Vertx.vertx();
//        NetServer server = vertx.createNetServer();
//
//// 处理连接请求
//        server.connectHandler(socket -> {
//
//            socket.handler(buffer -> {
//                // 在这里应该解析报文，封装为协议对象，并找到响应的处理类，得到处理结果，并响应
//                String s = buffer.toString();
//
//                Gson gson = new Gson();
//                Player player = gson.fromJson(s, Player.class);
//                System.out.println(player);
//
//                System.out.println("接收到的数据为：" + s);
//
//                // 按照协议响应给客户端
//                socket.write(Buffer.buffer(String.valueOf(new Player())));
//            });
//
//            // 监听客户端的退出连接
//            socket.closeHandler(close -> {
//                System.out.println("客户端退出连接");
//            });
//
//        });
//
//// 监听端口
//        server.listen(5555, res -> {
//            if (res.succeeded()) {
//                System.out.println("服务器启动成功");
//            }
//        });
    }
}
