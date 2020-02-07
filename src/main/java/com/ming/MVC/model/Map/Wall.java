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

import java.util.ArrayList;
import java.util.Random;

public class Wall extends Item  implements BlockBox {

    public int transX = 0;
    public int transY = -10;
    private int exploreTime = 0;


    {
        super.unitType = UnitType.WALL;
    }

    public Wall(int pointX ,int pointY){
        super(new Point(pointX,pointY));
        this.point = new Point(pointX,pointY);
    }

    public Wall(Point point){
        super(point);
    }

    public static Wall init(ArrayList<String> item){
        //ex  [Wall,x,y,pX,pY,dead,recycleD,transX,transY]
        Wall wall = new Wall(Integer.parseInt(item.get(3)), Integer.parseInt(item.get(4)));
        wall.transX = Integer.valueOf(item.get(7));
        wall.transY = Integer.valueOf(item.get(8));
        wall.unitType = UnitType.valueOf(item.get(0));
        return wall;
    }

    public int getExploreTime() {
        return exploreTime;
    }

    public void setExploreTime(int exploreTime) {
        this.exploreTime = exploreTime;
    }

    @Override
    public void draw(GraphicsContext g) {
        if (dead){
            drawDead(g);
        }else
            g.drawImage(ImageUrl.maps.get(this.unitType),this.point.x * 40+transX,this.point.y * 40+transY);
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

    protected void drawDead(GraphicsContext g){
            g.drawImage(ImageUrl.wall_exps[exploreTime],this.point.x * 40,this.point.y * 40-10,40,40);
    }

    @Override
    public Item dead() {
        if (!dead){
            dead=true;
            System.out.println(this.unitType+" dead !");
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
