package org.ming.model.bubble;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.ming.model.World;
import org.ming.model.base.Position;
import org.ming.model.base.UnitType;
import org.ming.model.baseInterface.BlockBox;
import org.ming.model.baseInterface.GameObject;
import org.ming.model.players.Player;

import static org.ming.model.base.ImageUrl.bubble_red;

public class Bubble extends GameObject implements BlockBox {
    private int explodeTimer = 0;
    public int power = 2;
    private transient Player player;
    private boolean addSugerCount = false;
    private boolean boom = false;

    {
        this.unitType = UnitType.BUBBLE;
    }


    public Bubble() {
        image = bubble_red;
    }




    public Bubble initProperties(Bubble b){
        position = b.getPosition();
        point = b.getPoint();
        power = b.getPower();
        image = bubble_red;
        super.setPosition(position); //设置item的position
        return this;
    }

    public Bubble(Player player) {
        super(player.getPoint().x, player.getPoint().y);
        image = bubble_red;
        position = new Position(player.getPoint().x*40, player.getPoint().y*40);
        power = player.getPower();
        super.setPosition(position); //设置item的position
        this.player = player;
    }

    public Bubble(Player player, boolean loadImg) {
        super(player.getPoint().x,player.getPoint().y);

        position = new Position(player.getPoint().x*40,player.getPoint().y*40);
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
    public void update(World world) {
        if (boom) return;
        if(!dead) {
            explodeTimer += 20;
            if(explodeTimer>=3000) {  //三秒后爆炸
                dead = true;
            }
        }else
            recycled = true;

    }

    public void boom() {

        boom = true;
        player.releaseBubble();

    }

    public boolean isBoom() {

        return boom;

    }

    public Position getPosition() {
        return position;
    }

    @Override
    public GameObject dead() {
        dead =true;
        recycled = true;
        return null;
    }

    public boolean recycled(){return recycled;}

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Bubble) {
            Position p = ((Bubble)obj).position;
            return p.x==position.x && p.y==position.y;
        }

        return false;
    }
}
