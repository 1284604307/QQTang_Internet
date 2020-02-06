package com.ming.MVC.model.Map;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.Prop.TransProp.KngintProp;
import com.ming.MVC.model.Prop.威力嘎嘎;
import com.ming.MVC.model.Prop.糖炮多多;
import com.ming.MVC.model.Prop.跑的快快;
import com.ming.MVC.model.UnitType;
import javafx.scene.canvas.GraphicsContext;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.baseInterface.BlockBox;

import java.util.Random;

public class Wall extends Item  implements BlockBox {

    private int exploreTime = 0;

    {
        super.unitType = UnitType.WALL;
    }

    public Wall(int pointX ,int pointY){
        super(new Point(pointX,pointY));
        this.point = new Point(pointX,pointY);
    }

    @Override
    public void draw(GraphicsContext g) {
        if (dead){
            drawDead(g);
        }else
            g.drawImage(ImageUrl.maps.get(this.unitType),this.point.x * 40,this.point.y * 40-10);
    }

    @Override
    public void update() {
        if (dead) {
            if (exploreTime > 2)
                this.recycled = true;
            else
                exploreTime++;
        }
    }

    void drawDead(GraphicsContext g){
            g.drawImage(ImageUrl.wall_exps[exploreTime],this.point.x * 40,this.point.y * 40-10,40,40);
    }

    @Override
    public Item dead() {
        if (!dead){
            dead=true;

            double seed = Math.random();
            if (seed >=0.9){
                return new KngintProp(point);
            }else if(seed>=0.8){
                return new 威力嘎嘎(point);
            }else if (seed>=0.6)
                return new 糖炮多多(point);
            else if (seed>0.4)
                return new 跑的快快(point);
        }
        return null;
    }

}
