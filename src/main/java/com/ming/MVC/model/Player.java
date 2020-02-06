package com.ming.MVC.model;

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


    protected Image[] images;

    private final static double tranlateX = 4;
    private final static double translateY = 19;

    protected final static double centerX = 20;
    protected final static double centerY = 39;

    //无敌时间计数
    protected int InvincibleTimer = 0;
    protected boolean Invincible = true;

    protected PlayerStatus playerStatus = PlayerStatus.NORMAL;

    protected boolean isRun = false;
    protected boolean canRun = true;
    //困住状态
    protected boolean Trapped = false;

    protected int runTimes = 0;
    protected byte dir = 0; // 上1 下0 左2 右3
    private int power = 1;

    private int bubbles = 2;
    private int putBubbles = 0;

    public int getPower(){return power;}
    public int getId(){return 1;}

    protected int speed = 3;

    public Player(Image[] images){
        this.point = new Point(0,0);
        this.position = new Position(0,0);
        this.images = images;
    }

    public Player(){
        this.point = new Point(0,0);
        this.position = new Position(0,0);
        this.images = images;
    }

    public void initProperty(Player player){
        this.speed = player.speed;
        this.power = player.power;
        this.bubbles = player.bubbles;
        this.point = player.point;

        this.isRun = player.isRun;
        this.canRun = player.canRun;
        this.Trapped = player.Trapped;
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
        this.playerStatus = PlayerStatus.NORMAL;
    }

    public void updateProperty(Player player){
        this.speed = player.speed;
        this.power = player.power;
        this.bubbles = player.bubbles;
        this.point = player.point;

        this.isRun = player.isRun;
        this.canRun = player.canRun;
        this.Trapped = player.Trapped;
        this.dir = player.dir;
        this.dead = player.dead;
        this.recycled = player.recycled;

        this.position = player.position;
        this.Invincible = player.Invincible;
        this.InvincibleTimer = player.InvincibleTimer;
        this.playerStatus = player.playerStatus;
    }
    @Override
    public void run(KeyCode key) {
        isRun = true;
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
                isRun = false;
        }


    }

    public void releaseBubble(){
        if (putBubbles>0)
            putBubbles--;
    }

    public boolean applyBubble(){
        if (bubbles>putBubbles) {
            putBubbles++;
            return true;
        }
        return false;
    }


    public boolean canAttack(){
        if (playerStatus == PlayerStatus.a可达鸭) return false;
        return putBubbles<bubbles&&(!Trapped&&!dead);
    }

    @Override
    public Bubble attck() {
        if (canAttack()) {
            putBubbles++;
            return new Bubble(this,false);
        }
        return null;
    }

    public void canRun(ArrayList<Item>... itemsArr){
        // 0x04 中间位置 判断是否碰到炸弹爆炸
        if (Trapped){
            // 待拯救状态
            canRun = false;
            if (itemsArr[CENTER]!=null)
                for (Item item :
                        itemsArr[CENTER]) {
                    if (item.unitType == UnitType.EXPLODING) {
                        Trapped = false;
                        return;
                    }
                }
        }else {
            if (itemsArr[CENTER]!=null)
                for (Item item : itemsArr[CENTER]) {
                    if (item.unitType == UnitType.EXPLODING) {
                        // 正常状态(非无敌)
                        if (!Invincible)
                            Trapped = true;
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
                                System.out.println(item.position.x +"  "+this.position.x);
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
                if (power<MaxPower)
                    this.power++;
                break;
            case 糖炮多多:
                if (bubbles<MaxBubbles)
                    this.bubbles++;
                break;
            case 跑的快快:
                if (speed<MaxSpeed)
                    this.speed++;
                break;
            case KNIGHT_PROP:
                this.playerStatus = PlayerStatus.a骑士;
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

        g.fillRect(point.x*40,point.y*40,10,10);



        if (Trapped){
            //绘制 死亡/待拯救 状态
            if (!dead){
                g.fillText("\n\n我在这，\n快来救我",position.getX(),position.getY(),40);
            }
        }else {
            // 绘制人物行走状态
            if (isRun){
                //行走
                if (runTimes>16)
                    runTimes=0;
                else
                    runTimes ++;

                g.drawImage(this.images[runTimes/4%4+dir*4],position.getX(),position.getY());
            }else {
                g.drawImage(this.images[dir*4],position.getX(),position.getY());
                runTimes=0;
            }
        }
        if (Invincible) {
            g.drawImage(ImageUrl.InvincibleZero,position.x-3,position.y-5);

        }
    }



    @Override
    public void update() {


        //无敌
        if (InvincibleTimer<2000){
            InvincibleTimer+=0x0f;
        }else Invincible = false;

        if (isRun){
            if (canRun){
                switch (dir){
                    case LEFT:
                        this.position.translateX(-speed);
                        break;
                    case RIGHT:
                        this.position.translateX(speed);
                        break;
                    case UP:
                        this.position.translateY(-speed);
                        break;
                    case DOWN:
                        this.position.translateY(speed);
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
        switch (playerStatus){
            case a骑士:
                return new 骑士(this);
            case NORMAL:
                return this;
        }
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"Invincible\":")
                .append(Invincible);
        sb.append(",\"position\":")
                .append(position);
        sb.append(",\"isRun\":")
                .append(isRun);
        sb.append(",\"canRun\":")
                .append(canRun);
        sb.append(",\"Trapped\":")
                .append(Trapped);
        sb.append(",\"dir\":")
                .append(dir);
        sb.append(",\"power\":")
                .append(power);
        sb.append(",\"bubbles\":")
                .append(bubbles);
        sb.append(",\"putBubbles\":")
                .append(putBubbles);
        sb.append(",\"speed\":")
                .append(speed);
        sb.append(",\"dead\":")
                .append(dead);
        sb.append(",\"recycled\":")
                .append(recycled);
        sb.append(",\"unitType\":")
                .append(unitType);
        sb.append(",\"point\":")
                .append(point);
        sb.append('}');
        return sb.toString();
    }
}
