package org.ming.model.players;

import com.ming.MVC.controller.KeyStack;
import com.ming.MVC.model.Prop.Prop;
import com.ming.MVC.model.playerForms.骑士;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.base.Position;
import com.ming.MVC.model.baseInterface.AttackObject;
import com.ming.MVC.model.baseInterface.BlockBox;
import com.ming.MVC.model.baseInterface.RunBox;
import com.ming.GameObjectManager;
import com.ming.MusicManager;

import java.io.Serializable;
import java.util.ArrayList;

import static com.ming.MVC.model.data.BaseData.*;
import static com.ming.MVC.model.data.BaseData.DOWN;

public class Player extends Item implements RunBox, AttackObject<Bubble> , Serializable {

    private final static double tranlateX = 4;
    private final static double translateY = 19;


    protected final static double centerX = 20;
    protected final static double centerY = 39;

    private transient GameObjectManager gameObjectManager ;
    private transient KeyStack<KeyCode> keyStack = new KeyStack<>();
    protected TransStatus transStatus = TransStatus.NORMAL;
    protected transient boolean canRun = true;

    protected PlayerStatus playerStatus = PlayerStatus.STOPED;
    //无敌时间计数
    protected int InvincibleTimer = 0;
    protected boolean Invincible = true;

    protected transient int drawTimer = 0;



    protected byte dir = 0; // 上1 下0 左2 右3
    private int power = 1;
    protected int speed = 3;
    private int bubbles = 2;
    private int putBubbles = 0;


    public int getId(){return 0;}

    public Player(Image[] images){
        this.point = new Point(0,0);
        this.position = new Position(0,0);
    }

    public Player(GameObjectManager gameObjectManager){
        this.point = new Point(0,0);
        this.position = new Position(0,0);
        this.gameObjectManager = gameObjectManager;
    }

    public KeyStack<KeyCode> getKeyStack() {
        return keyStack;
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }

    public void initProperty(Player player){
        this.speed = player.speed;
        this.power = player.power;
        this.bubbles = player.bubbles;
        this.point = player.point;

        this.canRun = player.canRun;
        this.dir = player.dir;
        this.dead = player.dead;
        this.recycled = player.recycled;


        if (point.x>14)  point.x = 14;
        else if (point.x<0) point.x = 0;
        if (point.y>12)  point.y = 12;
        else if (point.y<0) point.y = 0;

        this.position = player.position;
        this.Invincible = true;
        this.InvincibleTimer = 0;
    }

    public void updateProperty(Player player){
        this.speed = player.speed;
        this.power = player.power;
        this.bubbles = player.bubbles;
        this.point = player.point;
        this.dir = player.dir;
        this.dead = player.dead;
        this.recycled = player.recycled;
        this.position = player.position;
        this.Invincible = player.Invincible;
        this.InvincibleTimer = player.InvincibleTimer;
        this.playerStatus = player.playerStatus;
        this.transStatus = player.transStatus;

    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public TransStatus getTransStatus() {
        return transStatus;
    }

    @Override
    public void run(KeyCode key) {
        switch (key){
            case LEFT:
                dir = LEFT;
                break;
            case RIGHT:
                dir = RIGHT;
                break;
            case UP:
                dir = UP;
                break;
            case DOWN:
                dir = DOWN;
                break;
            default:
                playerStatus = PlayerStatus.STOPED;
                return;
        }
        setPlayerStatus(PlayerStatus.RUNNING);


    }

    public void releaseBubble(){
        if (putBubbles>0)
            putBubbles--;
    }

    public boolean canAttack(){
        return putBubbles<getBubbles()&&(playerStatus==PlayerStatus.RUNNING||playerStatus==PlayerStatus.STOPED);
    }

    @Override
    public Bubble attck() {
        if (canAttack()) {
            putBubbles++;
            return new Bubble(this,false);
        }
        return null;
    }

    public void canRun(){

        ArrayList<Item>[][] items = gameObjectManager.getItemsArr();

        // 遵循规则: 下 上 左 右 中
        //noinspection unchecked
        ArrayList<Item>[] itemsArr = new ArrayList[]
        {
            getPoint().y < 12 ? items[getPoint().y + 1][getPoint().x] : null,
            getPoint().y > 0 ? items[getPoint().y - 1][getPoint().x] : null,
            getPoint().x > 0 ? items[getPoint().y][getPoint().x - 1] : null,
            getPoint().x < 14 ? items[getPoint().y][getPoint().x + 1] : null,
            items[getPoint().y][getPoint().x]
        };

        // 0x04 中间位置 判断是否碰到炸弹爆炸
        if (playerStatus==PlayerStatus.TRAPPED){
            // 待拯救状态
            canRun = false;
            if (itemsArr[CENTER]!=null)
                for (Item item :
                        itemsArr[CENTER]) {
                    if (item.unitType == UnitType.EXPLODING) {
                        playerStatus = PlayerStatus.STOPED;
                        return;
                    }
                }
        }
        else {
            if (itemsArr[CENTER]!=null)
                for (Item item : itemsArr[CENTER]) {
                    if (item.unitType == UnitType.EXPLODING) {
                        // 正常状态(非无敌)
                        if (!Invincible)
                            playerStatus = PlayerStatus.TRAPPED;
                            return;
                    }
                    if(item instanceof Prop){
                        enterGoods((Prop)item);
                    }
                }
            canRun = judge(itemsArr);

        }

    }

    protected boolean judge(ArrayList<Item>[] itemsArr){
        ArrayList<Item> items;

        switch (dir) {
            case DOWN:
                //下
                if (this.position.y+ 79 >=540) {
                    return false;
                }
                items = itemsArr[dir];
                if (items != null) {
                    for (Item item :
                            items) {
                        if (item instanceof Prop){
                            //TODO
                            if (item.position.y  < this.position.y + 55){
                                enterGoods((Prop)item);
                            }
                            continue;
                        }
                        // todo 只处理继承了碰撞盒子的item
                        if (!(item instanceof BlockBox)) {
                            continue;
                        }
                        if (item.position.y  < this.position.y + 63) {
                            return false;
                        }
                    }
                }

                return true;
            case UP:
                //上
                if (this.position.y+18 <=0) {
                    return false;
                }
                items = itemsArr[dir];
                if (items!=null){
                    for (Item item :
                            items) {
                        if (item instanceof Prop){
                            //TODO
                            if (item.position.y +8 > this.position.y){
                                enterGoods((Prop)item);
                            }
                            continue;
                        }
                        // todo 只处理继承了碰撞盒子的item
                        if (!(item instanceof BlockBox)) {
                            continue;
                        }
                        if (item.position.y +8 > this.position.y){
                            return   false;
                        }
                    }
                }
                return true;
            case LEFT:
                //左
                if (this.position.x<=0) {
                    return false;

                }
                items = itemsArr[dir];
                if (items!=null) {
                    for (Item item :
                            items) {
                        if (item instanceof Prop){
                            //TODO
                            if (item.position.x  > this.position.x - 20){
//                                System.out.println(item.position.x +"  "+this.position.x);
                                enterGoods((Prop)item);
                            }
                            continue;
                        }
                        // todo 只处理继承了碰撞盒子的item
                        if (!(item instanceof BlockBox)) {
                            continue;
                        }
                        if (item.position.x  + 40 > this.position.x) {
                            return false;
                        }
                    }
                }
                return true;
            case RIGHT:
                //右
                if (this.position.x+ 40>=600) {
                    return false;
                }
                items = itemsArr[dir];
                if (items!=null) {
//                    System.out.println(items[DOWN].y+" "+this.position.y);
                    for (Item item :
                            items) {
                        if (item instanceof Prop){

                            //TODOx
                            if (item.position.x  < this.position.x+30){
                                enterGoods((Prop)item);
                            }
                            continue;
                        }
                        // todo 只处理继承了碰撞盒子的item
                        if (!(item instanceof BlockBox)) {
                            continue;
                        }
                        if (item.position.x  < this.position.x+40) {
                            return false;
                        }
                    }
                }
                return true;
            default:
                return true;
        }
    }

    // 一个预留的作弊方法 emmm
    public void transMe(PlayerStatus playerStatus){
        this.playerStatus = playerStatus;
    }

    public void enterGoods(Prop prop){
        switch (prop.unitType){
            case 威力嘎嘎:
                addPower(1);
                break;
            case 糖炮多多:
                addBubbles(1);
                break;
            case 跑的快快:
                addSpeed(1);
                break;
            case KNIGHT_PROP:
                this.transStatus = TransStatus.a骑士;
                break;
        }
        prop.recycle();
        MusicManager.getGoods();
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public void draw(GraphicsContext g) {
        drawTimer = drawTimer++ % 3;
        g.drawImage(ImageUrl.arrows.get(drawTimer),position.getX()+8,position.getY()-30);


        switch (transStatus){
            case NORMAL:
                if (playerStatus == PlayerStatus.TRAPPED){
                    //绘制 死亡/待拯救 状态
                    if (!dead){
                        g.fillText("\n\n我在这，\n快来救我",position.getX(),position.getY(),40);
                    }

                    g.drawImage(ImageUrl.Kngint[dir],position.x,position.y);

                    if (Invincible)
                        g.drawImage(ImageUrl.InvincibleZero,position.x-3,position.y+5);

                }else {
                    // 绘制人物行走状态
                    if (playerStatus == PlayerStatus.RUNNING){
                        //行走
                        g.drawImage(ImageUrl.Monkey[dir],position.getX(),position.getY());
                    }else {
                        g.drawImage(ImageUrl.Monkey[dir+4],position.getX(),position.getY());
                    }
                }
                break;
            case a骑士:
                g.drawImage(ImageUrl.Kngint[dir],position.x,position.y);
                break;
        }

        if (Invincible) {
            g.drawImage(ImageUrl.InvincibleZero,position.x-3,position.y-5);
        }
    }


    public void addPower(int power) {
        if (power<MaxPower)
        this.power += power;
    }

    public void addSpeed(int speed) {
        if (speed<MaxSpeed)
        this.speed += speed;
    }

    public int getPower(){
        switch (transStatus){
            case NORMAL:
            case NULL:
            case a泡泡皇后:
            case a可达鸭:
            case a骑士:
                return power;
            case a反身怪:
                return MaxPower;
        }
        return power;
    }

    public int getSpeed() {
        switch (transStatus){
            case NORMAL:
            case NULL:
            case a泡泡皇后:
            case a可达鸭:
                return speed;
            case a反身怪:
            case a骑士:
                return MaxSpeed;
        }

        return speed;
    }

    public int getBubbles() {
        switch (transStatus){
            case NORMAL:
            case a骑士:
            case NULL:
                return bubbles;
            case a反身怪:
            case a泡泡皇后:
                return MaxBubbles;
            case a可达鸭:
                return 0;
        }
        return bubbles;
    }

    public void addBubbles(int bubbles) {
        if (bubbles<MaxBubbles)
        this.bubbles += bubbles;
    }

    @Override
    public void update() {

        //无敌
        if (InvincibleTimer<180){
            InvincibleTimer+=1;
        }else Invincible = false;

        //todo 方向更新
        if (!keyStack.isEmpty()) {
            run(keyStack.peek());
        }
        else run(KeyCode.STOP);

        canRun();

        if (playerStatus == PlayerStatus.RUNNING){
            if (canRun){
                switch (dir){
                    case LEFT:
                        this.position.translateX(-getSpeed());
                        break;
                    case RIGHT:
                        this.position.translateX(getSpeed());
                        break;
                    case UP:
                        this.position.translateY(-getSpeed());
                        break;
                    case DOWN:
                        this.position.translateY(getSpeed());
                        break;
                }
            }

            // 面向左时，以偏右侧为准得到point值
            this.point.x = (int)(position.x + centerX)/40;
            this.point.y = (int)(position.y + centerY)/40;
            //修正
            if (point.x>14)  point.x = 14;
            else if (point.x<0) point.x = 0;
            if (point.y>12)  point.y = 12;
            else if (point.y<0) point.y = 0;


        }

    }

    public Player trans(){
        switch (transStatus){
            case a骑士:
                return new 骑士(this);
        }
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"transStatus\":")
                .append(transStatus);
        sb.append(",\"canRun\":")
                .append(canRun);
        sb.append(",\"playerStatus\":")
                .append(playerStatus);
        sb.append(",\"InvincibleTimer\":")
                .append(InvincibleTimer);
        sb.append(",\"Invincible\":")
                .append(Invincible);
        sb.append(",\"drawTimer\":")
                .append(drawTimer);
        sb.append(",\"dir\":")
                .append(dir);
        sb.append(",\"power\":")
                .append(power);
        sb.append(",\"speed\":")
                .append(speed);
        sb.append(",\"bubbles\":")
                .append(bubbles);
        sb.append(",\"putBubbles\":")
                .append(putBubbles);
        sb.append(",\"dead\":")
                .append(dead);
        sb.append(",\"recycled\":")
                .append(recycled);
        sb.append(",\"unitType\":")
                .append(unitType);
        sb.append(",\"point\":")
                .append(point);
        sb.append(",\"position\":")
                .append(position);
        sb.append('}');
        return sb.toString();
    }
}
