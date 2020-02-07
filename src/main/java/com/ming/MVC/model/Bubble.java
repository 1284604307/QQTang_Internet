package com.ming.MVC.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import com.ming.MVC.model.base.Position;
import com.ming.MVC.model.base.Size;
import com.ming.MVC.model.baseInterface.BlockBox;
import com.ming.MusicManager;

import static com.ming.MVC.model.ImageUrl.bubble_red;

public class Bubble extends Item implements BlockBox {
    private int timeCounter = 0;
    private int explodeTimer = 0;
    public int power = 2;
    private boolean addSugerCount = false;
    private boolean hasLeaved=false;
    public int playerId;

    {
        this.unitType = UnitType.BUBBLE;
    }


    public Bubble() {
        image = bubble_red;
    }




    public Bubble initProperties(Bubble b){
        playerId = b.getPlayerId();
        position = b.getPosition();
        point = b.getPoint();
        power = b.getPower();
        image = bubble_red;
        super.setPosition(position); //设置item的position
        return this;
    }

    @Deprecated
    public Bubble(Player player) {
        super(player.point.x,player.point.y);

        position = new Position(player.point.x*40,player.point.y*40);
        playerId=player.getId();
        power = player.getPower();
        image = bubble_red;
        super.setPosition(position); //设置item的position
    }

    public Bubble(Player player,boolean loadImg) {
        super(player.point.x,player.point.y);

        position = new Position(player.point.x*40,player.point.y*40);
        playerId = player.getId();
        power = player.getPower();

        super.setPosition(position); //设置item的position

    }

    public int getPower() {
        return power;
    }

    public Bubble(Position position) {
        this.position = position;
        image = new Image("file:src/main/resources/Images/Bubble/糖炮红.gif");
        super.setPosition(position); //设置item的position
    }

    @Override
    public void draw(GraphicsContext g) {
        if(!dead) {
            g.drawImage(image, position.x, position.y,40,40);
        }else {
            recycled = true;
        }
    }



    @Override
    public void update() {
        if(!dead) {
            explodeTimer += 20;
            if(explodeTimer>=3000) {  //三秒后爆炸
                dead = true;
            }
        }

        if(dead) {
            if(!addSugerCount) {
                addSugerCount = true;
                recycled = true;
            }
        }

    }

    public Position getPosition() {
        return position;
    }

    public boolean recycled(){return recycled;}

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Bubble) {
            Position p = ((Bubble)obj).position;
            return p.x==position.x && p.y==position.y;
        }

        return false;
    }
}
