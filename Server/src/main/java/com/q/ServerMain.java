package com.q;

import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.core.streams.ReadStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ming.model.World;
import org.ming.model.players.Player;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class ServerMain extends AbstractVerticle {

    private static final Logger LOGGER = LogManager.getLogger(ServerMain.class);
    private Map<String, NetSocket> SOCKET_MAP ;
    private Map<String, Long> ACTIVE_SOCKET_MAP;

    private Jedis jedis ;

    //TODO 游戏对象
    private static World world;
    private Player[] players;
    private Gson gson = new Gson();

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new ServerMain());
    }

    @Override
    public void start() throws Exception {

        jedis = new Jedis("127.0.0.1",6379);

        final RecordParser parser = RecordParser.newDelimited("\n", h -> {
            final String msg = h.toString();
//            LOGGER.info("服务器解包: " + msg);
            final String[] msgSplit = msg.split("\\*");
            final String socketId = msgSplit[0];
            final String msgBody = msgSplit[1];

            if ("PING".equals(msgBody)) {
                // 心跳
//                LOGGER.debug("接收到 "+socketId+" 客户端的心跳包 ");
                ACTIVE_SOCKET_MAP.put(socketId, System.currentTimeMillis());
            }
        });


        NetServerOptions options = new NetServerOptions();
        options.setTcpKeepAlive(true);

        NetServer server = vertx.createNetServer(options);

        ReadStream<NetSocket> stream = server.connectStream();
        stream.handler(netSocket -> {
            String socketId = netSocket.writeHandlerID();
            LOGGER.debug("New socket Id: " + socketId);

            netSocket.handler(parser);
            // TODO 第一次链接发送  socketId - PlyaerId(替换)
            //
            netSocket.write(socketId + "*" + "Server\n");
            netSocket.write(players.length + "*" + "Syn_Players\n");



            SOCKET_MAP.put(socketId, netSocket);
            jedis.expire(socketId,3000);
            ACTIVE_SOCKET_MAP.put(socketId, System.currentTimeMillis());
        });

        stream.endHandler(end -> {
            System.out.println("stream end");
        });

        stream.exceptionHandler(exception -> {
            System.out.println("stream exception : " + exception.getMessage());
            exception.printStackTrace();
        });

        server.listen(8080, res -> {
            if (res.succeeded()) {
                System.out.println("Server start !!!");
            } else {
                res.cause().printStackTrace();
            }
        });

    }
}
