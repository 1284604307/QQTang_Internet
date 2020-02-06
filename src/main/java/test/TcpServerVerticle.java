package test;

import com.google.gson.Gson;
import com.ming.GameObjectManager;
import com.ming.MVC.controller.KeyStack;
import com.ming.MVC.controller.ServerHelper;
import com.ming.MVC.model.*;
import com.ming.MVC.model.Map.*;
import com.ming.MVC.model.Prop.Prop;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.data.BaseData;
import com.ming.MusicManager;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.core.streams.ReadStream;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.Socket;
import java.util.*;

import static com.ming.MVC.model.Behavior.*;


public class TcpServerVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new TcpServerVerticle());
    }
 
    private static final Logger LOGGER = LogManager.getLogger(TcpServerVerticle.class);

    static ServerHelper serverHelper = new ServerHelper();

    //ex playerID , NetSocket
    private static final Map<String, NetSocket> SOCKET_MAP = serverHelper.SOCKET_MAP;
    //ex playerID , Long
    private static final Map<String, Long> ACTIVE_SOCKET_MAP = serverHelper.ACTIVE_SOCKET_MAP;

    //TODO 游戏对象
    private static GameObjectManager gameObjectManager;
    private Player player;

    private Queue<DoMessage> behaviors = new LinkedList<>();
    private KeyStack<KeyCode> keyStack = new KeyStack<>();
    private Gson gson = new Gson();


    class MainUpdateFixed implements Runnable {

        @Override
        public void run() {

//            gameObjectManager.addNode(new Wall(14,1));
//            gameObjectManager.addNode(new Wall(3,1));
//            gameObjectManager.addNode(new Wall(4,1));
//            gameObjectManager.addNode(new Wall(5,1));
//            gameObjectManager.addNode(new IronWall(4,8));
//            gameObjectManager.addNode(new IronWall(5,3));
//            gameObjectManager.addNode(new RedWall(4,5));
//            gameObjectManager.addNode(new RedWall(3,2));
//            gameObjectManager.addNode(new FlowerPort(6,5));
//            gameObjectManager.addNode(new 仙人掌(6,12));

            while (true){
                ArrayList<Item>[][] items = gameObjectManager.getItemsArr();


                //todo 方向更新
                if (!keyStack.isEmpty()) {
                    player.run(keyStack.peek());
                }
                else player.run(KeyCode.STOP);

                // 遵循规则: 下 上 左 右
                //noinspection unchecked
                player.canRun(
                        player.getPoint().y<12?items[player.getPoint().y+1][player.getPoint().x]:null,
                        player.getPoint().y>0?items[player.getPoint().y-1][player.getPoint().x]:null,
                        player.getPoint().x>0?items[player.getPoint().y][player.getPoint().x-1]:null,
                        player.getPoint().x<14?items[player.getPoint().y][player.getPoint().x+1]:null,
                        items[player.getPoint().y][player.getPoint().x]);

                player.update();

                clearBehaviors(gameObjectManager.getItemsArr());


            }

        }
    }


    private ArrayList<Bubble> exBubbles = new ArrayList<>();
    private ArrayList<Exploding> exBubbleExplodings = new ArrayList<>();


    private void clearBehaviors(ArrayList<Item>[][] items) {

        for (int i =0;i<items.length;i++) {
            ArrayList<Item>[] itemArr = items[i];
            for (int j = 0; j < itemArr.length; j++) {
                ArrayList<Item> itemsArr = itemArr[j];
                if (itemsArr == null) continue;
                ListIterator<Item > it =  itemsArr.listIterator();
                while (it.hasNext()){
                    Item item = it.next();
                    if (item.getUnitType() == UnitType.NULL) continue;
                    item.update();
                    // 移除
                    if (item.recycled()){
                        // todo 发送删除节点命令
                        if (item.getUnitType() == UnitType.BUBBLE) {
                            player.releaseBubble();
                            exBubbles.add((Bubble) item);
                        }else if (item.getUnitType() == UnitType.EXPLODING)
                        {
                            exBubbleExplodings.add((Exploding) item);
//                            serverHelper.sendAll(new DoMessage(REMOVE_EXPLODING,gson.toJson(item)));
                        }else
                            it.remove();
                    }
                }
            }
        }

//        if (!behaviors.isEmpty()){
//            Queue<DoMessage> bs = behaviors;
//            while (!bs.isEmpty())
//                serverHelper.sendAll(bs.poll());
//        }

        ListIterator<Bubble > it =  exBubbles.listIterator();
        while (it.hasNext()){
            Bubble item = it.next();
            // 移除
            openExplore(item);
//            serverHelper.sendAll(new DoMessage(REMOVE_BUBBLE,gson.toJson(item)));
            gameObjectManager.deleteNode(item);
            it.remove();
        }

        ListIterator<Exploding > it2 =  exBubbleExplodings.listIterator();
        while (it2.hasNext()){
            Exploding item = it2.next();
            gameObjectManager.deleteNode(item);
            it2.remove();
        }

        serverHelper.sendAll(new DoMessage(UPDATE_ITEM,gson.toJson(gameObjectManager.getItemsArr())));



        // todo 清理本帧行为
//        behaviors.clear();

        try {
//                    Thread.sleep(0x0f);
            Thread.sleep(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openExplore(Bubble item) {

        Point point = item.getPoint();

        // todo 爆炸初始化
        Exploding exploding = new Exploding(point);
        gameObjectManager.addNode(exploding);

        serverHelper.sendAll(new DoMessage(Behavior.ADD_EXPLODING,gson.toJson(exploding)));

        boolean goL=true,
                goR=true,
                goUp=true,
                goDown=true;

        //todo
        // i 从 0开始，被我忽视了
        for (int i = 1; i <= item.getPower(); i++) {

            int pX = point.x;
            int pY = point.y;
            int k = i;
            if (pX-k<0) goL=false;
            if (pX+k>=15) goR=false;
            if (pY-k<0) goUp=false;
            if (pY+k>=13) goDown = false;

            if (goL){
                ArrayList<Item> l = gameObjectManager.getItemArrList(pX-k, pY);
                goL = justJudge(l, pX-k, pY , k==item.getPower()? BaseData.LEFT:BaseData.CENTER);
            }
            if (goR){
                ArrayList<Item> r = gameObjectManager.getItemArrList(pX+k, pY);
                goR = justJudge(r, pX+k, pY , k==item.getPower()? BaseData.RIGHT:BaseData.CENTER);
            }
            if (goUp){
                ArrayList<Item> up = gameObjectManager.getItemArrList( pX, pY-k) ;
                goUp = justJudge(up, pX, pY-k , k==item.getPower()? BaseData.UP:BaseData.CENTER);
            }
            if (goDown){
                ArrayList<Item> down = gameObjectManager.getItemArrList( pX, pY+k);
                goDown = justJudge(down, pX, pY+k , k==item.getPower()? BaseData.DOWN:BaseData.CENTER);
            }

        }
    }


    /**
     * 判断当前延伸方向是否有节点，节点类型及其触发手段
     * @param items
     * @return 返回true 可继续延伸 false 不可继续
     */
    private boolean justJudge(ArrayList<Item> items,int pX,int pY,byte dir){
        if (items==null)  {
            Exploding exploding = new Exploding(new Point(pX, pY),dir);
            gameObjectManager.addNode(exploding);
            serverHelper.sendAll(new DoMessage(Behavior.ADD_EXPLODING,gson.toJson(exploding)));
            return true;
        }
        else
            for (Item item :
                    items) {
                //todo 判断碰撞的节点类型，Wall -> 损坏，Bubble -> 爆炸 , other -> 无行为
                //  这里可以在判断第一个后直接返回，因为一般没有多个触发事件 有以后再添加
                switch (item.getUnitType()){
                    case BUBBLE:
                        ((Bubble)item).dead();
                        return false;
                    case RED_WALL:
                    case WALL:
                        //todo  可炸毁墙的炸毁方法
                        Item dead = item.dead();
                        serverHelper.sendAll(new DoMessage(UPDATE_ITEM,gson.toJson(item)));
                        if (dead instanceof Prop) {
                            gameObjectManager.addNode(dead);
                            serverHelper.sendAll(new DoMessage(ADD_ITEM,gson.toJson(dead)));
                        }
                        return false;
                    case IRON_WALL:
                    case FLOWER_PORT:
                    case CACTUS:
                        return false;
                }
                if (item instanceof Prop)
                    item.recycle();
            }

        Exploding exploding = new Exploding(new Point(pX, pY), dir);
        gameObjectManager.addNode(exploding);
//        serverHelper.sendAll(new DoMessage(Behavior.ADD_EXPLODING,gson.toJson(exploding)));
        return true;
    }



    private void initGameObject() {
        gameObjectManager = new GameObjectManager();
        player = new Player();
    }

    @Override
    public void start() throws Exception {
        NetServerOptions options = new NetServerOptions();
        options.setTcpKeepAlive(true);

        initGameObject();
        final RecordParser parser = RecordParser.newDelimited("\n", h -> {
            final String msg = h.toString();
            LOGGER.info("服务器解包: " + msg);
 
            final String[] msgSplit = msg.split("\\*");
            final String socketId = msgSplit[0];
            final String msgBody = msgSplit[1];
 
            if ("PING".equals(msgBody)) {
                // 心跳
//                LOGGER.debug("接收到 "+socketId+" 客户端的心跳包 ");
                ACTIVE_SOCKET_MAP.put(socketId, System.currentTimeMillis());
                SOCKET_MAP.get(socketId).write(MsgUtils.joinMsg(socketId, "PING"));
            } else {
                // todo 处理指令信息
                DoMessage doMessage = gson.fromJson(msgBody, DoMessage.class);
                if (doMessage.behavior == Behavior.KEY_PRESSED){
                    KeyCode keyCode = KeyCode.valueOf(doMessage.value);
                    switch (keyCode){
                        case UP:
                        case DOWN:
                        case LEFT:
                        case RIGHT:
                            keyStack.push(keyCode);
                            break;
                        case SPACE:
                            if (!player.canAttack()) return;
                            Bubble b = player.attck();
                            if (gameObjectManager.hasItem(b)) {
                                player.releaseBubble();
                            }else {
                                gameObjectManager.addNode(b);
                                // todo 行为加入队列

                                behaviors.add(new DoMessage(Behavior.ADD_BUBBLE,gson.toJson(b)));
                                System.out.println(behaviors);
                            }
                            break;
                    }
                }
                else if (doMessage.behavior == Behavior.KEY_RELEASED){
                    KeyCode keyCode = KeyCode.valueOf(doMessage.value);
                    switch (keyCode){
                        case UP:
                        case DOWN:
                        case LEFT:
                        case RIGHT:
                            keyStack.remove(keyCode);
                            break;
                    }
                }
//                SOCKET_MAP.get(socketId).write(MsgUtils.joinMsg(socketId, msgBody));
            }
        });
 
        NetServer server = vertx.createNetServer(options);
 
        ReadStream<NetSocket> stream = server.connectStream();
        stream.handler(netSocket -> {
            String socketId = netSocket.writeHandlerID();
            LOGGER.debug("New socket Id: " + socketId);
            netSocket.handler(parser::handle);
            netSocket.write(socketId + "*" + "Server\n");
 
            SOCKET_MAP.put(socketId, netSocket);
            ACTIVE_SOCKET_MAP.put(socketId, System.currentTimeMillis());
        });
 
        stream.endHandler(end -> {
            System.out.println("stream end");
        });
 
        stream.exceptionHandler(exception -> {
            System.out.println("stream exception : " + exception.getMessage());
            exception.printStackTrace();
        });

        // 传送地图数据
        vertx.setPeriodic(1000/60, t -> {
            // todo 同步角色位置
            serverHelper.sendAll(new DoMessage(SYN_PLAYER, gson.toJson(player)));
            serverHelper.sendAll(new DoMessage(UPDATE_ALL,gson.toJson(gameObjectManager.getItemsArr())));
        });
        // 检查心跳
        vertx.setPeriodic(1000 * 20, t -> {
//            System.out.println("SOCKET MAP");
//            System.out.println(SOCKET_MAP);
//            System.out.println("ACTIVE MAP");
//            System.out.println(ACTIVE_SOCKET_MAP);
            Iterator<Map.Entry<String, Long>> iterator = ACTIVE_SOCKET_MAP.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                long time = System.currentTimeMillis() - entry.getValue();
                if (time > (1000 * 60)) {
                    //todo 心跳检查20秒一次 ， 当socket超时60s没有回应时，该socket会被移除
                    LOGGER.warn("SocketId: " + entry.getKey() + " 心跳回应超时(60s), 被清除");
                    SOCKET_MAP.remove(entry.getKey()).close();
                    iterator.remove();
                }
            }
        });

        server.listen(8080, res -> {
            if (res.succeeded()) {
                System.out.println("Server start !!!");
            } else {
                res.cause().printStackTrace();
            }
        });

        new Thread(new MainUpdateFixed()).start();

    }

}