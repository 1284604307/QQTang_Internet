package org.ming.model.players;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.ming.controller.KeyStack;
import org.ming.model.World;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Point;
import org.ming.model.base.Position;
import org.ming.model.baseInterface.GameObject;
import org.ming.model.baseInterface.RunBox;
import org.ming.model.bubble.Bubble;
import org.ming.model.prop.Prop;

import java.io.Serializable;

import static org.ming.model.base.BaseData.*;

public class Player extends GameObject implements RunBox, Serializable {

    private transient final static double tranlateX = 4;
    private transient final static double translateY = 19;

    private int id = 0;

    private final static double centerX = 20;
    private final static double centerY = 39;

    private transient KeyStack<KeyCode> keyStack = new KeyStack<>();
    private TransStatus transStatus = TransStatus.NORMAL;
    private transient boolean canRun = true;

    private PlayerStatus playerStatus = PlayerStatus.NORMAL;
    private boolean run = false;
    //无敌时间计数
    private int InvincibleTimer = 0;
    private boolean Invincible = true;

    private int deadTimer = 0;
    private int trapperTimer = 0;
    private int groupId = 1;

    private transient int drawTimer = 0;
    private int kill = 0;
    private int help = 0;



    private byte dir = 0; // 上1 下0 左2 右3
    private int power = 1;
    private int speed = 3;
    private int bubbles = 2;
    private int putBubbles = 0;


    public int getId(){return 0;}

    public Player(Image[] images){
        this.point = new Point(0,0);
        this.position = new Position(0,0);
    }

    public Player(int groupId){
        this.point = new Point(0,0);
        this.position = new Position(0,0);
        this.groupId = groupId;
    }

    public KeyStack<KeyCode> getKeyStack() {
        return keyStack;
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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
                run = false;
                return;
        }
        run = true;


    }

    public void releaseBubble(){
        if (putBubbles>0)
            putBubbles--;
    }

    public boolean canAttack(){
        return putBubbles<getBubbles()&&(playerStatus==PlayerStatus.NORMAL);
    }

    public void attck(World world) {
        if (canAttack()) {
            if (!world.hasBubble(point.y,point.x)) {
                putBubbles++;
                world.putBubble(point.y,point.x,
                        new Bubble(this));
            }
        }
    }

    private byte aliver(int groupId){
        switch (playerStatus){
            case TRAPPED:
                if (groupId==this.groupId) {
                    this.playerStatus = PlayerStatus.NORMAL;
                    return 0;
                }
                else{
                    this.playerStatus = PlayerStatus.DEAD;
                }
                return 1;
            case NORMAL:
//                System.out.println("接触了");
                return -1;
        }
        return -1;
    }

    public void canRun(World world){
        int py = point.y ,px = point.x;

        // 0x04 中间位置 判断是否碰到炸弹爆炸
        if (playerStatus==PlayerStatus.TRAPPED){
            // 待拯救状态
            canRun = false;
//            if (world.hasExploding(py,px)){
//                playerStatus = PlayerStatus.STOPED;
//                return;
//            }
        } else if (playerStatus==PlayerStatus.NORMAL){
            if (world.hasProp(py,px)){
                enterGoods(world.getProp(py,px));
                world.getMusicManager().getGoods();
            }
            if (!Invincible){
                if (world.hasExploding(py,px)){
                    if (transStatus!=TransStatus.NORMAL){
                        transStatus = TransStatus.NORMAL;
                        updateInv();
                    }else {
                    // 正常状态(非无敌)
                        playerStatus = PlayerStatus.TRAPPED;
                        return;
                    }

                }
            }
        }
        if (playerStatus!=PlayerStatus.NORMAL)
            canRun = false;
        else canRun = judge(world);

    }

    protected boolean judge(World world){

        int transX = 0;
        int transY = 0;
        int transPointX = 0;
        int transPointY = 0;
        switch (dir) {
            case DOWN:
                //下
                if (this.position.y+ 79 >=540) {
                    return false;
                }else if (point.y == 14) return true;

                transY = 55;
                transPointY = 1;
                break;
            case UP:
                //上
                if (this.position.y+18 <=0) {
                    return false;
                }else if (point.y == 0) return true;
                transY = -18;
                transPointY = -1;
                break;
            case LEFT:
                //左
                if (this.position.x<=0) {
                    return false;
                }else if (point.x == 0) return true;
                transX = -38;
                transPointX = -1;
                break;
            case RIGHT:
                //右
                if (this.position.x+ 40>=600) {
                    return false;
                }else if (point.x == 12) return true;
                transX = 36;
                transPointX = 1;
                break;
        }
//        if (world.hasProp(point.y+ transPointY,point.x+ transPointX)){
//            double y = world.getProp(point.y + transPointY,point.x + transPointX).position.y - transY;
//            double x = world.getProp(point.y + transPointY,point.x + transPointX).position.x - transX;
//
//            switch (dir){
//                case LEFT:
//                    if ((x > this.position.x ) )
//                        enterGoods(world.getProp(point.y,point.x));;
//                    break;
//                case RIGHT:
//                    if ((x < this.position.x ) )
//                        enterGoods(world.getProp(point.y,point.x));;
//                    break;
//                case UP:
//                    if ((y > this.position.y ) )
//                        enterGoods(world.getProp(point.y,point.x));;
//                    break;
//                case DOWN:
//                    if ((y > this.position.y ) )
//                        enterGoods(world.getProp(point.y,point.x));;
//                    break;
//            }
//            if (y  < this.position.y  && x < this.position.x){
//                enterGoods(world.getProp(point.y,point.x));
//            }
//        }

        if (world.hasWall(point.y+ transPointY,point.x+ transPointX)){
            double y = world.getWall(point.y + transPointY,point.x + transPointX).getY() ;
            double x = world.getWall(point.y + transPointY,point.x + transPointX).getX() - transX ;
            switch (dir){
                case LEFT:
                    if ((x > this.position.x ) )return false;
                    break;
                case RIGHT:
                    if (x <  this.position.x  )return false;
                    break;
                case UP:
                    if ((y- transY > this.position.y ) )return false;
                    break;
                case DOWN:
                    if ((y- transY < this.position.y ) )return false;
                    break;
            }
        }

        if (world.hasBubble(point.y+ transPointY,point.x+ transPointX)){
            double y = world.getBubble(point.y + transPointY,point.x + transPointX).position.y - transY;
            double x = world.getBubble(point.y + transPointY,point.x + transPointX).position.x - transX;
//            System.out.println(y +" Y "+ this.position.y +"  " +x + " X "+this.position.x);
            switch (dir){
                case LEFT:
                    return !(x > this.position.x ) ;
                case RIGHT:
                    return !(x < this.position.x );
                case UP:
                    return !(y > this.position.y ) ;
                case DOWN:
                    return !(y < this.position.y ) ;
            }
        }

        return true;
    }

    private void updateInv(){
        Invincible = true;
        InvincibleTimer = 0;
    }

    // 一个预留的作弊方法 emmm
    public void transMe(PlayerStatus playerStatus){
        this.playerStatus = playerStatus;
    }

    public void enterGoods(Prop prop){
        switch (prop.getUnitType()){
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
                updateInv();
                break;
        }
        prop.recycle();
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public void draw(GraphicsContext g) {
        drawTimer = drawTimer++ % 3;

        g.fillRect(point.x*40,point.y*40,20,20);

        g.drawImage(ImageUrl.arrows.get(drawTimer),position.getX()+8,position.getY()-30);
        //坐标
//        g.fillText(position.toString(),position.x,position.y);

        switch (transStatus){
            case NORMAL:
                switch (playerStatus){
                    case NORMAL:
                        // 绘制人物行走状态
                        if (run){
                            //行走
                            g.drawImage(ImageUrl.Monkey[dir],position.getX(),position.getY());
                        }else {
                            g.drawImage(ImageUrl.Monkey[dir+4],position.getX(),position.getY());
                        }
                        break;
                    case TRAPPED:
                        g.fillText("\n\n我在这，\n快来救我",position.getX(),position.getY(),40);
                        g.setFill(
                                Color.valueOf("#ffffff")
                        );
                        g.fillRect(position.x,position.y,40,60);
                        g.setFill(Color.BLACK);
                        break;
                    case DEAD:

                        break;
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
                return Math.min(MaxPower,power);
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
                return Math.min(MaxSpeed,speed);
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
                return Math.min(MaxBubbles,bubbles);
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
    public void update(World world) {

        //无敌
        if (InvincibleTimer<180){
            InvincibleTimer+=1;
        }else Invincible = false;


        //todo 方向更新
        if (!keyStack.isEmpty()) {
            run(keyStack.peek());
        }
        else run(KeyCode.STOP);

        canRun(world);

        switch (playerStatus){
            case DEAD:
                if (deadTimer<30)
                    deadTimer++;
                else recycle();
                break;
            case TRAPPED:
                if (trapperTimer<180)
                    trapperTimer++;
                else playerStatus = PlayerStatus.DEAD;
                break;
            case NORMAL:
                if (run){
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
                }

                for (Player player : world.getPlayers()) {
                    if (player==this) continue;
                    if (player.position.compareTo( position)==0){
                        switch (player.aliver(groupId)){
                            case 1:
                                kill++;
                                world.getMusicManager().kill.play();
                                break;
                            case 0:
                                help++;
                                world.getMusicManager().help.play();
                                break;
                        }
                    }
                }
                break;
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
