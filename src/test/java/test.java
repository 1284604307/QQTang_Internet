import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import javafx.scene.input.KeyCode;

import java.util.Timer;
import java.util.TimerTask;

public class test {
    public static void main(String[] args) {

        System.out.println(KeyCode.DOWN.getName());
        System.out.println(KeyCode.DOWN.toString());

        KeyCode.valueOf("DOWN");
//        KeyCode.valueOf("Down");

//        System.out.println("hahah");
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("3秒后");
//                timer.cancel();
//            }
//        },3000);

//        jedis.smembers("room:" + index)
//        Vertx vertx = Vertx.vertx();
//        NetServer netServer = vertx.createNetServer(new NetServerOptions());
//        netServer.listen(1234,"",res -> {
//            if (res.succeeded()) {
//                System.out.println("游戏数据传输服务接口启动");
//            }
//        });
//        netServer.connectHandler(netSocket -> {
//
//        });

    }
}
