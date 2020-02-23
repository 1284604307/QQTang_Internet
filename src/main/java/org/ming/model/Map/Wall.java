package org.ming.model.Map;

import com.sun.org.apache.regexp.internal.RE;
import org.ming.model.World;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Position;
import org.ming.model.base.UnitType;
import org.ming.model.prop.Prop;
import org.ming.model.prop.TransProp.KngintProp;
import org.ming.model.prop.威力嘎嘎;
import org.ming.model.prop.糖炮多多;
import org.ming.model.prop.跑的快快;
import org.ming.model.base.Point;
import org.ming.model.baseInterface.BlockBox;
import javafx.scene.canvas.GraphicsContext;
import org.ming.model.baseInterface.BlockBox;
import org.ming.model.baseInterface.GameObject;

import java.util.ArrayList;

public class Wall extends GameObject implements BlockBox {

    public transient int transX = 0;
    public transient int transY = -10;
    private int exploreTime = 0;

    {
        super.unitType = UnitType.WALL;
    }

    public Wall(int pointX ,int pointY){
        this.point = new Point(pointX,pointY);
        this.setPosition(new Position(pointX*40+transX,pointY*40+transY));

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
            g.drawImage(ImageUrl.maps.get(this.unitType),this.point.x * 40+transX,this.point.y * 40+transY,42,52);
    }

    @Override
    public void update(World world) {
        if (dead) {
            if (exploreTime > 2)
                this.recycled = true;
            else
                exploreTime++;
        }
    }

    protected void drawDead(GraphicsContext g){
            g.drawImage(ImageUrl.wall_exps[exploreTime%3],this.point.x * 40,this.point.y * 40-10,42,52);
    }

    public Prop getProp(){
        if (!dead){
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

    @Override
    public GameObject dead() {
        System.out.println(this.unitType+" dead !");
        dead=true;
        return null;
    }
}
